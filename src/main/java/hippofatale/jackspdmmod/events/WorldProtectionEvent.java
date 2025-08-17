package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import com.pixelmonmod.pixelmon.items.CurryDishItem;
import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.club.Club;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.home.Home;
import hippofatale.jackspdmmod.home.HomeData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class WorldProtectionEvent {
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

    //protection
    private static boolean hasBreakPermission(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.isCreative()) {
            return true;
        }
        for (Map.Entry<UUID, Home> entry : personalHomes.entrySet()) {
            if (entry.getValue().isInBounds(blockPos)) {
                if (entry.getValue().getOwner().equals(player.getUUID())) {
                    return true;
                }
                if (entry.getValue().getSharedPlayers().containsKey(player.getUUID())) {
                    return true;
                }
                return false;
            }
        }
        for (Map.Entry<String, Home> entry : clubNameHomes.entrySet()) {
            if (entry.getValue().isInBounds(blockPos)) {
                if (entry.getKey().equals(playerClubs.get(player.getUUID()))) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean hasInteractionPermission(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.isCreative()) {
            return true;
        }
        for (Map.Entry<UUID, Home> entry : personalHomes.entrySet()) {
            if (entry.getValue().isInBounds(blockPos)) {
                if (entry.getValue().getOwner().equals(player.getUUID())) {
                    return true;
                }
                if (entry.getValue().getSharedPlayers().containsKey(player.getUUID())) {
                    return true;
                }
                return false;
            }
        }
        for (Map.Entry<String, Home> entry : clubNameHomes.entrySet()) {
            if (entry.getValue().isInBounds(blockPos)) {
                if (entry.getKey().equals(playerClubs.get(player.getUUID()))) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    //block
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            event.setCanceled(!hasInteractionPermission(player, blockPos));
            player.refreshContainer(player.containerMenu);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            if (event.getWorld().getBlockState(blockPos).getBlock().is(Tags.Blocks.ORES)) {
                return;
            }

            event.setCanceled(!hasBreakPermission(player, blockPos));
            player.refreshContainer(player.containerMenu);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            if (event.getWorld().getBlockState(blockPos).getBlock().is(Tags.Blocks.ORES)) {
                return;
            }

            event.setCanceled(!hasBreakPermission(player, blockPos));
            player.refreshContainer(player.containerMenu);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                BlockPos blockPos = event.getPos();

                event.setCanceled(!hasBreakPermission(player, blockPos));
                player.refreshContainer(player.containerMenu);

            }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlaceMultiBlock(BlockEvent.EntityMultiPlaceEvent event) {
        if (!event.getWorld().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                BlockPos blockPos = event.getPos();

                event.setCanceled(!hasBreakPermission(player, blockPos));
                player.refreshContainer(player.containerMenu);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!event.getWorld().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                BlockPos blockPos = event.getPos();

                event.setCanceled(!hasBreakPermission(player, blockPos));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBlockToolInteract(BlockEvent.BlockToolInteractEvent event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            event.setCanceled(!hasBreakPermission(player, blockPos));
        }
    }

    //entity
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            event.setCanceled(!hasInteractionPermission(player, blockPos));
            player.refreshContainer(player.containerMenu);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Entity entity = event.getTarget();
            BlockPos blockPos = entity.blockPosition();

            if (entity instanceof BikeEntity) {
                return;
            }

            event.setCanceled(!hasBreakPermission(player, blockPos));
        }
    }

    //etc
    @SubscribeEvent
    public static void onFillBucket(FillBucketEvent event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if (event.getTarget() != null) {
                BlockPos blockPos = new BlockPos(event.getTarget().getLocation());

                event.setCanceled(!hasBreakPermission(player, blockPos));
                player.refreshContainer(player.containerMenu);
            }
        }
    }
}
