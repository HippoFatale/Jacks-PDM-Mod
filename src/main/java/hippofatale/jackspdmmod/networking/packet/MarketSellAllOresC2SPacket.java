package hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import hippofatale.jackspdmmod.market.MarketData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.text.StringTextComponent;
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
                int soldPackages = 0;
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
                    soldPackages = soldPackages + sellingPackages;

                    BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                    if (account != null) {
                        account.add(marketPrices.get(sellingItemName) * sellingPackages);
                    }
                }
                if (soldPackages > 0) {
                    player.displayClientMessage(new StringTextComponent("판매할 수 있는 모든 광물을 성공적으로 판매했습니다."), false);
                } else {
                    player.displayClientMessage(new StringTextComponent("광물이 없거나 부족합니다."), false);
                }
            }
        });
        return true;
    }
}
