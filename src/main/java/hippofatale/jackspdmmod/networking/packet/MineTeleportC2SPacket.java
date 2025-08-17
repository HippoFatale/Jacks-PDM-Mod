package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MineTeleportC2SPacket {
    public MineTeleportC2SPacket() {

    }

    public MineTeleportC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                player.moveTo(TeleportData.getMineCoordinates());
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported", new TranslationTextComponent("menu.jackspdmmod.teleport_mine")), false);
            }
        });
        return true;
    }
}
