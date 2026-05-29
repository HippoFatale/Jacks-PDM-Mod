package hippofatale.jackspdmmod.events;

import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.market.MarketData;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hippofatale.jackspdmmod.JacksPDMMod.marketLastUpdateDate;

//@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class MiniGameRunEvents {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static MinecraftServer server;

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

                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }
}
