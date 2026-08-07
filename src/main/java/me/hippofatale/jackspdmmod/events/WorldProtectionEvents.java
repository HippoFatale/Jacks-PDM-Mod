package me.hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.block.ModBlocks;
import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.home.Home;
import me.hippofatale.jackspdmmod.home.HomeManager;
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
import java.util.Optional;
import java.util.UUID;

import static me.hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class WorldProtectionEvents {
    //protection
    private static boolean hasBreakPermission(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.isCreative()) {
            return true;
        }

        UUID playerUUID = player.getUUID();

        for (Home personalHome : HomeManager.personalHomes.values()) {
            if (personalHome.isInBounds(blockPos)) {
                if (personalHome.getOwningPlayerOrClub().equals(Optional.of(playerUUID))) {
                    return true;
                }

                if (personalHome.getSharedPlayers().contains(playerUUID)) {
                    return true;
                }

                return false;
            }
        }

        for (Home clubHome : HomeManager.clubHomes.values()) {
            if (clubHome.isInBounds(blockPos)) {
                Club playerClub = ClubManager.belongingClubs.get(playerUUID);

                if (playerClub != null && clubHome.getOwningPlayerOrClub().equals(Optional.of(playerClub))) {
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

        UUID playerUUID = player.getUUID();

        for (Home personalHome : HomeManager.personalHomes.values()) {
            if (personalHome.isInBounds(blockPos)) {
                if (personalHome.getOwningPlayerOrClub().equals(Optional.of(playerUUID))) {
                    return true;
                }

                if (personalHome.getSharedPlayers().contains(playerUUID)) {
                    return true;
                }

                return false;
            }
        }

        for (Home clubHome : HomeManager.clubHomes.values()) {
            if (clubHome.isInBounds(blockPos)) {
                Club playerClub = ClubManager.belongingClubs.get(playerUUID);

                if (playerClub != null && clubHome.getOwningPlayerOrClub().equals(Optional.of(playerClub))) {
                    return true;
                }

                return false; // 다른 동아리방 상자는 오픈 차단
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

            if (event.getWorld().getBlockState(blockPos).getBlock().is(ModBlocks.PDM_ORE.get()) && isInMine(blockPos)) {
                return;
            }

            event.setCanceled(!hasBreakPermission(player, blockPos));
            player.refreshContainer(player.containerMenu);
        }
    }


    public static boolean isInMine(BlockPos blockPos) {
        int minX = -899;
        int maxX = -722;
        int minZ = 2002;
        int maxZ = 2147;

        if (blockPos.getX() < minX) {
            return false;
        }
        if (blockPos.getX() > maxX) {
            return false;
        }
        if (blockPos.getZ() < minZ) {
            return false;
        }
        if (blockPos.getZ() > maxZ) {
            return false;
        }

        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            BlockPos blockPos = event.getPos();

            if (event.getWorld().getBlockState(blockPos).getBlock().is(ModBlocks.PDM_ORE.get()) && isInMine(blockPos)) {
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
            } else {
                event.setCanceled(true);
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
