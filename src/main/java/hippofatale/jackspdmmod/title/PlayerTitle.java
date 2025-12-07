package hippofatale.jackspdmmod.title;

import net.minecraft.nbt.CompoundNBT;

public class PlayerTitle {
    private int displayingTitleIndex = 0;
//    private int[] titleUnlockedList = {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] titleUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public int getTitleUnlocked(int titleIndex) {
        return titleUnlockedList[titleIndex];
    }

    public void unlockTitle(int titleIndex) {
        this.titleUnlockedList[titleIndex] = 1;
    }

    public void lockTitle(int titleIndex) {
        this.titleUnlockedList[titleIndex] = 0;
    }

    public int getDisplayingTitleIndex() {
        return this.displayingTitleIndex;
    }

    public void setDisplayingTitleIndex(int titleIndex) {
        this.displayingTitleIndex = titleIndex;
    }

    public int[] getTitleUnlockedList() {
        return titleUnlockedList;
    }

    public void copyFrom(PlayerTitle source) {
        this.displayingTitleIndex = source.displayingTitleIndex;
        this.titleUnlockedList = source.titleUnlockedList.clone();
    }

    public void saveNBTData(CompoundNBT nbt) {
        nbt.putInt("displaying_title_index", displayingTitleIndex);
        nbt.putIntArray("title_unlocked_list", titleUnlockedList);
    }

    public void loadNBTData(CompoundNBT nbt) {
        displayingTitleIndex = nbt.getInt("displaying_title_index");
        titleUnlockedList = nbt.getIntArray("title_unlocked_list").clone();
    }
}
