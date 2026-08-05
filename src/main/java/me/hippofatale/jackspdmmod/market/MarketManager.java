package me.hippofatale.jackspdmmod.market;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import net.minecraft.item.Items;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MarketManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "jackspdmmod/market.json");

    public static LocalDate lastUpdateDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
    public static List<MarketItem> oreMarketItems = new ArrayList<>();
    public static List<MarketItem> cropMarketItems = new ArrayList<>();

    public static void save() {
        try {
            File parentDir = FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(FILE)) {
                MarketContainer container = new MarketContainer();
                container.lastUpdateDate = lastUpdateDate.toString();
                container.oreMarketItems = MarketManager.oreMarketItems;
                container.cropMarketItems = MarketManager.cropMarketItems;

                GSON.toJson(container, writer);
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("채집물 데이터를 저장하는 중 오류가 발생했습니다!", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) {
            initDefaultMarket();
            save();
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            MarketContainer container = GSON.fromJson(reader, MarketContainer.class);

            if (container != null) {
                lastUpdateDate = LocalDate.parse(container.lastUpdateDate);

                oreMarketItems = container.oreMarketItems != null ? container.oreMarketItems : new ArrayList<>();
                for (MarketItem item : oreMarketItems) {
                    item.restoreItemObject();
                }

                cropMarketItems = container.cropMarketItems != null ? container.cropMarketItems : new ArrayList<>();
                for (MarketItem item : cropMarketItems) {
                    item.restoreItemObject();
                }
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("채집물 데이터를 로드하는 중 오류가 발생했습니다!", e);
            initDefaultMarket();
        }
    }

    private static void initDefaultMarket() {
        lastUpdateDate = LocalDate.of(2025, 7, 27);

        oreMarketItems.clear();
        oreMarketItems.add(new MarketItem(Items.COAL, 1, 12));
        oreMarketItems.add(new MarketItem(Items.LAPIS_LAZULI, 5, 12));
        oreMarketItems.add(new MarketItem(Items.REDSTONE, 3, 12));
        oreMarketItems.add(new MarketItem(Items.IRON_ORE, 20, 1));
        oreMarketItems.add(new MarketItem(Items.GOLD_ORE, 30, 1));
        oreMarketItems.add(new MarketItem(Items.DIAMOND, 300, 1));
        oreMarketItems.add(new MarketItem(Items.EMERALD, 3500, 1));

        cropMarketItems.clear();
        cropMarketItems.add(new MarketItem(Items.MELON_SLICE, 712, 32));
        cropMarketItems.add(new MarketItem(Items.PUMPKIN, 1700, 32));
        cropMarketItems.add(new MarketItem(Items.COCOA_BEANS, 1160, 64));
        cropMarketItems.add(new MarketItem(Items.WHEAT, 840, 32));
        cropMarketItems.add(new MarketItem(Items.POTATO, 584, 32));
        cropMarketItems.add(new MarketItem(Items.CARROT, 584, 32));
    }

    private static class MarketContainer {
        String lastUpdateDate;
        List<MarketItem> oreMarketItems;
        List<MarketItem> cropMarketItems;
    }
}
