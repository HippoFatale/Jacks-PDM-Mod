package me.hippofatale.jackspdmmod.home;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "jackspdmmod_homes.json");

    public static final Map<UUID, Home> personalHomes = new HashMap<>();
    public static final Map<Club, Home> clubHomes = new HashMap<>();

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            HomeSaveData data = new HomeSaveData();
            data.personal = personalHomes;

            data.clubRawMap = new HashMap<>();
            for (Map.Entry<Club, Home> entry : clubHomes.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    data.clubRawMap.put(entry.getKey().getClubName(), entry.getValue());
                }
            }

            GSON.toJson(data, writer);
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("하우징 데이터를 저장하는 중 오류가 발생했습니다!", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) {
            personalHomes.clear();
            clubHomes.clear();
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            HomeSaveData data = GSON.fromJson(reader, HomeSaveData.class);

            if (data != null) {
                personalHomes.clear();
                if (data.personal != null) {
                    personalHomes.putAll(data.personal);
                    for (Home home : personalHomes.values()) {
                        rebuildHomeArea(home);
                    }
                }

                clubHomes.clear();
                if (data.clubRawMap != null) {
                    for (Map.Entry<String, Home> entry : data.clubRawMap.entrySet()) {
                        String clubName = entry.getKey();
                        Home home = entry.getValue();

                        Club actualClub = null;
                        for (Club c : ClubManager.clubs) {
                            if (c.getClubName().equals(clubName)) {
                                actualClub = c;
                                break;
                            }
                        }

                        if (actualClub != null) {
                            rebuildHomeArea(home);
                            clubHomes.put(actualClub, home);
                        }
                    }
                }
            }
        } catch (IOException e) {
            JacksPDMMod.LOGGER.error("하우징 데이터를 로드하는 중 오류가 발생했습니다!", e);
        }
    }

    private static void rebuildHomeArea(Home home) {
        try {
            Field homeAreaField = Home.class.getDeclaredField("homeArea");
            homeAreaField.setAccessible(true);
            AxisAlignedBB aabb = new AxisAlignedBB(home.getStartPos(), home.getEndPos()).expandTowards(1, 1, 1);
            homeAreaField.set(home, aabb);
        } catch (Exception e) {
            JacksPDMMod.LOGGER.error("하우징 AABB 범위를 복원하는 중 오류 발생!", e);
        }
    }

    public static List<Home> getSharedHomesForPlayer(UUID playerUUID) {
        List<Home> sharedHomes = new ArrayList<>();
        for (Map.Entry<UUID, Home> entry : personalHomes.entrySet()) {
            Home home = entry.getValue();
            if (home.getSharedPlayers().contains(playerUUID)) {
                sharedHomes.add(home);
            }
        }
        return sharedHomes;
    }

    private static class HomeSaveData {
        Map<UUID, Home> personal;
        Map<String, Home> clubRawMap;
    }

}
