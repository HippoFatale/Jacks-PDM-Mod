package me.hippofatale.jackspdmmod.teleport;

import net.minecraft.nbt.CompoundNBT;

public class PlayerTeleportUnlock {
     private int[] teleportUnlockedList = {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//     private int[] teleportUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
     private boolean homeUnlocked = false;
     private boolean clubHomeUnlocked = false;

     public int[] getTeleportUnlockedList() {
         return teleportUnlockedList;
     }

     public int getTeleportUnlocked(int townIndex) {
         return teleportUnlockedList[townIndex];
     }

     public boolean getHomeUnlocked() {
         return homeUnlocked;
     }

     public boolean getClubHomeUnlocked() {
         return clubHomeUnlocked;
     }

     public void unlockTeleport(int teleportIndex) {
         this.teleportUnlockedList[teleportIndex] =  1;
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
         nbt.putIntArray("town_unlocked_list", teleportUnlockedList);
         nbt.putBoolean("home_unlocked", homeUnlocked);
         nbt.putBoolean("club_home_unlocked", clubHomeUnlocked);
     }

     public void loadNBTData(CompoundNBT nbt) {
         teleportUnlockedList = nbt.getIntArray("town_unlocked_list").clone();
         homeUnlocked = nbt.getBoolean("home_unlocked");
         clubHomeUnlocked = nbt.getBoolean("club_home_unlocked");
     }
}
