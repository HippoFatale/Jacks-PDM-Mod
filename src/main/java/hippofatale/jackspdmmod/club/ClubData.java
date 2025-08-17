package hippofatale.jackspdmmod.club;

import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class ClubData {
    public static void saveClubData() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(CLUBS_FILE.toPath()))) {
            out.writeObject(clubs);
            out.writeObject(playerClubs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadClubData() {
        if (!CLUBS_FILE.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(CLUBS_FILE.toPath()))) {
            Map<String, Club> loadedClubs = (Map<String, Club>) in.readObject();
            Map<UUID, String> loadedPlayerClubs = (Map<UUID, String>) in.readObject();
            clubs.putAll(loadedClubs);
            playerClubs.putAll(loadedPlayerClubs);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Club getBelongingClub(UUID playerUUID) {
        return clubs.get(playerClubs.get(playerUUID));
    }

    public static boolean arePlayersInSameClub(PlayerEntity player1, PlayerEntity player2) {
        String club1 = playerClubs.get(player1.getUUID());
        String club2 = playerClubs.get(player2.getUUID());
        return club1 != null && club1.equals(club2);
    }
}
