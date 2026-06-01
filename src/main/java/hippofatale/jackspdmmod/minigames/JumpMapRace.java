package hippofatale.jackspdmmod.minigames;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.events.MiniGameRunEvents;
import hippofatale.jackspdmmod.util.MiniGameType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class JumpMapRace {
    //starting point
    private static final AxisAlignedBB startArea = new AxisAlignedBB(-1557, 58, 73, -1549, 59, 87);
    private static final AxisAlignedBB goalArea = new AxisAlignedBB(-1556, 72, 97, -1553, 73, 100);
    public static final AxisAlignedBB field = new AxisAlignedBB(-1581, 57, 73, -1549, 80, 100);
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

        //teleport applicants to field
        for (UUID playerUUID : miniGameApplicants) {
            ServerPlayerEntity player = (ServerPlayerEntity) server.overworld().getPlayerByUUID(playerUUID);
            if (player != null && BattleRegistry.getBattle(player) == null) {
                player.teleportTo(
                        (startArea.minX + 0.5) + (startArea.getXsize() - 1) * Math.random(),
                        startArea.maxY,
                        (startArea.minZ + 0.5) + (startArea.getZsize() - 1) * Math.random()
                );
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
        isMiniGameRunning = false;

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
        if (!(miniGameType == MiniGameType.JUMP_MAP_RACE && isMiniGameRunning)) {
            return;
        }
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (player.getBoundingBox().intersects(goalArea) && player.gameMode.isSurvival()) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_race_goal"), false);
                player.inventory.add(new ItemStack(Items.NETHER_STAR, 1));
                player.teleportTo(
                        MiniGameRunEvents.returnPoint.x,
                        MiniGameRunEvents.returnPoint.y,
                        MiniGameRunEvents.returnPoint.z);
            }
        }
    }
}
