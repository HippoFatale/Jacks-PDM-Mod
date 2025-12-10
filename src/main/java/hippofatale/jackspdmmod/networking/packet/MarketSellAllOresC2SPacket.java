package hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import hippofatale.jackspdmmod.market.MarketData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;
import static hippofatale.jackspdmmod.JacksPDMMod.marketQuantities;

public class MarketSellAllOresC2SPacket {

    public MarketSellAllOresC2SPacket() {

    }

    public MarketSellAllOresC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                int totalEarned = 0;
                BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                if (account != null) {
                    for (int i = 0; i < 7; i++) {
                        Item sellingItem = MarketData.getItem(i);
                        String sellingItemName = MarketData.getItemName(i);
                        int packageQuantity = marketQuantities.get(sellingItemName);
                        int owningCount = 0;
                        for (int j = 0; j < player.inventory.getContainerSize(); j++) {
                            if (player.inventory.getItem(j).sameItem(sellingItem.getDefaultInstance())) {
                                owningCount = owningCount + player.inventory.getItem(j).getCount();
                            }
                        }
                        int sellingPackages = owningCount / packageQuantity;
                        for (int j = 0; j < sellingPackages * packageQuantity; j++) {
                            for (int k = 0; k < player.inventory.getContainerSize(); k++) {
                                if (player.inventory.getItem(k).sameItem(sellingItem.getDefaultInstance())) {
                                    player.inventory.getItem(k).shrink(1);
                                    break;
                                }
                            }
                        }

                        totalEarned =  totalEarned + marketPrices.get(sellingItemName) * sellingPackages;
                        account.add(marketPrices.get(sellingItemName) * sellingPackages);
                    }

                    if (totalEarned > 0) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.sold_all_ores", new StringTextComponent(Integer.toString(totalEarned)).withStyle(TextFormatting.YELLOW)), false);
                    } else {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_ores"), false);
                    }
                }
            }
        });
        return true;
    }
}
