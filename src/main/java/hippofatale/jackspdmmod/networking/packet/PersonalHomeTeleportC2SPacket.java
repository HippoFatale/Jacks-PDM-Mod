package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.home.HomeData;
import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static hippofatale.jackspdmmod.JacksPDMMod.personalHomes;

public class PersonalHomeTeleportC2SPacket {
    public PersonalHomeTeleportC2SPacket() {

    }

    public PersonalHomeTeleportC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                if (personalHomes.getOrDefault(player.getUUID(), null) != null) {
                    if (player.getVehicle() != null) {
                        Entity vehicle = player.getVehicle();
                        player.stopRiding();
                        player.moveTo(HomeData.getOwningPersonalHome(player.getUUID()).getTeleportPoint());
                        vehicle.moveTo(HomeData.getOwningPersonalHome(player.getUUID()).getTeleportPoint());
                        player.startRiding(vehicle);
                    } else {
                        player.moveTo(HomeData.getOwningPersonalHome(player.getUUID()).getTeleportPoint());
                    }
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported", new TranslationTextComponent("menu.jackspdmmod.teleport_home")), false);
                } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
                }
            }
        });
        return true;
    }
}
