package me.hippofatale.jackspdmmod.client;

import me.hippofatale.jackspdmmod.title.TitleManager;

import net.minecraft.util.text.ITextComponent;

public class ClientTitleData {
    private static int displayingTitleIndex = 0;
    private static boolean[] titleUnlockedList;

    static {
        titleUnlockedList = new boolean[TitleManager.getTitleCount()];
        for (int i = 0; i < titleUnlockedList.length; i++) {
            titleUnlockedList[i] = TitleManager.isObtainedByDefault(i);
        }
    }

    public static boolean isTitleUnlocked(int titleIndex) {
        return titleUnlockedList[titleIndex];
    }

    public static void unlockTitle(int titleIndex) {
        titleUnlockedList[titleIndex] = true;
    }

    public static void lockTitle(int titleIndex) {
        titleUnlockedList[titleIndex] = false;
    }

    public static int getDisplayingTitleIndex() {
        return displayingTitleIndex;
    }

    public static void setDisplayingTitleIndex(int titleIndex) {
        ClientTitleData.displayingTitleIndex = titleIndex;
    }

    public static void setTitleUnlockedList(boolean[] unlockedList) {
        titleUnlockedList = unlockedList.clone();
    }

    public static ITextComponent getTitleText(int titleIndex) {
        return TitleManager.getTitle(titleIndex);
    }

    public static ITextComponent getTitleTextBold(int titleIndex) {
        return TitleManager.getTitleBold(titleIndex);
    }
}
