package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CropPriceDataSyncRequestC2SPacket {
    public CropPriceDataSyncRequestC2SPacket() {
    }

    public CropPriceDataSyncRequestC2SPacket(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            Map<String, MarketItem> cropDataToSync = new HashMap<>();

            for (MarketItem item : MarketManager.cropMarketItems) {
                if (item != null && item.getRegistryName() != null) {
                    cropDataToSync.put(item.getRegistryName(), item);
                }
            }

            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropDataToSync), player);
        });
        return true;
    }
}
