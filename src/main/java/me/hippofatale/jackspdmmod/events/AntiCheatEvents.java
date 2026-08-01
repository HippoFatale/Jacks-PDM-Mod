package me.hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.events.PokemonSendOutEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.minigames.JumpMapRace;
import me.hippofatale.jackspdmmod.minigames.MagmaFall;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class AntiCheatEvents {
    private static final AxisAlignedBB maze = new AxisAlignedBB(-234 - 1, 96 - 1, -1826 + 1, 130 + 1, 109 + 1, -1890 - 1);
    private static final AxisAlignedBB eventEntrance = new AxisAlignedBB( -53 - 1, 41, 2 - 1, -48 + 1, 49, 15 + 1);
    private static final AxisAlignedBB prison = new AxisAlignedBB(-116 - 1, 20 - 1, -18 - 1, -96 + 1, 41 + 1, 14 + 1);
    private static final AxisAlignedBB court = new AxisAlignedBB(-263 - 1, 96 - 1, -2014 - 1, -230 + 1, 108 + 1, -2000 + 1);
    private static final List<AxisAlignedBB> miniGameFields = Arrays.asList(
            MagmaFall.field,
            JumpMapRace.field
    );
    private static final List<AxisAlignedBB> specialFields = Arrays.asList(
            MagmaFall.field,
            JumpMapRace.field,
            maze,
            eventEntrance,
            prison,
            court

    );

    private static boolean isPlayerInMiniGameField(ServerPlayerEntity player) {
        return miniGameFields.stream().anyMatch(field -> player.getBoundingBox().intersects(field));
    }

    private static boolean isPlayerInSpecialField(ServerPlayerEntity player) {
        return specialFields.stream().anyMatch(field -> player.getBoundingBox().intersects(field));
    }

    //tp when logged in minigame field
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if (isPlayerInMiniGameField(player) && player.gameMode.isSurvival()) {
                player.teleportTo(
                        MiniGameRunEvents.returnPoint.x,
                        MiniGameRunEvents.returnPoint.y,
                        MiniGameRunEvents.returnPoint.z);
            }
        }
    }

    @SubscribeEvent
    public static void onUseFishingRod(PlayerInteractEvent.RightClickItem event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if (event.getItemStack().getItem() instanceof net.minecraft.item.FishingRodItem) {
                if (isPlayerInMiniGameField(player) && player.gameMode.isSurvival()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    //TODO
    //no riding
    @SubscribeEvent
    public static void onRiding(EntityMountEvent event) {
        if (event.isDismounting()) {
            return;
        }
        if (event.getEntityMounting() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityMounting();
            if (isPlayerInSpecialField(player) && player.gameMode.isSurvival()) {
                if ((event.getEntityBeingMounted() instanceof PixelmonEntity || event.getEntityBeingMounted() instanceof BikeEntity)) {
                    event.setCanceled(true);
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.riding_is_forbidden").withStyle(TextFormatting.RED), false);
                }
            }
        }
    }

    //cancel pokemon send out
    @SubscribeEvent
    public static void onPokemonSendOut(PokemonSendOutEvent.Pre event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (isPlayerInSpecialField(player) && player.gameMode.isSurvival()) {
            event.setCanceled(true);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.send_out_is_forbidden").withStyle(TextFormatting.RED), false);
        }
    }

    @SubscribeEvent
    public static void onPokemonSpawn(SpawnEvent event) {
        BlockPos spawnPos = event.action.spawnLocation.location.pos.immutable();
        if (specialFields.stream().anyMatch(field -> field.contains(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()))) {
            event.setCanceled(true);
        }
    }
}
