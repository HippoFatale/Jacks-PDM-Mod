package me.hippofatale.jackspdmmod.events;

import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncS2CPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class MarketPriceUpdateEvents {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onScheduleMarketUpdate(FMLServerStartedEvent event) {
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (!currentDate.equals(MarketManager.lastUpdateDate)) {
            marketPriceUpdate(currentDate);
        }
        server = event.getServer();
        startMarketUpdateTimer();
    }

    private static void startMarketUpdateTimer() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime nextUpdateTime = currentTime.toLocalDate().plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul"));
        long initialDelay = Duration.between(currentTime, nextUpdateTime).getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (server != null) {
                    server.execute(() -> {
                        LocalDate updateDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
                        marketPriceUpdate(updateDate);

                        Map<String, MarketItem> cropDataToSync = new HashMap<>();
                        for (MarketItem item : MarketManager.cropMarketItems) {
                            if (item != null && item.getRegistryName() != null) {
                                cropDataToSync.put(item.getRegistryName(), item);
                            }
                        }

                        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
                            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropDataToSync), player);
                        }

                        JacksPDMMod.LOGGER.info("농작물 시세가 업데이트 되었습니다.");
                    });
                }
            } catch (Exception e) {
                JacksPDMMod.LOGGER.error("농작물 시세 업데이트 중 오류가 발생했습니다.", e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    private static void marketPriceUpdate(LocalDate updateDate) {
        cropsPriceChange();
        MarketManager.lastUpdateDate = updateDate;
        MarketManager.save();
    }

    private static void cropsPriceChange() {
        Random random = new java.util.Random();

        for (MarketItem item : MarketManager.cropMarketItems) {
            if (item == null) continue;

            int newPrice = item.getDefaultPrice() - 100 + random.nextInt(200);

            item.setPrice(newPrice);
        }
    }
}
