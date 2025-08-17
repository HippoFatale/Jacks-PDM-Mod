package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientHooks;
import hippofatale.jackspdmmod.screen.PlacardScreen;
import hippofatale.jackspdmmod.tileentity.PlacardTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.math.BigDecimal;
import java.util.function.Supplier;

public class SetPlacardScreenS2CPacket {
    PlacardTile placard;

    boolean purchased;
    int plotTypeIndex;
    int plotSizeIndex;
    long plotPrice;
    static BlockPos placardPos;

    public SetPlacardScreenS2CPacket(PlacardTile placard) {
        this.placard = placard;
        purchased = placard.getPurchased();
        switch (placard.getPlotType()) {
            case PERSONAL: {
                plotTypeIndex = 1;
                break;
            }
            case CLUB: {
                plotTypeIndex = 2;
                break;
            }
        }
        switch (placard.getPlotSize()) {
            case SMALL: {
                plotSizeIndex = 1;
                break;
            }
            case MEDIUM: {
                plotSizeIndex = 2;
                break;
            }
            case LARGE: {
                plotSizeIndex = 3;
                break;
            }
        }
        plotPrice = placard.getPlotPrice().longValueExact();
        placardPos = placard.getBlockPos();
    }

    public SetPlacardScreenS2CPacket(ByteBuf buf) {
        this.purchased = buf.readBoolean();
        this.plotTypeIndex = buf.readInt();
        this.plotSizeIndex = buf.readInt();
        this.plotPrice = buf.readLong();
        placardPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(purchased);
        buf.writeInt(plotTypeIndex);
        buf.writeInt(plotSizeIndex);
        buf.writeLong(plotPrice);
        buf.writeInt(placardPos.getX());
        buf.writeInt(placardPos.getY());
        buf.writeInt(placardPos.getZ());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHooks.openPlacardScreen(purchased, plotTypeIndex, plotSizeIndex, plotPrice, placardPos);
        });
        return true;
    }
}
