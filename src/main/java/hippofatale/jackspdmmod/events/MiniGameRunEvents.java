package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.events.PokemonSendOutEvent;
import com.pixelmonmod.pixelmon.entities.bikes.BikeEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.minigames.DiceOfFortune;
import hippofatale.jackspdmmod.minigames.JumpMapRace;
import hippofatale.jackspdmmod.minigames.MagmaFall;
import hippofatale.jackspdmmod.util.MiniGameType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class MiniGameRunEvents {
    public static final Vector3d returnPoint = new Vector3d(-93, 44, -8);

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static MinecraftServer server;
    private static final int beginDelayMinutes = 3;

    @SubscribeEvent
    public static void onScheduleMiniGames(FMLServerStartedEvent event) {
        server = event.getServer();
        startMiniGameTimer();
    }

    private static void startMiniGameTimer() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime nextMiniGameTime = currentTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        long initialDelay = Duration.between(currentTime, nextMiniGameTime).getSeconds();
        long period = TimeUnit.HOURS.toSeconds(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (server != null) {
                    server.execute(() -> {
                        if (!isMiniGameOpen && !isMiniGameRunning) {
                            ZonedDateTime currentHour = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                            int miniGameNum = currentHour.getHour();
                            switch (miniGameNum % 3) {
                                case 0:
                                    openMiniGame(MiniGameType.MAGMA_FALL);
                                    break;
                                case 1:
                                    openMiniGame(MiniGameType.JUMP_MAP_RACE);
                                    break;
                                case 2:
                                    openMiniGame(MiniGameType.DICE_OF_FORTUNE);
                                    break;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    public static void openMiniGame(MiniGameType openedMiniGame) {
        if (server == null) {
            return;
        }

        //enable join command
        isMiniGameOpen = true;
        miniGameType = openedMiniGame;

        //prepare mini-game
        switch (miniGameType) {
            case MAGMA_FALL:
                MagmaFall.prepareMagmaFall(server);
                break;
            case JUMP_MAP_RACE:
                JumpMapRace.prepareJumpMapRace(server);
                break;
            case DICE_OF_FORTUNE:
                DiceOfFortune.prepareDiceOfFortune(server);
                break;
        }

        //schedule mini-game start
        scheduler.schedule(() -> {
           try {
               if (server != null) {
                   server.execute(() -> {
                       beginMiniGame(miniGameType);
                   });
               }
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
        }, beginDelayMinutes, TimeUnit.MINUTES);

        //announce mini-game
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime beginTime = currentTime.plusMinutes(beginDelayMinutes);
        miniGameApplicants.clear();
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (ServerPlayerEntity player : players) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_announce", beginTime.getHour(), beginTime.getMinute() ,miniGameType.getName()), false);
        }
    }

    private static void beginMiniGame(MiniGameType beginningMiniGame) {
        if (server == null) {
            return;
        }

        //disable join command
        isMiniGameOpen = false;

        //announce mini-game join end
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (ServerPlayerEntity player : players) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_started", miniGameType.getName()), false);
        }

        //begin mini-game
        isMiniGameRunning = true;
        switch (beginningMiniGame) {
            case MAGMA_FALL:
                MagmaFall.beginMagmaFall();
                break;
            case JUMP_MAP_RACE:
                JumpMapRace.beginJumpMapRace();
                break;
            case DICE_OF_FORTUNE:
                DiceOfFortune.beginDiceOfFortune();
                break;
        }
    }

    //anti-cheat
    //no riding
    @SubscribeEvent
    public static void onRiding(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (player.getBoundingBox().intersects(MagmaFall.field) || player.getBoundingBox().intersects(JumpMapRace.field)) {
                if ((player.getVehicle() instanceof PixelmonEntity || player.getVehicle() instanceof BikeEntity) && player.gameMode.isSurvival()) {
                    player.stopRiding();
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
            if ((player.getBoundingBox().intersects(MagmaFall.field) || player.getBoundingBox().intersects(JumpMapRace.field)) && player.gameMode.isSurvival()) {
                player.teleportTo(
                        returnPoint.x,
                        returnPoint.y,
                        returnPoint.z);
            }
        }
    }

    //cancel pokemon send out
    @SubscribeEvent
    public static void onPokemonSendOut(PokemonSendOutEvent.Pre event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if ((player.getBoundingBox().intersects(MagmaFall.field) || player.getBoundingBox().intersects(JumpMapRace.field)) && player.gameMode.isSurvival()) {
            event.setCanceled(true);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_send_out_forbidden").withStyle(TextFormatting.RED), false);
        }

    }
}
