package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientTeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportDataSyncS2CPacket {
    private boolean[] teleportUnlockedList;
    private boolean homeUnlocked = false;
    private boolean clubHomeUnlocked = false;

    public TeleportDataSyncS2CPacket(boolean[] teleportUnlockedList, boolean homeUnlocked, boolean clubHomeUnlocked) {
        this.teleportUnlockedList = teleportUnlockedList.clone();
        this.homeUnlocked = homeUnlocked;
        this.clubHomeUnlocked = clubHomeUnlocked;
    }

    public TeleportDataSyncS2CPacket(ByteBuf buf) {
        int teleportCount = buf.readInt();
        this.teleportUnlockedList = new boolean[teleportCount];
        for (int i = 0; i < teleportCount; i++) {
            this.teleportUnlockedList[i] = buf.readBoolean();
        }
        this.homeUnlocked = buf.readBoolean();
        this.clubHomeUnlocked = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(teleportUnlockedList.length);
        for (int i = 0; i < teleportUnlockedList.length; i++) {
            buf.writeBoolean(teleportUnlockedList[i]);
        }
        buf.writeBoolean(homeUnlocked);
        buf.writeBoolean(clubHomeUnlocked);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientTeleportData.setTeleportUnlockedList(teleportUnlockedList);
            ClientTeleportData.setHomeUnlocked(homeUnlocked);
            ClientTeleportData.setClubHomeUnlocked(clubHomeUnlocked);
        });
        return true;
    }
}
