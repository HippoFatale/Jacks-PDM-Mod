package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportC2SPacket {
    private int teleportIndex;

    public TeleportC2SPacket(int teleportIndex) {
        this.teleportIndex = teleportIndex;
    }

    public TeleportC2SPacket(ByteBuf buf) {
        this.teleportIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(teleportIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                player.moveTo(TeleportData.getTeleportCoordinates(teleportIndex));
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported", TeleportData.getTeleportName(teleportIndex)), false);
            }
        });
        return true;
    }
}
