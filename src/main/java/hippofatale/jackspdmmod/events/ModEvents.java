package hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.commands.*;
import hippofatale.jackspdmmod.home.Home;
import hippofatale.jackspdmmod.home.HomeData;
import hippofatale.jackspdmmod.market.MarketData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncS2CPacket;
import hippofatale.jackspdmmod.networking.packet.TeleportDataSyncS2CPacket;
import hippofatale.jackspdmmod.teleport.PlayerTeleportUnlockProvider;
import hippofatale.jackspdmmod.teleport.TeleportData;
import hippofatale.jackspdmmod.title.PlayerTitleProvider;
import hippofatale.jackspdmmod.title.TitleData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.server.command.ConfigCommand;

import java.time.*;
import java.util.*;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class ModEvents {
    //commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MenuCommand(event.getDispatcher());
        new TitleCommand(event.getDispatcher());
        new ClubCommand(event.getDispatcher());
        new ClubPointCommand(event.getDispatcher());
        new BasicPokemonTicketCommand(event.getDispatcher());
        new HomeCommand(event.getDispatcher());
        new TMTradeTicketCommand(event.getDispatcher());
        new TRTradeTicketCommand(event.getDispatcher());
        new PDTransferCommand(event.getDispatcher());
        new BattleSpectateCommand(event.getDispatcher());
        new JoinMiniGameCommand(event.getDispatcher());
        new CasinoSwitchingCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            pendingInvites.putIfAbsent(player.getUUID(), new ArrayList<>());

            int[] cropPrices = {marketPrices.get("melon_slice"), marketPrices.get("pumpkin"), marketPrices.get("cocoa_beans"), marketPrices.get("wheat"), marketPrices.get("potato"), marketPrices.get("carrot")};
            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropPrices), player);
        }
    }

    //display title
    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                if (playerTitle.getDisplayingTitleIndex() == 0) {
                    event.setDisplayname(player.getName());
                }
                else {
                    event.setDisplayname(new StringTextComponent("[")
                            .append(TitleData.getTitleTextBold(playerTitle.getDisplayingTitleIndex()))
                            .append("]")
                            .append(player.getName()));
                }
            });
        }
    }

    //unlock teleport
    @SubscribeEvent
    public static void onPlayerVisitTown(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getTarget() instanceof NPCEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
                NPCEntity npc = (NPCEntity) event.getTarget();

                List<Vector3d> coordinates = TeleportData.getTeleportCoordinatesList();
                for (Vector3d coordinate : coordinates) {
                    if (new Vector3d(npc.getX(), npc.getY(), npc.getZ()).distanceTo(coordinate) < 5) {
                        int townIndex = coordinates.indexOf(coordinate);
                        player.getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).ifPresent(playerTeleportUnlock -> {
                            if (playerTeleportUnlock.getTeleportUnlocked(townIndex) == 0) {
                                playerTeleportUnlock.unlockTeleport(townIndex);
                                ModMessages.sendToPlayer(new TeleportDataSyncS2CPacket(playerTeleportUnlock.getTeleportUnlockedList(), playerTeleportUnlock.getHomeUnlocked(), playerTeleportUnlock.getClubHomeUnlocked()), player);
                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleport_unlocked", TeleportData.getTeleportName(townIndex)), false);
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    private static int priceUpdateTickCount = 60 * 20;
    private static final int priceUpdateTickPeriod = 60 * 20;
    //change market price
    @SubscribeEvent
    public static void onMidnightMarketUpdate(TickEvent.ServerTickEvent event) {
        priceUpdateTickCount++;
        if (priceUpdateTickCount > priceUpdateTickPeriod) {
            LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
            if (!currentDate.equals(marketLastUpdateDate)) {
                MarketData.cropsPriceChange();
                marketLastUpdateDate = currentDate;
                MarketData.saveMarketData();
            }
            priceUpdateTickCount = 0;
        }
    }

//    private static int checkNightVisionTickCount = 0;
//    private static final int checkNightVisionTickPeriod = 1 * 20;
//    //night vision in school
//    @SubscribeEvent
//    public static void onAtSchool(TickEvent.PlayerTickEvent event) {
//        if (event.player instanceof ServerPlayerEntity) {
//            checkNightVisionTickCount++;
//            if (checkNightVisionTickCount >= checkNightVisionTickPeriod) {
//                ServerPlayerEntity player = (ServerPlayerEntity) event.player;
//                BlockPos playerPos = player.blockPosition();
//                boolean hasNightVision = player.hasEffect(Effects.NIGHT_VISION);
//                boolean atSchool = playerPos.getX() >= -626 && playerPos.getX() <= -328 && playerPos.getZ() >= -980 && playerPos.getZ() <= -712;
//                if (atSchool && !hasNightVision) {
//                    player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 1000000));
//                } else if (!atSchool && hasNightVision && !player.isCreative()) {
//                    player.removeEffect(Effects.NIGHT_VISION);
//                }
//                checkNightVisionTickCount = 0;
//            }
//        }
//    }

    //data
    @SubscribeEvent
    public static void onLoadDataAtStart(FMLServerAboutToStartEvent event) {
        //load data at start
        ClubData.loadClubData();
        HomeData.loadHomeData();
        MarketData.loadMarketData();

        //debugging: check data
        Map<UUID, BlockPos> personalHomePoses = new HashMap<>();
        for (Map.Entry<UUID, Home> entry : personalHomes.entrySet()) {
            personalHomePoses.put(entry.getKey(), entry.getValue().getPlacardPos());
        }
        Map<String, BlockPos> clubHomePoses = new HashMap<>();
        for (Map.Entry<String, Home> entry : clubNameHomes.entrySet()) {
            clubHomePoses.put(entry.getKey(), entry.getValue().getPlacardPos());
        }

//        //debugging: modify data
//        //last modified: 2025 Dec 14 2.2.2
//        HomeData.removeClubHomeData("test");
//        HomeData.removeClubHomeData("scon");
    }

    @SubscribeEvent
    public static void onSaveDataAtStopping(FMLServerStoppingEvent event) {
        //save data at stop
        ClubData.saveClubData();
        HomeData.saveHomeData();
        MarketData.saveMarketData();
    }
}
