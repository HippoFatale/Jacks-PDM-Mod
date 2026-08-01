package me.hippofatale.jackspdmmod.minigames;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.events.MiniGameRunEvents;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class JumpMapRace {
    //starting point
    private static final AxisAlignedBB startArea = new AxisAlignedBB(-1557, 58, 73, -1549, 59, 87);
    private static final AxisAlignedBB goalArea = new AxisAlignedBB(-1556, 72, 97, -1553, 73, 100);
    public static final AxisAlignedBB field = new AxisAlignedBB(-1581 - 1, 57, 73 - 1, -1549 + 1, 80, 100 + 1);
//    private static final Vector3d returnPoint = new Vector3d(-93, 44, -8);
    private static final int timeLimit = 15; //minutes

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static MinecraftServer server;


    public static void prepareJumpMapRace(MinecraftServer server) {
        if (server == null) {
            return;
        }
        JumpMapRace.server = server;
    }

    public static void beginJumpMapRace() {
        if (server == null) {
            return;
        }

        //teleport applicants to field + remove rewarded tag
        for (UUID playerUUID : MiniGameManager.miniGameApplicants) {
            ServerPlayerEntity player = (ServerPlayerEntity) server.overworld().getPlayerByUUID(playerUUID);
            if (player != null && BattleRegistry.getBattle(player) == null) {
                player.stopRiding();
                player.teleportTo(
                        (startArea.minX + 0.5) + (startArea.getXsize() - 1) * Math.random(),
                        startArea.maxY,
                        (startArea.minZ + 0.5) + (startArea.getZsize() - 1) * Math.random()
                );
                player.getPersistentData().remove("jump_map_race_reward_received");
            }
        }

        jumpMapRaceEvents(5);
        jumpMapRaceEvents(10);
        jumpMapRaceEvents(12);
        jumpMapRaceEvents(14);
        jumpMapRaceEvents(15);
    }

    private static void jumpMapRaceEvents(int eventTime) {
        scheduler.schedule(() -> {
            try {
                if (server != null) {
                    server.execute(() -> {
                        switch (eventTime) {
                            case timeLimit:
                                endJumpMapRace();
                                break;
                            default:
                                timeLeftMessage(timeLimit - eventTime);
                                break;
                        }
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, eventTime, TimeUnit.MINUTES);
    }

    public static void endJumpMapRace() {
        MiniGameManager.isMiniGameRunning = false;

        //kick left players from field
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (ServerPlayerEntity player : players) {
            if (player.getBoundingBox().intersects(field)) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_time_ended"), false);
                player.teleportTo(
                        MiniGameRunEvents.returnPoint.x,
                        MiniGameRunEvents.returnPoint.y,
                        MiniGameRunEvents.returnPoint.z);
            }
        }
    }

    private static void timeLeftMessage(int timeLeft) {
        //send time left msg
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (ServerPlayerEntity player : players) {
            if (player.getBoundingBox().intersects(field)) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_time_left",
                        new StringTextComponent(Integer.toString(timeLeft)).withStyle(TextFormatting.YELLOW)), false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerGoalIn(TickEvent.PlayerTickEvent event) {
        if (!(MiniGameManager.miniGameType == MiniGameType.JUMP_MAP_RACE && MiniGameManager.isMiniGameRunning)) {
            return;
        }
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            //check rewarded tag
            if (player.getPersistentData().getBoolean("jump_map_race_reward_received")) {
                return;
            }

            if (player.getBoundingBox().intersects(goalArea) && player.gameMode.isSurvival()) {
                //add rewarded tag
                player.getPersistentData().putBoolean("jump_map_race_reward_received", true);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_race_goal"), false);
                player.teleportTo(
                        MiniGameRunEvents.returnPoint.x,
                        MiniGameRunEvents.returnPoint.y,
                        MiniGameRunEvents.returnPoint.z);
                player.inventory.add(new ItemStack(Items.NETHER_STAR, 1));
            }
        }
    }
}
