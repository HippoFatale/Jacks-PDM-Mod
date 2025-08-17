package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TownTeleportC2SPacket {
    private int townIndex;

    public TownTeleportC2SPacket(int townIndex) {
        this.townIndex = townIndex;
    }

    public TownTeleportC2SPacket(ByteBuf buf) {
        this.townIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(townIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                player.moveTo(TeleportData.getTownCoordinates(townIndex));
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported", TeleportData.getTownText(townIndex)), false);
            }
        });
        return true;
    }
}
