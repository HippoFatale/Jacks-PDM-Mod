package me.hippofatale.jackspdmmod.networking.packet;

import io.netty.buffer.ByteBuf;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.home.Home;
import me.hippofatale.jackspdmmod.home.HomeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
                if (!ClubManager.belongingClubs.containsKey(player.getUUID())) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                } else if (HomeManager.clubHomes.get(ClubManager.belongingClubs.get(player.getUUID()).getClubName()) != null) {
                    BlockPos teleportPos = HomeManager.clubHomes.get(ClubManager.belongingClubs.get(player.getUUID()).getClubName()).getTeleportPos();
                    if (player.getVehicle() != null) {
                        Entity vehicle = player.getVehicle();
                        vehicle.teleportToWithTicket(teleportPos.getX() + 0.5, teleportPos.getY(), teleportPos.getZ() + 0.5);
                    } else {
                        player.teleportToWithTicket(teleportPos.getX() + 0.5, teleportPos.getY(), teleportPos.getZ() + 0.5);
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
