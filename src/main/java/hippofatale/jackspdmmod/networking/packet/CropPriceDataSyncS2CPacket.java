package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientCropPriceData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CropPriceDataSyncS2CPacket {
    private int[] cropPriceList = {512, 1300, 960, 640, 384, 384};

    public CropPriceDataSyncS2CPacket(int[] cropPriceList) {
        this.cropPriceList = cropPriceList.clone();
    }

    public CropPriceDataSyncS2CPacket(ByteBuf buf) {
        for (int i = 0; i < this.cropPriceList.length; i++) {
            this.cropPriceList[i] = buf.readInt();
        }
    }

    public void toBytes(ByteBuf buf) {
        for (int i = 0; i < this.cropPriceList.length; i++) {
            buf.writeInt(cropPriceList[i]);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientCropPriceData.setCropPriceList(cropPriceList);
        });
        return true;
    }
}
