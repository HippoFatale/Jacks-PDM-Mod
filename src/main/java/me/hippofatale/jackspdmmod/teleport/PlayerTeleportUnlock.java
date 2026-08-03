package me.hippofatale.jackspdmmod.teleport;

import net.minecraft.nbt.CompoundNBT;

public class PlayerTeleportUnlock {
    private boolean[] teleportUnlockedList;
    private boolean homeUnlocked = false;
    private boolean clubHomeUnlocked = false;

    public PlayerTeleportUnlock() {
        teleportUnlockedList = new boolean[TeleportManager.getTeleportCount()];
        for (int i = 0; i < teleportUnlockedList.length; i++) {
            teleportUnlockedList[i] = TeleportManager.isOpenByDefault(i);
        }
    }

    public boolean[] getTeleportUnlockedList() {
        return teleportUnlockedList;
    }

    public boolean isTeleportUnlocked(int townIndex) {
        return teleportUnlockedList[townIndex];
    }

    public boolean isHomeUnlocked() {
        return homeUnlocked;
    }

    public boolean isClubHomeUnlocked() {
        return clubHomeUnlocked;
    }

    public void unlockTeleport(int teleportIndex) {
        this.teleportUnlockedList[teleportIndex] = true;
    }

    public void setHomeUnlocked(boolean isUnlocked) {
        this.homeUnlocked = isUnlocked;
    }

    public void setClubHomeUnlocked(boolean isUnlocked) {
        this.clubHomeUnlocked = isUnlocked;
    }

    public void copyFrom(PlayerTeleportUnlock source) {
        this.teleportUnlockedList = source.teleportUnlockedList.clone();
        this.homeUnlocked = source.homeUnlocked;
        this.clubHomeUnlocked = source.clubHomeUnlocked;
    }

    public void saveNBTData(CompoundNBT nbt) {
        byte[] byteArray = new byte[teleportUnlockedList.length];
        for (int i = 0; i < teleportUnlockedList.length; i++) {
            byteArray[i] = (byte) (teleportUnlockedList[i] ? 1 : 0);
        }
        nbt.putByteArray("town_unlocked_list", byteArray);
        nbt.putBoolean("home_unlocked", homeUnlocked);
        nbt.putBoolean("club_home_unlocked", clubHomeUnlocked);
    }

    public void loadNBTData(CompoundNBT nbt) {
        byte[] byteArray = nbt.getByteArray("town_unlocked_list");
        int teleportCount = TeleportManager.getTeleportCount();
        teleportUnlockedList = new boolean[teleportCount];
        for (int i = 0; i < Math.min(byteArray.length, teleportCount); i++) {
            teleportUnlockedList[i] = byteArray[i] == 1;
        }
        // Ensure teleports open by default are always unlocked
        for (int i = 0; i < teleportCount; i++) {
            if (TeleportManager.isOpenByDefault(i)) {
                teleportUnlockedList[i] = true;
            }
        }
        homeUnlocked = nbt.getBoolean("home_unlocked");
        clubHomeUnlocked = nbt.getBoolean("club_home_unlocked");
    }
}
