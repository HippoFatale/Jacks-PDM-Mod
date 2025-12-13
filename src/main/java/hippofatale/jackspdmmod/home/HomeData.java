package hippofatale.jackspdmmod.home;

import hippofatale.jackspdmmod.club.ClubData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class HomeData {


    public static void saveHomeData() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(HOMES_FILE.toPath()))) {
            out.writeObject(personalHomes);
            out.writeObject(clubNameHomes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadHomeData() {
        if (!HOMES_FILE.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(HOMES_FILE.toPath()))) {
            Map<UUID, Home> loadedPersonalHomes = (Map<UUID, Home>) in.readObject();
            Map<String, Home> loadedClubNameHomes = (Map<String, Home>) in.readObject();
            personalHomes.putAll(loadedPersonalHomes);
            clubNameHomes.putAll(loadedClubNameHomes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Home getOwningPersonalHome(UUID playerUUID) {
        return personalHomes.get(playerUUID);
    }

    public static Home getHomeFromBelongingClubName(UUID playerUUID) {
        return clubNameHomes.get(ClubData.getBelongingClub(playerUUID).getClubName());
    }

    public static boolean removePersonalHomeData(UUID playerUUID) {
        if (personalHomes.containsKey(playerUUID)) {
            personalHomes.remove(playerUUID);
            saveHomeData();
            return true;
        }
        return false;
    }

    public static boolean removeClubHomeData(String clubName) {
        if (clubNameHomes.containsKey(clubName)) {
            clubNameHomes.remove(clubName);
            saveHomeData();
            return true;
        }
        return false;
    }
}
