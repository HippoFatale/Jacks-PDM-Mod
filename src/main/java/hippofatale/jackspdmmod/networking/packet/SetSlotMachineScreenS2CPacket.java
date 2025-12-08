package hippofatale.jackspdmmod.networking.packet;

import hippofatale.jackspdmmod.client.ClientHooks;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetSlotMachineScreenS2CPacket {
    private int reel0;
    private int reel1;
    private int reel2;
    private boolean spinReel;

    public SetSlotMachineScreenS2CPacket(int reel0, int reel1, int reel2, boolean spinReel) {
        this.reel0 = reel0;
        this.reel1 = reel1;
        this.reel2 = reel2;
        this.spinReel = spinReel;
    }

    public SetSlotMachineScreenS2CPacket(ByteBuf buf) {
        this.reel0 = buf.readInt();
        this.reel1 = buf.readInt();
        this.reel2 = buf.readInt();
        this.spinReel = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(reel0);
        buf.writeInt(reel1);
        buf.writeInt(reel2);
        buf.writeBoolean(spinReel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHooks.openSlotMachineScreen(reel0, reel1, reel2, spinReel);
        });
        return true;
    }
}
