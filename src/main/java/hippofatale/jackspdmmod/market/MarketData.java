package hippofatale.jackspdmmod.market;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class MarketData {
    public static int melonOriginalPrice = 712;
    public static int pumpkinOriginalPrice = 1700;
    public static int cocoaOriginalPrice = 1160;
    public static int wheatOriginalPrice = 840;
    public static int potatoOriginalPrice = 584;
    public static int carrotOriginalPrice = 584;

    private static final List<Item> itemList = Arrays.asList(new Item[]{
            Items.COAL,
            Items.LAPIS_LAZULI,
            Items.REDSTONE,
            Items.IRON_ORE,
            Items.GOLD_ORE,
            Items.DIAMOND,
            Items.EMERALD,

            Items.MELON_SLICE,
            Items.PUMPKIN,
            Items.COCOA_BEANS,
            Items.WHEAT,
            Items.POTATO,
            Items.CARROT
    });
    private static final List<String> itemStringList = Arrays.asList(new String[]{
            "coal",
            "lapis_lazuli",
            "redstone",
            "iron_ore",
            "gold_ore",
            "diamond",
            "emerald",

            "melon_slice",
            "pumpkin",
            "cocoa_beans",
            "wheat",
            "potato",
            "carrot"
    });

    public static void saveMarketData() {
        int year = marketLastUpdateDate.getYear();
        int month = marketLastUpdateDate.getMonthValue();
        int day = marketLastUpdateDate.getDayOfMonth();
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(MARKET_FILE.toPath()))) {
            out.writeInt(year);
            out.writeInt(month);
            out.writeInt(day);
            out.writeObject(marketPrices);
            out.writeObject(marketQuantities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMarketData() {
        if (!MARKET_FILE.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(MARKET_FILE.toPath()))) {
            int loadedYear = in.readInt();
            int loadedMonth = in.readInt();
            int loadedDay = in.readInt();
            Map<String, Integer> loadedMarketPrices = (Map<String, Integer>) in.readObject();
            Map<String, Integer> loadedMarketQuantities = (Map<String, Integer>) in.readObject();
            marketLastUpdateDate = LocalDate.of(loadedYear, loadedMonth, loadedDay);
            marketPrices.putAll(loadedMarketPrices);
            marketQuantities.putAll(loadedMarketQuantities);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void cropsPriceChange(){
        marketPrices.replace("melon_slice", melonOriginalPrice + (int) (Math.random() * 201) - 100);
        marketPrices.replace("pumpkin", pumpkinOriginalPrice + (int) (Math.random() * 201) - 100);
        marketPrices.replace("cocoa_beans", cocoaOriginalPrice + (int) (Math.random() * 201) - 100);
        marketPrices.replace("wheat", wheatOriginalPrice + (int) (Math.random() * 201) - 100);
        marketPrices.replace("potato", potatoOriginalPrice + (int) (Math.random() * 201) - 100);
        marketPrices.replace("carrot", carrotOriginalPrice + (int) (Math.random() * 201) - 100);
    }

    public static Item getItem(int itemIndex) {
        return itemList.get(itemIndex);
    }

    public static String getItemName(int itemIndex) {
        return itemStringList.get(itemIndex);
    }
}
