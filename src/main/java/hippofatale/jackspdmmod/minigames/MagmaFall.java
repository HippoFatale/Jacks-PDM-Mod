package hippofatale.jackspdmmod.minigames;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import hippofatale.jackspdmmod.events.MiniGameRunEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class MagmaFall {
    public static final AxisAlignedBB field = new AxisAlignedBB(-1573, 39, 83, -1556, 51, 100);
    private static final AxisAlignedBB platform = new AxisAlignedBB(-1573, 38, 83, -1556, 39, 100);
//    private static final Vector3d returnPoint = new Vector3d(-93, 44, -8);
    private static final int platformSize = 8;
    private static final BlockPos platformMin = new BlockPos(platform.minX, platform.minY, platform.minZ);
    private static final BlockPos platformMax = new BlockPos(platform.maxX, platform.maxY, platform.maxZ);
    private static final List<Block> magmaFallBlocks = Arrays.asList(new Block[]{
            Blocks.RED_WOOL,
            Blocks.ORANGE_WOOL,
            Blocks.YELLOW_WOOL,
            Blocks.GREEN_WOOL,
            Blocks.LIGHT_BLUE_WOOL,
            Blocks.BLUE_WOOL,
            Blocks.PURPLE_WOOL,
            Blocks.PINK_WOOL,
    });
    private static final List<ServerPlayerEntity> survivors = new ArrayList<>();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> future;
    private static int gameTime = 0;
    private static MinecraftServer server;
    private static World world;


    public static void prepareMagmaFall(MinecraftServer server) {
        if (server ==  null) {
            return;
        }
        MagmaFall.server = server;
        world = server.overworld();

        //init map
        List<Integer> colorPattern = new ArrayList<>();
        for (int i = 0; i < platformSize * platformSize; i++) {
            colorPattern.add(i);
        }

        for (int i = 0; i <= platformSize * 2; i++ ) {
            for (int j = 0; j <= platformSize * 2; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    world.setBlock(platformMin.offset(i, 0, j), magmaFallBlocks.get((colorPattern.remove((int) (Math.random() * colorPattern.size()))) % magmaFallBlocks.size()).defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                } else {
                    world.setBlock(platformMin.offset(i, 0, j), Blocks.GLASS.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                }
            }
        }

    }

    public static void beginMagmaFall() {
        if (server == null) {
            return;
        }

        //teleport applicants to field
        for (UUID playerUUID : miniGameApplicants) {
            ServerPlayerEntity player = (ServerPlayerEntity) server.overworld().getPlayerByUUID(playerUUID);
            if (player != null && BattleRegistry.getBattle(player) == null) {
                player.teleportTo(
                        (platform.minX + 0.5) + (platform.getXsize() - 1) * Math.random(),
                        platform.maxY + 1,
                        (platform.minZ + 0.5) + (platform.getZsize() - 1) * Math.random());
            }
        }

        //run magma fall
        gameTime = 0;
        future = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (server != null) {
                    server.execute(() -> {
                        switch (gameTime % 20) {
                            case 0:
                                checkMagmaFall();
                                break;
                            case 5:
                                countdown(5);
                                break;
                            case 6:
                                countdown(4);
                                break;
                            case 7:
                                countdown(3);
                                break;
                            case 8:
                                countdown(2);
                                break;
                            case 9:
                                countdown(1);
                                break;
                            case 10:
                                removeGlass();
                                break;
                            case 15:
                                removeBlock();
                                break;
                        }
                        gameTime++;
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 5, 1, TimeUnit.SECONDS);
    }

//    private static void magmaFallEvents(int phaseTime, Runnable phase) {
//        scheduler.schedule(() -> {
//            try {
//                if (server != null) {
//                    server.execute(() -> {
//                        if (!isMiniGameRunning) {
//                            return;
//                        }
//                        phase.run();
//                    });
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }, phaseTime, TimeUnit.SECONDS);
//    }

    private static void checkMagmaFall() {
        //count survivors
        survivors.clear();
        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            if ((player.getBoundingBox().intersects(field) && player.gameMode.isSurvival())) { //TODO LIVE
//            if ((player.getBoundingBox().intersects(field))) { //TODO TEST
                survivors.add(player);
            }
        }

        //count blocks
        long remainingBlocks = 0;
        Stream<BlockState> platformBlockStates = world.getBlockStates(platform);
        remainingBlocks = platformBlockStates.filter(blockState -> blockState.is(BlockTags.WOOL)).count();

        //check end
        if (survivors.size() <= 1 || (int) remainingBlocks <= 1) { //TODO LIVE
//        if ((int) remainingBlocks <= 1) { //TODO TEST
            endMagmaDrop();
        } else {
            //recolor
            Stream<BlockState> remainingBlockStates = world.getBlockStates(platform);
            Set<BlockState> remainingBlockTypes = remainingBlockStates.filter(blockState -> blockState.is(BlockTags.WOOL)).collect(Collectors.toSet());
            if (remainingBlockTypes.size() == 1) {
                List<Integer> recolorPattern = new ArrayList<>();
                for (int i = 0; i < platformSize; i++) {
                    recolorPattern.add(i);
                }

                for (int i = 0; i <= platformSize * 2; i++ ) {
                    for (int j = 0; j <= platformSize * 2; j++) {
                        if (!world.getBlockState(platformMin.offset(i, 0, j)).equals(Blocks.AIR.defaultBlockState())) {
                            world.setBlock(platformMin.offset(i, 0, j), magmaFallBlocks.get(recolorPattern.remove((int) (Math.random() * recolorPattern.size()))).defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                        }
                    }
                }
            }

            fillGlass();

            //announce number of survivors
            for (ServerPlayerEntity player : survivors) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_survivors",
                        new StringTextComponent(Integer.toString(survivors.size())).withStyle(TextFormatting.AQUA)), false);
            }

            countdown(10);
        }
    }

    private static void countdown(int countdown) {
        if (!isMiniGameRunning) {
            return;
        }
        for (ServerPlayerEntity player : survivors) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown",
                    new StringTextComponent(Integer.toString(countdown)).withStyle(TextFormatting.RED)), false);
        }
    }

    private static void fillGlass() {
        int y = platformMin.getY();
        for (int x = platformMin.getX(); x < platformMax.getX(); x++) {
            for (int z = platformMin.getZ(); z < platformMax.getZ(); z++) {
                BlockPos blockPos = new BlockPos(x, y, z);
                if (world.getBlockState(blockPos).is(Blocks.AIR)) {
                    world.setBlock(blockPos, Blocks.GLASS.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        }
    }

    private static void removeGlass() {
        if (!isMiniGameRunning) {
            return;
        }
        int y = platformMin.getY();
        for (int x = platformMin.getX(); x <= platformMax.getX(); x++) {
            for (int z = platformMin.getZ(); z <= platformMax.getZ(); z++) {
                BlockPos blockPos = new BlockPos(x, y, z);
                if (world.getBlockState(blockPos).is(Blocks.GLASS)) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        }
    }

    private static void removeBlock() {
        if (!isMiniGameRunning) {
            return;
        }
        Stream<BlockState> platformBlockStates = world.getBlockStates(platform);
        BlockState removingBlock = platformBlockStates.filter(blockState -> blockState.is(BlockTags.WOOL)).findAny().orElse(Blocks.AIR.defaultBlockState());

        int y = platformMin.getY();
        for (int x = platformMin.getX(); x < platformMax.getX(); x++) {
            for (int z = platformMin.getZ(); z < platformMax.getZ(); z++) {
                BlockPos blockPos = new BlockPos(x, y, z);
                if (world.getBlockState(blockPos).is(removingBlock.getBlock())) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        }

        for (ServerPlayerEntity player : survivors) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_remove_block", getMagmaFallBlockText(removingBlock.getBlock())), false);
        }
    }

    private static void endMagmaDrop() {
        //give prize to survivors
        for (ServerPlayerEntity player : survivors) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_winner"), false);
            player.inventory.add(new ItemStack(Items.NETHER_STAR, 1));
            player.teleportTo(
                    MiniGameRunEvents.returnPoint.x,
                    MiniGameRunEvents.returnPoint.y,
                    MiniGameRunEvents.returnPoint.z);
        }
        isMiniGameRunning = false;
        future.cancel(false);
        future = null;
//        scheduler.shutdown();
    }

    private static ITextComponent getMagmaFallBlockText(Block block) {
        if (block.is(Blocks.RED_WOOL)) {
            return new StringTextComponent("빨간색").withStyle(TextFormatting.RED);
        }
        if (block.is(Blocks.ORANGE_WOOL)) {
            return new StringTextComponent("주황색").withStyle(TextFormatting.GOLD);
        }
        if (block.is(Blocks.YELLOW_WOOL)) {
            return new StringTextComponent("노란색").withStyle(TextFormatting.YELLOW);
        }
        if (block.is(Blocks.GREEN_WOOL)) {
            return new StringTextComponent("초록색").withStyle(TextFormatting.DARK_GREEN);
        }
        if (block.is(Blocks.LIGHT_BLUE_WOOL)) {
            return new StringTextComponent("하늘색").withStyle(TextFormatting.AQUA);
        }
        if (block.is(Blocks.BLUE_WOOL)) {
            return new StringTextComponent("파란색").withStyle(TextFormatting.BLUE);
        }
        if (block.is(Blocks.PURPLE_WOOL)) {
            return new StringTextComponent("보라색").withStyle(TextFormatting.DARK_PURPLE);
        }
        if (block.is(Blocks.PINK_WOOL)) {
            return new StringTextComponent("분홍색").withStyle(TextFormatting.LIGHT_PURPLE);
        }

        return new StringTextComponent("");
    }
}
