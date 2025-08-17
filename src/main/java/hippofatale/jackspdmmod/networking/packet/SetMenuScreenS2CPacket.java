package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientHooks;
import hippofatale.jackspdmmod.screen.MenuScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMenuScreenS2CPacket {
    public SetMenuScreenS2CPacket() {

    }

    public SetMenuScreenS2CPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHooks.openMenuScreen();
        });
        return true;
    }
}
