package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.title.PlayerTitleProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TitleDisplayC2SPacket {
    private int titleIndex;

    public TitleDisplayC2SPacket(int titleIndex) {
        this.titleIndex = titleIndex;
    }

    public TitleDisplayC2SPacket(ByteBuf buf) {
        this.titleIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(titleIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                playerTitle.setDisplayingTitleIndex(titleIndex);
                ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), player);
                ModMessages.sendToAll(new PlayerTitleDataSyncS2CPacket(player.getUUID(), playerTitle.getDisplayingTitleIndex()));
            });
            player.refreshDisplayName();
        });
        return true;
    }
}
