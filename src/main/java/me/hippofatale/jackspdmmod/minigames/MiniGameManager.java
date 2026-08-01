package me.hippofatale.jackspdmmod.minigames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MiniGameManager {
    public static boolean isMiniGameOpen = false;
    public static boolean isMiniGameRunning = false;
    public static MiniGameType miniGameType = MiniGameType.DICE_OF_FORTUNE;
    public static final List<UUID> miniGameApplicants = new ArrayList<>();
    public static final Map<Integer, UUID> diceOfFortune = new HashMap<>();
    public static final List<Integer> diceNumbers = new ArrayList<>();

    // casino
    public static boolean isCasinoOpen = false;
}
