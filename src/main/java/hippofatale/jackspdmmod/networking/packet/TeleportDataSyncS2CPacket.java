package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientTeleportData;
import hippofatale.jackspdmmod.client.ClientTitleData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportDataSyncS2CPacket {
//    private int[] townUnlockedList = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] townUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private boolean homeUnlocked = false;
    private boolean clubHomeUnlocked = false;

    public TeleportDataSyncS2CPacket(int[] townUnlockedList, boolean homeUnlocked, boolean clubHomeUnlocked) {
        for (int i = 0; i < this.townUnlockedList.length; i++) {
            this.townUnlockedList[i] = townUnlockedList[i];
        }
        this.homeUnlocked = homeUnlocked;
        this.clubHomeUnlocked = clubHomeUnlocked;
    }

    public TeleportDataSyncS2CPacket(ByteBuf buf) {
        for (int i = 0; i < this.townUnlockedList.length; i++) {
            this.townUnlockedList[i] = buf.readInt();
        }
        this.homeUnlocked = buf.readBoolean();
        this.clubHomeUnlocked = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        for (int i = 0; i < this.townUnlockedList.length; i++) {
            buf.writeInt(townUnlockedList[i]);
        }
        buf.writeBoolean(homeUnlocked);
        buf.writeBoolean(clubHomeUnlocked);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientTeleportData.setTownUnlockedList(townUnlockedList);
            ClientTeleportData.setHomeUnlocked(homeUnlocked);
            ClientTeleportData.setClubHomeUnlocked(clubHomeUnlocked);
        });
        return true;
    }
}
