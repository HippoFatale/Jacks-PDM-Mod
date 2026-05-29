package hippofatale.jackspdmmod.events;

import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.market.MarketData;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.time.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hippofatale.jackspdmmod.JacksPDMMod.marketLastUpdateDate;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class MarketPriceUpdateEvents {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onScheduleMarketUpdate(FMLServerStartedEvent event) {
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (!currentDate.equals(marketLastUpdateDate)) {
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
                        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
                        marketPriceUpdate(currentDate);
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    private static void marketPriceUpdate(LocalDate updateDate) {
        MarketData.cropsPriceChange();
        marketLastUpdateDate = updateDate;
        MarketData.saveMarketData();
    }
}
