package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientRankPointData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RankPointDataSyncS2CPacket {
    private UUID playerUUID;
    private int rankPoints;

    public RankPointDataSyncS2CPacket(UUID playerUUID, int rankPoints) {
        this.playerUUID = playerUUID;
        this.rankPoints = rankPoints;
    }

    public RankPointDataSyncS2CPacket(ByteBuf buf) {
        this.playerUUID = new UUID(buf.readLong(), buf.readLong());
        this.rankPoints = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
        buf.writeInt(rankPoints);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientRankPointData.setRankPoints(playerUUID, rankPoints);
        });
        return true;
    }
}
