package me.hippofatale.jackspdmmod.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class ClientTitleData {
    private static int displayingTitleIndex = 0;
    private static int[] titleUnlockedList = {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static int getTitleUnlocked(int titleIndex) {
        return titleUnlockedList[titleIndex];
    }

    public static void unlockTitle(int titleIndex) {
        ClientTitleData.titleUnlockedList[titleIndex] = 1;
    }

    public static void lockTitle(int titleIndex) {
        ClientTitleData.titleUnlockedList[titleIndex] = 0;
    }

    public static int getDisplayingTitleIndex() {
        return ClientTitleData.displayingTitleIndex;
    }

    public static void setDisplayingTitleIndex(int titleIndex) {
        ClientTitleData.displayingTitleIndex = titleIndex;
    }

    public static void setTitleUnlockedList(int[] unlockedList) {
        ClientTitleData.titleUnlockedList = unlockedList.clone();
    }
    private static List<ITextComponent> titleList = Arrays.asList(new ITextComponent[]{
            new StringTextComponent(""),
            new TranslationTextComponent("title.jackspdmmod.stealth_unit").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.vanguard_unit").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.artillery_unit").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.intelligence_unit").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.life_of_a_miner").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.life_of_a_farmer").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.legendary_owner").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.mythical_owner").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.digidestined").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.no_server_no_life").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.everyones_neighbor").withStyle(TextFormatting.YELLOW),
            new TranslationTextComponent("title.jackspdmmod.blacklisted").withStyle(TextFormatting.GRAY),
            new TranslationTextComponent("title.jackspdmmod.gold_rich").withStyle(TextFormatting.GOLD),
            new TranslationTextComponent("title.jackspdmmod.diamond_rich").withStyle(TextFormatting.AQUA),
            new TranslationTextComponent("title.jackspdmmod.pdm_season1_champion").withStyle(TextFormatting.DARK_RED),
            new TranslationTextComponent("title.jackspdmmod.pdm_season2_champion").withStyle(TextFormatting.GOLD)
    });

    public static ITextComponent getTitleText(int titleIndex) {
        return titleList.get(titleIndex);
    }
    public static ITextComponent getTitleTextBold(int titleIndex) {
        return titleList.get(titleIndex).copy().withStyle(TextFormatting.BOLD);
    }
}
