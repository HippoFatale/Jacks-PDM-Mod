package me.hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBall;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.items.*;
import com.pixelmonmod.pixelmon.items.heldItems.MailItem;
import me.hippofatale.jackspdmmod.item.ModItems;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class GachaLists {
    private static final List<Item> rareToolGacha = new ArrayList<>();
    private static final List<ItemStack> greatGacha = new ArrayList<>();
    private static final List<ItemStack> ultraGacha =  new ArrayList<>();
    private static final List<ItemStack> masterGacha = new ArrayList<>();
    private static final List<Item> allToolGacha =  new ArrayList<>();
    private static final List<ItemStack> specialGacha = new ArrayList<>();

    private static final Set<Integer> legendarySpawnGacha = new HashSet<>();
    private static final Set<Integer> mythicalSpawnGacha = new HashSet<>();
    private static final Set<Integer> digimonSpawnGacha = new HashSet<>();


    public static void buildLists() {
        setRareToolGacha();
        setGreatGachaBox();
        setUltraGachaBox();
        setMasterGachaBox();
        setAllToolGachaCube();
        setSpecialGacha();

        setLegendarySpawnGacha();
        setMythicalSpawnGacha();
        setDigimonSpawnGacha();
    }

    //region Rare Tool Gacha Cube
    public static List<Item> getRareToolGacha() {
        return rareToolGacha;
    }

    private static void setRareToolGacha() {
        rareToolGacha.clear();

        List<Item> gachaItemList = Arrays.asList(new Item[]{
                PixelmonItems.leftovers,
                PixelmonItems.life_orb,
                PixelmonItems.choice_band,
                PixelmonItems.choice_scarf,
                PixelmonItems.choice_specs,
                PixelmonItems.sitrus_berry,
                PixelmonItems.lum_berry,
                PixelmonItems.muscle_band,
                PixelmonItems.black_glasses,
                PixelmonItems.black_sludge,
                PixelmonItems.focus_sash,
                PixelmonItems.focus_band,
        });

        rareToolGacha.addAll(gachaItemList);
    }
    //endregion

    //region Great Gacha Box
    public static List<ItemStack> getGreatGacha() {
        return greatGacha;
    }

    public static void setGreatGachaBox() {
        greatGacha.clear();

        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(PixelmonItems.rare_candy, 2),
                new ItemStack(ModItems.ALL_TOOL_GACHA_CUBE.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "great_ball")), 5)
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                2,
                1,
                1
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);
        greatGacha.addAll(gachaTableList);
    }
    //endregion

    //region Ultra Gacha Box
    public static List<ItemStack> getUltraGacha() {
        return ultraGacha;
    }

    public static void setUltraGachaBox() {
        ultraGacha.clear();

        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get()),
                new ItemStack(PixelmonItems.rare_candy, 10),
                new ItemStack(ModItems.SHINY_TRADE_TICKET.get()),
                new ItemStack(ModItems.BASIC_SELECTION_TICKET.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "ultra_ball")), 10),
                new ItemStack(PixelmonItems.silver_bottle_cap)
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                2,
                3,
                4,
                2,
                8,
                1
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);
        ultraGacha.addAll(gachaTableList);
    }
    //endregion

    //region Master Gacha Box
    public static List<ItemStack> getMasterGacha() {
        return masterGacha;
    }

    public static void setMasterGachaBox() {
        masterGacha.clear();

        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get()),
                new ItemStack(ModItems.LEGENDARY_SPAWN_GACHA.get()),
                new ItemStack(ModItems.MYTHICAL_SPAWN_GACHA.get()),
                new ItemStack(ModItems.SHINY_TRADE_TICKET.get()),
                new ItemStack(ModItems.BASIC_SELECTION_TICKET.get()),
                new ItemStack(PixelmonItems.silver_bottle_cap),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "master_ball")))
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                6,
                2,
                2,
                5,
                2,
                2,
                1
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);
        masterGacha.addAll(gachaTableList);
    }
    //endregion

    //region All Tool Gacha Cube
    public static List<Item> getAllToolGacha() {
        return allToolGacha;
    }

    public static void setAllToolGachaCube() {
        allToolGacha.clear();

        Set<Item> gachaItemList = new HashSet<>();

        //pokeballs
        for (PokeBall pokeball : PokeBallRegistry.getAll()) {
            Item pokeballItem = pokeball.getBallItem().getItem();
            gachaItemList.add(pokeballItem);
        }

        for (Item item : ForgeRegistries.ITEMS) {
            //held items
            if (item instanceof HeldItem && !(item instanceof MailItem)) {
                gachaItemList.add(item);
            }
            //battle items
            else if (item instanceof BattleItem) {
                gachaItemList.add(item);
            }
            //repel items
            else if (item instanceof RepelItem) {
                gachaItemList.add(item);
            }
            //fossil items
            else if (item instanceof FossilItem) {
                gachaItemList.add(item);
            }
            //lure items
            else if (item instanceof LureItem) {
                gachaItemList.add(item);
            }
            //mint items
            else if (item instanceof MintItem) {
                gachaItemList.add(item);
            }
        }

        //evostones
        gachaItemList.addAll(PixelmonItems.getEvostoneList());

        //flute items
        gachaItemList.add(PixelmonItems.black_flute);
        gachaItemList.add(PixelmonItems.white_flute);

        //ability capsule/patch
        gachaItemList.add(PixelmonItems.ability_capsule);
        gachaItemList.add(PixelmonItems.ability_patch);

        //bottle cap items
        gachaItemList.add(PixelmonItems.gold_bottle_cap);
        gachaItemList.add(PixelmonItems.silver_bottle_cap);

        //removing items
        gachaItemList.remove(PixelmonItems.amulet_coin);

        allToolGacha.addAll(gachaItemList);
    }
    //endregion

    //region Special Gacha Box
    public static List<ItemStack> getSpecialGacha() {
        return specialGacha;
    }

    public static void setSpecialGacha() {
        specialGacha.clear();

        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(ModItems.LEGENDARY_SPAWN_GACHA.get()),
                new ItemStack(ModItems.MYTHICAL_SPAWN_GACHA.get()),
                new ItemStack(ModItems.DIGIMON_SPAWN_GACHA.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "master_ball"))),
                new ItemStack(Items.NETHER_STAR, 2),
                new ItemStack(PixelmonItems.gold_bottle_cap)
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                1,
                1,
                2,
                4,
                6,
                1
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);
        specialGacha.addAll(gachaTableList);
    }
    //endregion

    //region Legendary Spawn Gacha
    public static Set<Integer> getLegendarySpawnGacha() {
        return legendarySpawnGacha;
    }

    public static void setLegendarySpawnGacha() {
        legendarySpawnGacha.clear();

        Set<Integer> species = new IntOpenHashSet(PixelmonSpecies.getLegendaries(true));
        species.removeAll(PixelmonSpecies.getGenerationDex(20));

        legendarySpawnGacha.addAll(species);
    }
    //endregion

    //region Mythical Spawn Gacha
    public static Set<Integer> getMythicalSpawnGacha() {
        return mythicalSpawnGacha;
    }

    public static void setMythicalSpawnGacha() {
        mythicalSpawnGacha.clear();

        Set<Integer> species = new IntOpenHashSet(PixelmonSpecies.getMythicals());
        species.removeAll(PixelmonSpecies.getGenerationDex(20));
        species.remove(PixelmonSpecies.ARCEUS.getValueUnsafe().getDex());

        mythicalSpawnGacha.addAll(species);
    }
    //endregion

    //region Digimon Spawn Gacha
    public static Set<Integer> getDigimonSpawnGacha() {
        return digimonSpawnGacha;
    }

    public static void setDigimonSpawnGacha() {
        digimonSpawnGacha.clear();

        Set<Integer> species = new HashSet<>();
        species.add(2002);
        species.add(2155);
        species.add(2111);
        species.add(2003);
        species.add(2112);
        species.add(2091);
        species.add(2052);
        species.add(2008);
        species.add(2154);
        species.add(2005);
        species.add(2001);
        species.add(2174);
        species.add(2062);
        species.add(2134);
        species.add(2006);
        species.add(2113);
        species.add(2090);
        species.add(2007);
        species.add(2004);
        species.add(2153);
        species.add(2135);
        species.add(2069);
        species.add(2079);
        species.add(2148);
        species.add(2073);
        species.add(2204);
        species.add(2200);
        species.add(2193);
        species.add(2186);
        species.add(2211);

        digimonSpawnGacha.addAll(species);
    }
    //endregion

    private static List<ItemStack> getGachaTableList(List<ItemStack> itemStackList, List<Integer> chanceList) {
        List<ItemStack> gachaTableList = new ArrayList<>(Collections.emptyList());

        if (itemStackList.size() != chanceList.size()) {
            return itemStackList;
        }

        for (int i = 0; i < itemStackList.size(); i++) {
            for (int j = 0; j < chanceList.get(i); j++) {
                gachaTableList.add(itemStackList.get(i));
            }
        }
        return gachaTableList;
    }
}
