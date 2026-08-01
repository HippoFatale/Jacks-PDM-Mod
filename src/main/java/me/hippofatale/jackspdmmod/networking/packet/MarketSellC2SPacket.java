package me.hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import io.netty.buffer.ByteBuf;
import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
            if (player != null) {
                BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                if (account != null) {
                    MarketItem marketItem = MarketManager.cropMarketItems.get(marketItemIndex);
                    Item sellingItem = marketItem.getItem();
                    int sellingQuantity = marketItem.getQuantity();

                    int owningCount = 0;
                    for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                        ItemStack itemStack = player.inventory.getItem(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() == sellingItem) {
                            owningCount = owningCount + itemStack.getCount();
                        }
                    }

                    if (owningCount >= sellingQuantity) {
                        int takenCount = 0;

                        for (int i = 0; i < player.inventory.getContainerSize() && takenCount < sellingQuantity; i++) {
                            ItemStack itemStack = player.inventory.getItem(i);

                            if (!itemStack.isEmpty() && itemStack.getItem() == sellingItem) {
                                int countInStack = itemStack.getCount();
                                int needToTake = sellingQuantity - takenCount;

                                if (countInStack <= needToTake) {
                                    takenCount += countInStack;
                                    player.inventory.setItem(i, ItemStack.EMPTY);
                                } else {
                                    itemStack.shrink(needToTake);
                                    takenCount += needToTake;
                                }
                            }
                        }

                        account.add(marketItem.getPrice());
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.sold_items"), false);
                    } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_items"), false);
                    }
                }
            }
        });
        return true;
    }
}
