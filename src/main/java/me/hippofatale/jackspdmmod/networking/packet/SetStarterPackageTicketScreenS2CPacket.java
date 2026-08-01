package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetStarterPackageTicketScreenS2CPacket {
    public SetStarterPackageTicketScreenS2CPacket() {

    }

    public SetStarterPackageTicketScreenS2CPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHooks.openStarterPackageTicketScreen();
        });
        return true;
    }
}
