package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.home.Home;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class WorldProtectionEvents {
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
