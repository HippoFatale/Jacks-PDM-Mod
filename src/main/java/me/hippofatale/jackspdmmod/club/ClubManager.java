package me.hippofatale.jackspdmmod.club;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leangen.geantyref.TypeToken;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class ClubManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "jackspdmmod/clubs.json");

    public static List<Club> clubs = new ArrayList<>();
    public static final Map<UUID, Club> belongingClubs = new HashMap<>();
    public static final Map<UUID, List<Club>> pendingInvites = new HashMap<>();

    public static void save() {
        try {
            File parentDir = FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(FILE)) {
                GSON.toJson(clubs, writer);
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("길드 데이터를 저장하는 중 오류가 발생했습니다!", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) {
            clubs = new ArrayList<>();
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            Type type = new TypeToken<List<Club>>(){}.getType();
            List<Club> loadedClubs = GSON.fromJson(reader, type);

            if (loadedClubs != null) {
                clubs = loadedClubs;

                belongingClubs.clear();
                for (Club club : clubs) {
                    club.ensureValidData();

                    for (UUID memberUUID : club.getMembers()) {
                        belongingClubs.put(memberUUID, club);
                    }
                }
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("길드 데이터를 로드하는 중 오류가 발생했습니다!", e);
            clubs = new ArrayList<>();
        }
    }

    public static Club getClub(String clubName) {
        for (Club club : clubs) {
            if (club.getClubName().equals(clubName)) {
                return club;
            }
        }
        return null;
    }
}
