package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.items.CurryDishItem;
import hippofatale.jackspdmmod.JacksPDMMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class ItemRestrictionEvents {
    //item ban
    private static boolean isBannedItem(Item item) {
        List<Item> bannedItemList = Arrays.asList(new Item[]{
                PixelmonItems.gold_bottle_cap,
                PixelmonItems.silver_bottle_cap
        });
        for (Item listedItem : bannedItemList) {
            if (item.equals(listedItem)) {
                return true;
            }
        }
        if (item instanceof CurryDishItem) {
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getPlayer().isCreative()) {
                return;
            }

            if (isBannedItem(event.getStack().getItem())) {
                for (int i = 0; i < event.getPlayer().inventory.getContainerSize(); i++) {
                    if (isBannedItem(event.getPlayer().inventory.getItem(i).getItem())) {
                        event.getPlayer().inventory.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerContainerOpen(PlayerContainerEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getPlayer().isCreative()) {
                return;
            }

            for (int i = 0; i < event.getContainer().slots.size(); i++) {
                if (isBannedItem(event.getContainer().getSlot(i).getItem().getItem())) {
                    event.getContainer().getSlot(i).set(ItemStack.EMPTY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUseItem(LivingEntityUseItemEvent event) {
        if (!event.getEntity().level.isClientSide()) {
            if (event.getEntity() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                if (isBannedItem(event.getItem().getItem())) {
                    event.setCanceled(true);
                    for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                        if (isBannedItem(player.inventory.getItem(i).getItem())) {
                            player.inventory.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBannedItemInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if (isBannedItem(player.getItemInHand(event.getHand()).getItem())) {
                event.setCanceled(true);
                for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                    if (isBannedItem(player.inventory.getItem(i).getItem())) {
                        player.inventory.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }


    //crafting ban
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            ItemStack craftedItem = event.getCrafting();
            int craftedAmount = craftedItem.getCount();
            int count = 0;
            for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                if (player.inventory.getItem(i).sameItem(craftedItem)) {
                    count = count + player.inventory.getItem(i).getCount();
                }
            }
            if (count >= craftedAmount) {
                for (int i = 0; i < craftedAmount; i++) {
                    for (int j = 0; j < player.inventory.getContainerSize(); j++) {
                        if (player.inventory.getItem(j).sameItem(craftedItem)) {
                            player.inventory.getItem(j).shrink(1);
                            break;
                        }
                    }
                }
            } else {
                player.inventory.setCarried(ItemStack.EMPTY);
            }

            player.refreshContainer(player.containerMenu);
            player.closeContainer();
            player.refreshContainer(player.containerMenu);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.crafting_is_forbidden").withStyle(TextFormatting.RED), false);
        }
    }
}
