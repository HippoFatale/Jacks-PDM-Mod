package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientHooks;
import hippofatale.jackspdmmod.screen.MenuScreen;
import hippofatale.jackspdmmod.screen.StarterPackageTicketScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
