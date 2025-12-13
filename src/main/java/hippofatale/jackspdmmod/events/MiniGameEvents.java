package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import hippofatale.jackspdmmod.util.MiniGameType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class MiniGameEvents {
    private static final Vector3d returnPoint =
        new Vector3d(-93, 44, -8); //to town hall(spawn)
//        new Vector3d(-382, 119, -1918); //to mini-game arcade lobby
    private static LocalDateTime startTime;

    @SubscribeEvent
    public static void onMiniGameAnnounce(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isClientSide) {
            return;
        }

        if (isMiniGameOpen) {
            return;
        }

        //timer
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (currentTime.getHour() % 3 == 2 && currentTime.getMinute() == 55) {
            //enable join command
            isMiniGameOpen = true;

            //end prev mini-game
            isMiniGameRunning = false;

            //set start time
            startTime = currentTime.plusMinutes(5);

            //set mini-game value
            MiniGameType prevMiniGame = miniGameType;
            switch (prevMiniGame) {
                case MAGMA_FALL:
                    miniGameType = MiniGameType.JUMP_MAP_RACE;
                    break;
                case JUMP_MAP_RACE:
                    miniGameType = MiniGameType.DICE_OF_FORTUNE;
                    break;
                case DICE_OF_FORTUNE:
                    miniGameType = MiniGameType.MAGMA_FALL;
                    break;
                default:
                    miniGameType = MiniGameType.MAGMA_FALL;
                    break;
            }

            //init mini-game
            switch (miniGameType) {
                case MAGMA_FALL:
                    initMagmaFall(world);
                    break;
                case JUMP_MAP_RACE:
                    break;
                case DICE_OF_FORTUNE:
                    diceNumbers.clear();
                    for (int i = 0; i < 100; i++) {
                        diceNumbers.add(i + 1);
                    }
                    break;
                default:
            }

            miniGameApplicants.clear();
            //announce mini-game join begin
            List<ServerPlayerEntity> players = world.getServer().getPlayerList().getPlayers();
            for (ServerPlayerEntity player : players) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_announce", startTime.getHour(), startTime.getMinute() ,miniGameType.getName()), false);
            }
            return;
        }
    }

    @SubscribeEvent
    public static void onMiniGameBegin(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isClientSide) {
            return;
        }

        if (startTime == null) {
            return;
        }

        //timer
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (currentTime.isAfter(startTime) && isMiniGameOpen) {
            //disable join command
            isMiniGameOpen = false;

            //announce mini-game join end
            List<ServerPlayerEntity> players = world.getServer().getPlayerList().getPlayers();
            for (ServerPlayerEntity player : players) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_started", miniGameType.getName()), false);
            }

            //start mini-game
            switch (miniGameType) {
                case MAGMA_FALL:
                    isMiniGameRunning = true;

                    //teleport applicants to map
                    for (UUID playerUUID : miniGameApplicants) {
                        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUUID(playerUUID);
                        if (player != null && BattleRegistry.getBattle(player) == null) {
                            player.moveTo(magmaFallFieldPivot.getX() + Math.random() * magmaFallFieldSize * 2 + 0.5,
                                    magmaFallFieldPivot.getY() + 1,
                                    magmaFallFieldPivot.getZ() + Math.random() * magmaFallFieldSize * 2 + 0.5);
                        }
                    }

                    break;

                case JUMP_MAP_RACE:
                    isMiniGameRunning = true;

                    //teleport applicants to map
                    for (UUID playerUUID : miniGameApplicants) {
                        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUUID(playerUUID);
                        if (player != null && BattleRegistry.getBattle(player) == null) {
//                            player.moveTo(-559, 85, -1870);
                            player.moveTo(-570 -4 + Math.random() * 8,
                                    85,
                                    -1884 -4 + Math.random() * 8);
                        }
                    }

                    break;

                case DICE_OF_FORTUNE:
                    diceOfFortuneResult(world);
                    break;
                default:
            }
        }
    }

    //magma fall
    private static final int magmaFallFieldSize = 8;
    private static final BlockPos magmaFallFieldPivot = new BlockPos(-527 - magmaFallFieldSize, 103, -1894 - magmaFallFieldSize);
    private static List<Integer> magmaFallRemoveIndex = new ArrayList<>();
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
    private static final List<ITextComponent> magmaFallNames = Arrays.asList(new ITextComponent[]{
            new StringTextComponent("빨간색").withStyle(TextFormatting.RED),
            new StringTextComponent("주황색").withStyle(TextFormatting.GOLD),
            new StringTextComponent("노란색").withStyle(TextFormatting.YELLOW),
            new StringTextComponent("초록색").withStyle(TextFormatting.DARK_GREEN),
            new StringTextComponent("하늘색").withStyle(TextFormatting.AQUA),
            new StringTextComponent("파란색").withStyle(TextFormatting.BLUE),
            new StringTextComponent("보라색").withStyle(TextFormatting.DARK_PURPLE),
            new StringTextComponent("분홍색").withStyle(TextFormatting.LIGHT_PURPLE)
    });

    public static boolean isPlayerOnMagmaFallField(ServerPlayerEntity player) {
        int playerX = player.blockPosition().getX();
        int playerY = player.blockPosition().getY();
        int playerZ = player.blockPosition().getZ();

        if (playerX < magmaFallFieldPivot.getX()) {
            return false;
        }
        if (playerX > magmaFallFieldPivot.getX() + magmaFallFieldSize * 2) {
            return false;
        }
        if (playerZ < magmaFallFieldPivot.getZ()) {
            return false;
        }
        if (playerZ > magmaFallFieldPivot.getZ() + magmaFallFieldSize * 2) {
            return false;
        }
        if (playerY < magmaFallFieldPivot.getY()) {
            return false;
        }

        return true;
    }

    public static void initMagmaFall(World world) {
        magmaFallSequence = 0;
        magmaFallSequenceTime = startTime.plusSeconds(15);
        magmaFallRemoveIndex.clear();
        for (int i = 0; i < magmaFallBlocks.size(); i++) {
            magmaFallRemoveIndex.add(i);
        }

        List<Integer> colorPattern = new ArrayList<>();
        for (int i = 0; i < magmaFallFieldSize * magmaFallFieldSize; i++) {
            colorPattern.add(i);
        }

        for (int i = 0; i <= magmaFallFieldSize * 2; i++ ) {
            for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    world.setBlock(magmaFallFieldPivot.offset(i, 0, j), magmaFallBlocks.get((colorPattern.remove((int) (Math.random() * colorPattern.size()))) % magmaFallBlocks.size()).defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                } else {
                    world.setBlock(magmaFallFieldPivot.offset(i, 0, j), Blocks.GLASS.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                }
            }
        }
    }

    private static int magmaFallSequence = 0;
    private static LocalDateTime magmaFallSequenceTime;
    @SubscribeEvent
    public static void onMagmaFall(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isClientSide) {
            return;
        }
        if (!(miniGameType == MiniGameType.MAGMA_FALL && isMiniGameRunning)) {
            return;
        }

        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (magmaFallSequenceTime == null || currentTime.isBefore(magmaFallSequenceTime)) {
            return;
        }

        switch (magmaFallSequence) {
            case 0:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(5);

                //count survivors
                List<ServerPlayerEntity> survivingPlayers = new ArrayList<>();
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player) && player.gameMode.isSurvival()) {
                        survivingPlayers.add(player);
                    }
                }

                //count blocks
                int remainingBlocks = 0;
                for (int i = 0; i <= magmaFallFieldSize * 2; i++) {
                    for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                        if (!world.getBlockState(magmaFallFieldPivot.offset(i, 0, j)).equals(Blocks.AIR.defaultBlockState())) {
                            remainingBlocks++;
                        }
                    }
                }

                if (survivingPlayers.size() <= 1 || remainingBlocks <= 1) {
                    //give prize
                    for (ServerPlayerEntity player : survivingPlayers) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_winner"), false);
                        player.inventory.add(new ItemStack(Items.NETHER_STAR, 1));
                        player.moveTo(returnPoint);
                    }
                    isMiniGameRunning = false;
                } else {
                    //recolor
                    if (magmaFallRemoveIndex.size() == 1) {
                        magmaFallRemoveIndex.clear();
                        for (int i = 0; i < magmaFallBlocks.size(); i++) {
                            magmaFallRemoveIndex.add(i);
                        }

                        for (int i = 0; i <= magmaFallFieldSize * 2; i++ ) {
                            for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                                if (!world.getBlockState(magmaFallFieldPivot.offset(i, 0, j)).equals(Blocks.AIR.defaultBlockState())) {
                                    world.setBlock(magmaFallFieldPivot.offset(i, 0, j), magmaFallBlocks.get(magmaFallRemoveIndex.remove((int) (Math.random() * magmaFallRemoveIndex.size()))).defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                                }
                            }
                        }

                        for (int i = 0; i < magmaFallBlocks.size(); i++) {
                            magmaFallRemoveIndex.add(i);
                        }
                    }

                    //fill glass
                    for (int i = 0; i <= magmaFallFieldSize * 2; i++ ) {
                        for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                            if (world.getBlockState(magmaFallFieldPivot.offset(i, 0, j)).equals(Blocks.AIR.defaultBlockState())) {
                                world.setBlock(magmaFallFieldPivot.offset(i, 0, j), Blocks.GLASS.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                        }
                    }

                    //announce survivors
                    for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                        if (isPlayerOnMagmaFallField(player)) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_number_of_survivors", new StringTextComponent(Integer.toString(survivingPlayers.size())).withStyle(TextFormatting.AQUA)), false);
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("10").withStyle(TextFormatting.YELLOW)), false);
                        }
                    }

                    magmaFallSequence++;
                }

                break;

            case 1:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(1);

                //countdown 5
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("5").withStyle(TextFormatting.RED)), false);
                    }
                }

                magmaFallSequence++;
                break;

            case 2:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(1);

                //countdown 4
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("4").withStyle(TextFormatting.RED)), false);
                    }
                }

                magmaFallSequence++;
                break;

            case 3:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(1);

                //countdown 3
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("3").withStyle(TextFormatting.DARK_RED)), false);
                    }
                }

                magmaFallSequence++;
                break;

            case 4:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(1);

                //countdown 2
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("2").withStyle(TextFormatting.DARK_RED)), false);
                    }
                }

                magmaFallSequence++;
                break;

            case 5:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(1);

                //countdown 1
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_fall_countdown", new StringTextComponent("1").withStyle(TextFormatting.DARK_RED)), false);
                    }
                }

                magmaFallSequence++;
                break;

            case 6:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(5);

                //remove glass
                for (int i = 0; i <= magmaFallFieldSize * 2; i++ ) {
                    for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                        if (world.getBlockState(magmaFallFieldPivot.offset(i, 0, j)).equals(Blocks.GLASS.defaultBlockState())) {
                            world.setBlock(magmaFallFieldPivot.offset(i, 0, j), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                        }
                    }
                }

                magmaFallSequence++;
                break;

            case 7:
                magmaFallSequenceTime = magmaFallSequenceTime.plusSeconds(5);

                //remove block
                int selectedBlockIndex = magmaFallRemoveIndex.remove((int) (Math.random() * magmaFallRemoveIndex.size()));
                for (int i = 0; i <= magmaFallFieldSize * 2; i++ ) {
                    for (int j = 0; j <= magmaFallFieldSize * 2; j++) {
                        if (world.getBlockState(magmaFallFieldPivot.offset(i, 0, j)).equals(magmaFallBlocks.get(selectedBlockIndex).defaultBlockState())) {
                            world.setBlock(magmaFallFieldPivot.offset(i, 0, j), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                        }
                    }
                }
                for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
                    if (isPlayerOnMagmaFallField(player)) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.magma_remove_block", magmaFallNames.get(selectedBlockIndex)), false);
                    }
                }

                magmaFallSequence = 0;
                break;
        }
    }

    //jump map race
    private static final double jumpMapRaceFailY = 84.5;
    public static boolean isInJumpMapRaceGoal(ServerPlayerEntity player) {
        int playerX = player.blockPosition().getX();
        int playerZ = player.blockPosition().getZ();
        int playerY = player.blockPosition().getY();

        int minX = -560;
        int maxX = -559;
        int minZ = -1871;
        int maxZ = -1870;
        int mapY = 103;

        if (playerX < minX) {
            return false;
        }
        if (playerX > maxX) {
            return false;
        }
        if (playerZ < minZ) {
            return false;
        }
        if (playerZ > maxZ) {
            return false;
        }

        return playerY == mapY + 1;
    }

    @SubscribeEvent
    public static void OnJumpMapRace(TickEvent.PlayerTickEvent event) {
        if (!(miniGameType == MiniGameType.JUMP_MAP_RACE && isMiniGameRunning)) {
            return;
        }

        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            //goal
            if (isInJumpMapRaceGoal(player) && player.gameMode.isSurvival()) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_race_goal"), false);
                player.inventory.add(new ItemStack(Items.NETHER_STAR, 1));
                player.moveTo(returnPoint);
            }

            //fail
            if (isInJumpMapRaceMap(player) && player.fallDistance >= 5 /*player.position().y < jumpMapRaceFailY*/ && player.gameMode.isSurvival()) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.jump_map_race_fail"), false);
                player.moveTo(returnPoint);
            }

        }
    }

    //dice of fortune
    public static void diceOfFortuneResult(World world) {
        //if no one joined
        if (diceOfFortune.isEmpty()) {
            return;
        }

        //sort list, get winner
        int winningNumber;
        UUID winningPlayerUUID;
        ServerPlayerEntity winningPlayer;
        
        while (!diceOfFortune.isEmpty()) {
            winningNumber = new TreeMap<>(diceOfFortune).lastKey();
            winningPlayerUUID = diceOfFortune.get(winningNumber);
            winningPlayer = world.getServer().getPlayerList().getPlayer(winningPlayerUUID);
            
            if (winningPlayer != null) {
                //announce winner
                for (UUID playerUUID : diceOfFortune.values()) {
                    ServerPlayerEntity joinedPlayer = world.getServer().getPlayerList().getPlayer(playerUUID);
                    if (joinedPlayer != null) {
                        joinedPlayer.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_winner",
                                new StringTextComponent(winningPlayer.getName().getString()).withStyle(TextFormatting.YELLOW),
                                new StringTextComponent(Integer.toString(winningNumber)).withStyle(TextFormatting.AQUA)), false);
                    }

                }
                //give prize
                winningPlayer.inventory.add(new ItemStack(Items.NETHER_STAR, 1));

                //reset list
                diceOfFortune.clear();

                return;

            } else {
                //if winner not present
                diceOfFortune.remove(winningNumber);
            }
        }
    }

    //anti-cheat
    public static boolean isInMagmaFallMap(ServerPlayerEntity player) {
        int playerX = player.blockPosition().getX();
        int playerZ = player.blockPosition().getZ();
        int playerY = player.blockPosition().getY();
        int minX = -540;
        int maxX = -515;
        int minZ = -1909;
        int maxZ = -1879;
        int maxY = 116;

        if (playerX < minX) {
            return false;
        }
        if (playerX > maxX) {
            return false;
        }
        if (playerZ < minZ) {
            return false;
        }
        if (playerZ > maxZ) {
            return false;
        }

        return playerY < maxY;
    }

    public static boolean isInJumpMapRaceMap(ServerPlayerEntity player) {
        int playerX = player.blockPosition().getX();
        int playerZ = player.blockPosition().getZ();
        int playerY = player.blockPosition().getY();
        int minX = -583;
        int maxX = -558;
        int minZ = -1899;
        int maxZ = -1869;
        int maxY = 116;

        if (playerX < minX) {
            return false;
        }
        if (playerX > maxX) {
            return false;
        }
        if (playerZ < minZ) {
            return false;
        }
        if (playerZ > maxZ) {
            return false;
        }

        return playerY < maxY;
    }

    //no riding
    @SubscribeEvent
    public static void onRiding(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (isInJumpMapRaceMap(player) || isInMagmaFallMap(player)) {
                if ((player.getVehicle() instanceof PixelmonEntity || player.getVehicle() instanceof BikeEntity) && player.gameMode.isSurvival()) {
                    Entity vehicle = player.getVehicle();
                    player.stopRiding();
                    player.moveTo(returnPoint);
                    vehicle.moveTo(returnPoint);
                    player.startRiding(vehicle);
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_riding_forbidden").withStyle(TextFormatting.RED), false);
                }
            }
        }
    }

    //tp when logged in
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            if ((isInJumpMapRaceMap(player) || isInMagmaFallMap(player)) && player.gameMode.isSurvival()) {
                player.moveTo(returnPoint);
            }
        }
    }
}
