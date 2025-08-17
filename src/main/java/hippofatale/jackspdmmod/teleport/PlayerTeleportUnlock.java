package hippofatale.jackspdmmod.teleport;

import net.minecraft.nbt.CompoundNBT;

public class PlayerTeleportUnlock {
     private int[] townUnlockedList = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//     private int[] townUnlockedList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
     private boolean homeUnlocked = false;
     private boolean clubHomeUnlocked = false;

     public int[] getTownUnlockedList() {
         return townUnlockedList;
     }

     public int getTownUnlocked(int townIndex) {
         return townUnlockedList[townIndex];
     }

     public boolean getHomeUnlocked() {
         return homeUnlocked;
     }

     public boolean getClubHomeUnlocked() {
         return clubHomeUnlocked;
     }

     public void unlockTown(int townIndex) {
         this.townUnlockedList[townIndex] =  1;
     }

     public void setHomeUnlocked(boolean isUnlocked) {
         this.homeUnlocked = isUnlocked;
     }

     public void setClubHomeUnlocked(boolean isUnlocked) {
         this.clubHomeUnlocked = isUnlocked;
     }

     public void copyFrom(PlayerTeleportUnlock source) {
         this.townUnlockedList = source.townUnlockedList.clone();
         this.homeUnlocked = source.homeUnlocked;
         this.clubHomeUnlocked = source.clubHomeUnlocked;
     }

     public void saveNBTData(CompoundNBT nbt) {
         nbt.putIntArray("town_unlocked_list", townUnlockedList);
         nbt.putBoolean("home_unlocked", homeUnlocked);
         nbt.putBoolean("club_home_unlocked", clubHomeUnlocked);
     }

     public void loadNBTData(CompoundNBT nbt) {
         townUnlockedList = nbt.getIntArray("town_unlocked_list").clone();
         homeUnlocked = nbt.getBoolean("home_unlocked");
         clubHomeUnlocked = nbt.getBoolean("club_home_unlocked");
     }
}
