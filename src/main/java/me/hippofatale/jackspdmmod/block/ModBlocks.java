package me.hippofatale.jackspdmmod.block;

import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.block.custom.OreSeedBlock;
import me.hippofatale.jackspdmmod.block.custom.PlacardBlock;
import me.hippofatale.jackspdmmod.block.custom.SlotMachineBlock;
import me.hippofatale.jackspdmmod.home.HomeSize;
import me.hippofatale.jackspdmmod.home.HomeType;
import me.hippofatale.jackspdmmod.item.ModItemGroup;
import me.hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.math.BigDecimal;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JacksPDMMod.MOD_ID);

    //ore seed
    public static final RegistryObject<Block> ORE_SEED = registerBlock("ore_seed",
            () -> new OreSeedBlock(AbstractBlock.Properties.of(Material.STONE)));

    //placards
    public static final RegistryObject<Block> PERSONAL_SMALL_PLACARD_BLOCK = registerBlock("personal_small_placard",
            () -> new PlacardBlock(AbstractBlock.Properties.of(Material.WOOD), HomeType.PERSONAL, HomeSize.SMALL, WoodType.BIRCH));
    public static final RegistryObject<Block> PERSONAL_MEDIUM_PLACARD_BLOCK = registerBlock("personal_medium_placard",
            () -> new PlacardBlock(AbstractBlock.Properties.of(Material.WOOD), HomeType.PERSONAL, HomeSize.MEDIUM, WoodType.OAK));
    public static final RegistryObject<Block> CLUB_LARGE_PLACARD_BLOCK = registerBlock("club_large_placard",
            () -> new PlacardBlock(AbstractBlock.Properties.of(Material.WOOD), HomeType.CLUB, HomeSize.LARGE, WoodType.SPRUCE));

    //slot machine
    public static final RegistryObject<Block> SLOT_MACHINE = registerBlock("slot_machine",
            () -> new SlotMachineBlock(AbstractBlock.Properties.of(Material.STONE)));

    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
