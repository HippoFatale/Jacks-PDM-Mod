package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CropPriceDataSyncS2CPacket {
    private final Map<String, MarketItem> cropMarketData;

    public CropPriceDataSyncS2CPacket(Map<String, MarketItem> cropMarketData) {
        this.cropMarketData = cropMarketData;
    }

    public CropPriceDataSyncS2CPacket(PacketBuffer buf) {
        this.cropMarketData = new HashMap<>();

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            String registryName = buf.readUtf();
            int price = buf.readInt();
            int quantity = buf.readInt();

            MarketItem item = new MarketItem(Items.AIR, price, quantity);
            this.cropMarketData.put(registryName, item);
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.cropMarketData.size());
        for (Map.Entry<String, MarketItem> entry : this.cropMarketData.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeInt(entry.getValue().getPrice());
            buf.writeInt(entry.getValue().getQuantity());
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            for (MarketItem clientItem : MarketManager.cropMarketItems) {
                if (clientItem == null || clientItem.getRegistryName() == null) continue;

                String registryName = clientItem.getRegistryName();

                if (this.cropMarketData.containsKey(registryName)) {
                    int livePrice = this.cropMarketData.get(registryName).getPrice();

                    clientItem.setPrice(livePrice);
                }
            }
        });
        return true;
    }
}
