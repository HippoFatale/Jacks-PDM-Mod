package hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import hippofatale.jackspdmmod.networking.ModMessages;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySlotMachineC2SPacket {
    private final int cost = 10000;
    private final int imageTypes = 4;
    private final int prizeMultiplier = 10;

    public PlaySlotMachineC2SPacket() {

    }

    public PlaySlotMachineC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            //points
            BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
            if (account != null) {
                if (account.hasBalance(cost)) {
                    account.take(cost);

                    //roll
                    int reel0 = (int) (Math.random() * imageTypes);
                    int reel1 = (int) (Math.random() * imageTypes);
                    int reel2 = (int) (Math.random() * imageTypes);

                    if (reel0 == reel1 && reel1 == reel2) {
                        account.add(cost * prizeMultiplier);
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.slot_machine_1st_place"), false);
                    } else {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.slot_machine_last_place"), false);
                    }
                    long balance = account.getBalance().longValue();
                    ModMessages.sendToPlayer(new SetSlotMachineScreenS2CPacket(reel0, reel1, reel2, false, balance), player);
                } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_pokedollars"), false);
                }
            }
        });
        return true;
    }
}
