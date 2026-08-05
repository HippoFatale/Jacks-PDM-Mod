package me.hippofatale.jackspdmmod.minigames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MiniGameManager {
    public static boolean isMiniGameHalted = false;
    public static boolean isMiniGameOpen = false;
    public static boolean isMiniGameRunning = false;
    public static MiniGameType miniGameType = MiniGameType.DICE_OF_FORTUNE;
    public static final List<UUID> miniGameApplicants = new ArrayList<>();
    public static final Map<Integer, UUID> diceOfFortune = new HashMap<>();
    public static final List<Integer> diceNumbers = new ArrayList<>();

    // casino
    public static boolean isCasinoOpen = false;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "jackspdmmod/minigame.json");
    public static void save() {
        try {
            File parentDir = FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(FILE)) {
                MiniGameSaveData data = new MiniGameSaveData();
                data.isMiniGameHalted = isMiniGameHalted;
                data.isCasinoOpen = isCasinoOpen;
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("미니게임 데이터를 저장하는 중 오류가 발생했습니다!", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) {
            isMiniGameHalted = false;
            isCasinoOpen = false;
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            MiniGameSaveData data = GSON.fromJson(reader, MiniGameSaveData.class);
            if (data != null) {
                isMiniGameHalted = data.isMiniGameHalted;
                isCasinoOpen = data.isCasinoOpen;
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("미니게임 데이터를 로드하는 중 오류가 발생했습니다!", e);
            isMiniGameHalted = false;
            isCasinoOpen = false;
        }
    }

    private static class MiniGameSaveData {
        boolean isMiniGameHalted;
        boolean isCasinoOpen;
    }
}
