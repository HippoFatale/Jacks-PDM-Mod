package me.hippofatale.jackspdmmod.client;

import me.hippofatale.jackspdmmod.teleport.TeleportManager;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public class ClientTeleportData {
    private static boolean[] teleportUnlockedList;
    private static boolean homeUnlocked = false;
    private static boolean clubHomeUnlocked = false;

    static {
        teleportUnlockedList = new boolean[TeleportManager.getTeleportCount()];
        for (int i = 0; i < teleportUnlockedList.length; i++) {
            teleportUnlockedList[i] = TeleportManager.isOpenByDefault(i);
        }
    }

    public static boolean isTeleportUnlocked(int townIndex) {
        return teleportUnlockedList[townIndex];
    }

    public static boolean isHomeUnlocked() {
        return homeUnlocked;
    }

    public static boolean isClubHomeUnlocked() {
        return clubHomeUnlocked;
    }

    public static void unlockTeleport(int townIndex) {
        teleportUnlockedList[townIndex] = true;
    }

    public static void setTeleportUnlockedList(boolean[] unlockedList) {
        teleportUnlockedList = unlockedList.clone();
    }

    public static void setHomeUnlocked(boolean isUnlocked) {
        homeUnlocked = isUnlocked;
    }

    public static void setClubHomeUnlocked(boolean isUnlocked) {
        clubHomeUnlocked = isUnlocked;
    }

    public static ITextComponent getTeleportName(int townIndex) {
        return TeleportManager.getTeleportName(townIndex);
    }

    public static Vector3d getTownCoordinates(int townIndex) {
        return TeleportManager.getTeleportPos(townIndex);
    }

    public static Vector3d getTeleportCoordinates(int townIndex) {
        return TeleportManager.getTeleportPos(townIndex);
    }
}
