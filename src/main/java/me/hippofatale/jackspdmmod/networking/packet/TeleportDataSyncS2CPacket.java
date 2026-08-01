package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientTeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportDataSyncS2CPacket {
    private int[] teleportUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private boolean homeUnlocked = false;
    private boolean clubHomeUnlocked = false;

    public TeleportDataSyncS2CPacket(int[] teleportUnlockedList, boolean homeUnlocked, boolean clubHomeUnlocked) {
        for (int i = 0; i < this.teleportUnlockedList.length; i++) {
            this.teleportUnlockedList[i] = teleportUnlockedList[i];
        }
        this.homeUnlocked = homeUnlocked;
        this.clubHomeUnlocked = clubHomeUnlocked;
    }

    public TeleportDataSyncS2CPacket(ByteBuf buf) {
        for (int i = 0; i < this.teleportUnlockedList.length; i++) {
            this.teleportUnlockedList[i] = buf.readInt();
        }
        this.homeUnlocked = buf.readBoolean();
        this.clubHomeUnlocked = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        for (int i = 0; i < this.teleportUnlockedList.length; i++) {
            buf.writeInt(teleportUnlockedList[i]);
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
