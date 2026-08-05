package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.client.ClientTitleData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerTitleDataSyncS2CPacket {
    private final UUID playerUUID;
    private final int titleIndex;

    public PlayerTitleDataSyncS2CPacket(UUID playerUUID, int titleIndex) {
        this.playerUUID = playerUUID;
        this.titleIndex = titleIndex;
    }

    public PlayerTitleDataSyncS2CPacket(ByteBuf buf) {
        this.playerUUID = new UUID(buf.readLong(), buf.readLong());
        this.titleIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
        buf.writeInt(titleIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientTitleData.setPlayerTitleIndex(playerUUID, titleIndex);
        });
        return true;
    }
}
