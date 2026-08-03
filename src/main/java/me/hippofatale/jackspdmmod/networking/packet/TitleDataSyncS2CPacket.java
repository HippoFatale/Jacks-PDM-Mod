package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientTitleData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TitleDataSyncS2CPacket {
    private int displayingTitleIndex = 0;
    private boolean[] titleUnlockedList;

    public TitleDataSyncS2CPacket(int displayingTitleIndex, boolean[] titleUnlockedList) {
        this.displayingTitleIndex = displayingTitleIndex;
        this.titleUnlockedList = titleUnlockedList.clone();
    }

    public TitleDataSyncS2CPacket(ByteBuf buf) {
        this.displayingTitleIndex = buf.readInt();
        int titleCount = buf.readInt();
        this.titleUnlockedList = new boolean[titleCount];
        for (int i = 0; i < titleCount; i++) {
            this.titleUnlockedList[i] = buf.readBoolean();
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(displayingTitleIndex);
        buf.writeInt(titleUnlockedList.length);
        for (int i = 0; i < titleUnlockedList.length; i++) {
            buf.writeBoolean(titleUnlockedList[i]);
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
