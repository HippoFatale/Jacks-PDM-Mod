package me.hippofatale.jackspdmmod.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientRankPointData {
    private static final Map<UUID, Integer> playerRankPoints = new HashMap<>();

    public static int getRankPoints(UUID playerUUID) {
        return playerRankPoints.getOrDefault(playerUUID, 0);
    }

    public static void setRankPoints(UUID playerUUID, int points) {
        playerRankPoints.put(playerUUID, points);
    }

    public static void removePlayer(UUID playerUUID) {
        playerRankPoints.remove(playerUUID);
    }
}
