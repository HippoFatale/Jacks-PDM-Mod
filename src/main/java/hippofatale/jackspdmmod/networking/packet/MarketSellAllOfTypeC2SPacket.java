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

public class MarketSellAllOfTypeC2SPacket {
    private int marketItemIndex;

    public MarketSellAllOfTypeC2SPacket(int marketItemIndex) {
        this.marketItemIndex = marketItemIndex;
    }

    public MarketSellAllOfTypeC2SPacket(ByteBuf buf) {
        this.marketItemIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(marketItemIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                int totalEarned = 0;
                BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                if (account != null) {
                    Item sellingItem = MarketData.getItem(marketItemIndex);
                    String sellingItemName = MarketData.getItemName(marketItemIndex);
                    int packageQuantity = marketQuantities.get(sellingItemName);
                    int owningCount = 0;
                    for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                        if (player.inventory.getItem(i).sameItem(sellingItem.getDefaultInstance())) {
                            owningCount = owningCount + player.inventory.getItem(i).getCount();
                        }
                    }
                    int sellingPackages = owningCount / packageQuantity;
                    for (int i = 0; i < sellingPackages * packageQuantity; i++) {
                        for (int k = 0; k < player.inventory.getContainerSize(); k++) {
                            if (player.inventory.getItem(k).sameItem(sellingItem.getDefaultInstance())) {
                                player.inventory.getItem(k).shrink(1);
                                break;
                            }
                        }
                    }

                    totalEarned =  totalEarned + marketPrices.get(sellingItemName) * sellingPackages;
                    account.add(marketPrices.get(sellingItemName) * sellingPackages);

                    if (totalEarned > 0) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.sold_all_items_of_type", new StringTextComponent(Integer.toString(totalEarned)).withStyle(TextFormatting.YELLOW)), false);
                    } else {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_items"), false);
                    }
                }
            }
        });
        return true;
    }
}
