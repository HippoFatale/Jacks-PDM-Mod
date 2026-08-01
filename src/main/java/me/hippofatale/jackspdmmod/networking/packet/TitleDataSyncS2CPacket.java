package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientTitleData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TitleDataSyncS2CPacket {
    private int displayingTitleIndex = 0;
    private int[] titleUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public TitleDataSyncS2CPacket(int displayingTitleIndex, int[] titleUnlockedList) {
        this.displayingTitleIndex = displayingTitleIndex;
        for (int i = 0; i < this.titleUnlockedList.length; i++) {
            this.titleUnlockedList[i] = titleUnlockedList[i];
        }
    }

    public TitleDataSyncS2CPacket(ByteBuf buf) {
        this.displayingTitleIndex = buf.readInt();
        for (int i = 0; i < this.titleUnlockedList.length; i++) {
            this.titleUnlockedList[i] = buf.readInt();
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(displayingTitleIndex);
        for (int i = 0; i < this.titleUnlockedList.length; i++) {
            buf.writeInt(titleUnlockedList[i]);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientTitleData.setDisplayingTitleIndex(displayingTitleIndex);
            ClientTitleData.setTitleUnlockedList(titleUnlockedList);
        });
        return true;
    }
}
