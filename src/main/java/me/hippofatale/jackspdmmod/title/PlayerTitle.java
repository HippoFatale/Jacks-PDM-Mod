package me.hippofatale.jackspdmmod.title;

import net.minecraft.nbt.CompoundNBT;

public class PlayerTitle {
    private int displayingTitleIndex = 0;
    private boolean[] titleUnlockedList;

    public PlayerTitle() {
        titleUnlockedList = new boolean[TitleManager.getTitleCount()];
        for (int i = 0; i < titleUnlockedList.length; i++) {
            titleUnlockedList[i] = TitleManager.isObtainedByDefault(i);
        }
    }

    public boolean isTitleUnlocked(int titleIndex) {
        return titleUnlockedList[titleIndex];
    }

    public void unlockTitle(int titleIndex) {
        this.titleUnlockedList[titleIndex] = true;
    }

    public void lockTitle(int titleIndex) {
        this.titleUnlockedList[titleIndex] = false;
    }

    public int getDisplayingTitleIndex() {
        return this.displayingTitleIndex;
    }

    public void setDisplayingTitleIndex(int titleIndex) {
        this.displayingTitleIndex = titleIndex;
    }

    public boolean[] getTitleUnlockedList() {
        return titleUnlockedList;
    }

    public void copyFrom(PlayerTitle source) {
        this.displayingTitleIndex = source.displayingTitleIndex;
        this.titleUnlockedList = source.titleUnlockedList.clone();
    }

    public void saveNBTData(CompoundNBT nbt) {
        nbt.putInt("displaying_title_index", displayingTitleIndex);
        byte[] byteArray = new byte[titleUnlockedList.length];
        for (int i = 0; i < titleUnlockedList.length; i++) {
            byteArray[i] = (byte) (titleUnlockedList[i] ? 1 : 0);
        }
        nbt.putByteArray("title_unlocked_list", byteArray);
    }

    public void loadNBTData(CompoundNBT nbt) {
        displayingTitleIndex = nbt.getInt("displaying_title_index");
        byte[] byteArray = nbt.getByteArray("title_unlocked_list");
        int titleCount = TitleManager.getTitleCount();
        titleUnlockedList = new boolean[titleCount];
        for (int i = 0; i < Math.min(byteArray.length, titleCount); i++) {
            titleUnlockedList[i] = byteArray[i] == 1;
        }
        // Ensure titles obtained by default are always unlocked
        for (int i = 0; i < titleCount; i++) {
            if (TitleManager.isObtainedByDefault(i)) {
                titleUnlockedList[i] = true;
            }
        }
    }
}
