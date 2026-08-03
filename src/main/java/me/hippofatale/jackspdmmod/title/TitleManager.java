package me.hippofatale.jackspdmmod.title;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class TitleManager {
    private static List<Title> titleList = Arrays.asList(new Title[]{
            new Title(new StringTextComponent(""), true),

            new Title(new TranslationTextComponent("title.jackspdmmod.stealth_unit").withStyle(TextFormatting.GRAY), true),
            new Title(new TranslationTextComponent("title.jackspdmmod.vanguard_unit").withStyle(TextFormatting.GRAY), true),
            new Title(new TranslationTextComponent("title.jackspdmmod.artillery_unit").withStyle(TextFormatting.GRAY), true),
            new Title(new TranslationTextComponent("title.jackspdmmod.intelligence_unit").withStyle(TextFormatting.GRAY), true),

            new Title(new TranslationTextComponent("title.jackspdmmod.life_of_a_miner").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.life_of_a_farmer").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.legendary_owner").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.mythical_owner").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.digidestined").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.no_server_no_life").withStyle(TextFormatting.YELLOW), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.blacklisted").withStyle(TextFormatting.GRAY), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.gold_rich").withStyle(TextFormatting.GOLD), false),
            new Title(new TranslationTextComponent("title.jackspdmmod.diamond_rich").withStyle(TextFormatting.AQUA), false),

            new Title(new TranslationTextComponent("title.jackspdmmod.pdm_season1_champion").withStyle(TextFormatting.DARK_RED), false, true),
            new Title(new TranslationTextComponent("title.jackspdmmod.pdm_season2_champion").withStyle(TextFormatting.GOLD), false, true),
            new Title(new TranslationTextComponent("title.jackspdmmod.pdm_season3_champion").withStyle(TextFormatting.YELLOW), false, true)
    });

    public static ITextComponent getTitle(int titleIndex) {
        return titleList.get(titleIndex).getTitleText();
    }
    public static ITextComponent getTitleBold(int titleIndex) {
        return titleList.get(titleIndex).getTitleText().copy().withStyle(TextFormatting.BOLD);
    }

    public static int getTitleCount() {
        return titleList.size();
    }

    public static boolean isObtainedByDefault(int titleIndex) {
        return titleList.get(titleIndex).isObtainedByDefault();
    }

    public static boolean isHidden(int titleIndex) {
        return titleList.get(titleIndex).isHidden();
    }
}
