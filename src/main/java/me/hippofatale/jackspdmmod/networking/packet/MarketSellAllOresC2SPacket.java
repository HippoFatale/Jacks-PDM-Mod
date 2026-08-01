package me.hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import io.netty.buffer.ByteBuf;
import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
                    for (int i = 0; i < MarketManager.oreMarketItems.size(); i++) {
                        MarketItem marketItem = MarketManager.oreMarketItems.get(i);
                        Item sellingItem = marketItem.getItem();
                        int packageQuantity = marketItem.getQuantity();
                        int owningCount = 0;

                        for (int j = 0; j < player.inventory.getContainerSize(); j++) {
                            ItemStack itemStack = player.inventory.getItem(j);
                            if (!itemStack.isEmpty() && itemStack.getItem() == sellingItem) {
                                owningCount = owningCount + itemStack.getCount();
                            }
                        }

                        int sellingPackages = owningCount / packageQuantity;

                        if (sellingPackages > 0) {
                            int totalItemsToTake = sellingPackages * packageQuantity;
                            int takenCount = 0;

                            for (int k = 0; k < player.inventory.getContainerSize() && takenCount < totalItemsToTake; k++) {
                                ItemStack itemStack = player.inventory.getItem(k);

                                if (!itemStack.isEmpty() && itemStack.getItem() == sellingItem) {
                                    int countInStack = itemStack.getCount();
                                    int needToTake = totalItemsToTake - takenCount;

                                    if (countInStack <= needToTake) {
                                        takenCount += countInStack;
                                        player.inventory.setItem(k, ItemStack.EMPTY);
                                    } else {
                                        itemStack.shrink(needToTake);
                                        takenCount += needToTake;
                                    }
                                }
                            }

                            totalEarned = totalEarned + marketItem.getPrice() * sellingPackages;
                        }
                    }

                    if (totalEarned > 0) {
                        account.add(totalEarned);
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
