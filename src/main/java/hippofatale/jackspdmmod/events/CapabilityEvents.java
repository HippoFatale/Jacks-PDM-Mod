package hippofatale.jackspdmmod.events;

import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.TeleportDataSyncS2CPacket;
import hippofatale.jackspdmmod.networking.packet.TitleDataSyncS2CPacket;
import hippofatale.jackspdmmod.storage.PlayerStorageProvider;
import hippofatale.jackspdmmod.teleport.PlayerTeleportUnlockProvider;
import hippofatale.jackspdmmod.title.PlayerTitleProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class CapabilityEvents {
    //attach capabilities
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            //teleport unlock
            if (!event.getObject().getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).isPresent()) {
                event.addCapability(new ResourceLocation(JacksPDMMod.MOD_ID, "properties_teleport"), new PlayerTeleportUnlockProvider());
            }

            //storage
            if (!event.getObject().getCapability(PlayerStorageProvider.PLAYER_STORAGE).isPresent()) {
                event.addCapability(new ResourceLocation(JacksPDMMod.MOD_ID, "properties_storage"), new PlayerStorageProvider());
            }

            //title
            if (!event.getObject().getCapability(PlayerTitleProvider.PLAYER_TITLE).isPresent()) {
                event.addCapability(new ResourceLocation(JacksPDMMod.MOD_ID, "properties_title"), new PlayerTitleProvider());
            }
        }
    }

    //sync capabilities on login
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), player);
            });
            player.getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).ifPresent(playerTeleportUnlock -> {
                ModMessages.sendToPlayer(new TeleportDataSyncS2CPacket(playerTeleportUnlock.getTeleportUnlockedList(), playerTeleportUnlock.getHomeUnlocked(), playerTeleportUnlock.getClubHomeUnlocked()), player);
            });
        }
    }

    //preserve capabilities on death
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            //teleport unlock
            event.getOriginal().getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

            //storage
            event.getOriginal().getCapability(PlayerStorageProvider.PLAYER_STORAGE).ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerStorageProvider.PLAYER_STORAGE).ifPresent(newStore -> {
                    CompoundNBT nbt = new CompoundNBT();
                    oldStore.saveNBTData(nbt);
                    newStore.loadNBTData(nbt);
                });
            });

            //title
            event.getOriginal().getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
