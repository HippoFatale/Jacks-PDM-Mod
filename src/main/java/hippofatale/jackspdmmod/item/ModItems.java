package hippofatale.jackspdmmod.item;

import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.item.custom.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JacksPDMMod.MOD_ID);

    //ticket
    public static final RegistryObject<Item> STARTER_PACKAGE_TICKET = ITEMS.register("starter_package_ticket",
            () -> new StarterPackageTicketItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.starter_package_ticket.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> BASIC_SELECTION_TICKET = ITEMS.register("basic_pokemon_ticket",
            () -> new Item(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.basic_pokemon_ticket.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> SHINY_TRADE_TICKET = ITEMS.register("shiny_trade_ticket",
            () -> new Item(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.shiny_trade_ticket.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> TM_TRADE_TICKET = ITEMS.register("tm_trade_ticket",
            () -> new Item(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tm_trade_ticket.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> TR_TRADE_TICKET = ITEMS.register("tr_trade_ticket",
            () -> new Item(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tr_trade_ticket.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });

    //etc
    public static final RegistryObject<Item> DYNAMAX_LICENSE = ITEMS.register("dynamax_license",
            () -> new Dynamaxitem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.dynamax_license.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> CLUB_PAPER = ITEMS.register("club_paper",
            () -> new Item(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.club_paper.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> CLUB_POINTS = ITEMS.register("club_points",
            () -> new ClubPointItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.club_points.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> INVITATION_CARD = ITEMS.register("invitation_card",
            () -> new ClubPointItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.invitation_card.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });

    //package
    public static final RegistryObject<Item> STARTER_PACKAGE = ITEMS.register("starter_package",
            () -> new StarterPackageItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.starter_package.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });

    //spawn gacha
    public static final RegistryObject<Item> LEGENDARY_SPAWN_GACHA = ITEMS.register("legendary_spawn_gacha",
            () -> new GachaSpawnItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'L') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.legendary_spawn_gacha.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> MYTHICAL_SPAWN_GACHA = ITEMS.register("mythical_spawn_gacha",
            () -> new GachaSpawnItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'M') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.mythical_spawn_gacha.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> DIGIMON_SPAWN_GACHA = ITEMS.register("digimon_spawn_gacha",
            () -> new GachaSpawnItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'D') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.digimon_spawn_gacha.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });

    //gacha
    public static final RegistryObject<Item> ALL_TOOL_GACHA_CUBE = ITEMS.register("all_tool_gacha_cube",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'A') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.all_tool_gacha_cube.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> RARE_TOOL_GACHA_CUBE = ITEMS.register("rare_tool_gacha_cube",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'R') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.rare_tool_gacha_cube.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> GREAT_GACHA_BOX = ITEMS.register("great_gacha_box",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'G') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.great_gacha_box.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> ULTRA_GACHA_BOX = ITEMS.register("ultra_gacha_box",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'U') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.ultra_gacha_box.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> MASTER_GACHA_BOX = ITEMS.register("master_gacha_box",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'M') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.master_gacha_box.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });
    public static final RegistryObject<Item> SPECIAL_GACHA_BOX = ITEMS.register("special_gacha_box",
            () -> new GachaItem(new Item.Properties().tab(ModItemGroup.JACKS_PDM_GROUP), 'S') {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
                    if (Screen.hasShiftDown()) {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.special_gacha_box.tooltip"));
                    } else {
                        tooltip.add(new TranslationTextComponent("item.jackspdmmod.tooltip_shift"));
                    }
                    super.appendHoverText(itemStack, world, tooltip, flag);
                }
            });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
