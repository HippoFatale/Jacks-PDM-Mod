package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.home.HomeData;
import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static hippofatale.jackspdmmod.JacksPDMMod.clubNameHomes;
import static hippofatale.jackspdmmod.JacksPDMMod.playerClubs;

public class ClubHomeTeleportC2SPacket {
    public ClubHomeTeleportC2SPacket() {

    }

    public ClubHomeTeleportC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                if (!playerClubs.containsKey(player.getUUID())) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                } else if (clubNameHomes.get(playerClubs.get(player.getUUID())) != null) {
                    if (player.getVehicle() != null) {
                        Entity vehicle = player.getVehicle();
                        player.stopRiding();
                        player.moveTo(HomeData.getHomeFromBelongingClubName(player.getUUID()).getTeleportPoint());
                        vehicle.moveTo(HomeData.getHomeFromBelongingClubName(player.getUUID()).getTeleportPoint());
                        player.startRiding(vehicle);
                    } else {
                        player.moveTo(HomeData.getHomeFromBelongingClubName(player.getUUID()).getTeleportPoint());
                    }
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported", new TranslationTextComponent("menu.jackspdmmod.teleport_club_home")), false);
                } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_club_home"), false);
                }
            }
        });
        return true;
    }
}
