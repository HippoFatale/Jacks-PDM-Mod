package hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import hippofatale.jackspdmmod.market.MarketData;
import hippofatale.jackspdmmod.teleport.TeleportData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;
import static hippofatale.jackspdmmod.JacksPDMMod.marketQuantities;

public class MarketSellC2SPacket {
    private int marketItemIndex;

    public MarketSellC2SPacket(int marketItemIndex) {
        this.marketItemIndex = marketItemIndex;
    }

    public MarketSellC2SPacket(ByteBuf buf) {
        this.marketItemIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(marketItemIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            Item sellingItem = MarketData.getItem(marketItemIndex);
            String sellingItemName = MarketData.getItemName(marketItemIndex);
            int sellingQuantity = marketQuantities.get(sellingItemName);
            if (player != null) {
                int count = 0;
                for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                    if (player.inventory.getItem(i).sameItem(sellingItem.getDefaultInstance())) {
                        count = count + player.inventory.getItem(i).getCount();
                    }
                }
                if (count >= sellingQuantity) {
                    for (int i = 0; i < sellingQuantity; i++) {
                        for (int j = 0; j < player.inventory.getContainerSize(); j++) {
                            if (player.inventory.getItem(j).sameItem(sellingItem.getDefaultInstance())) {
                                player.inventory.getItem(j).shrink(1);
                                break;
                            }
                        }
                    }

                    BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                    if (account != null) {
                        account.add(marketPrices.get(sellingItemName));
                        player.displayClientMessage(new StringTextComponent("아이템을 성공적으로 판매했습니다."), false);
                    }
                } else {
                    player.displayClientMessage(new StringTextComponent("아이템이 없거나 부족합니다."), false);
                }
            }
        });
        return true;
    }
}
