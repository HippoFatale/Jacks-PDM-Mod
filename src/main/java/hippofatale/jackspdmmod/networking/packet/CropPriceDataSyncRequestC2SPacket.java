package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientCropPriceData;
import hippofatale.jackspdmmod.networking.ModMessages;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;

public class CropPriceDataSyncRequestC2SPacket {
    public CropPriceDataSyncRequestC2SPacket() {
    }

    public CropPriceDataSyncRequestC2SPacket(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            int[] cropPrices = {marketPrices.get("melon_slice"), marketPrices.get("pumpkin"), marketPrices.get("cocoa_beans"), marketPrices.get("wheat"), marketPrices.get("potato"), marketPrices.get("carrot")};
            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropPrices), player);
        });
        return true;
    }
}
