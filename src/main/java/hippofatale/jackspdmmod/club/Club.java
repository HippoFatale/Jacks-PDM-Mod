package hippofatale.jackspdmmod.club;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Club implements Serializable {
    private String clubName;
    private UUID presidentUUID;
    private String presidentName;
    private Map<UUID, String> members;
    private int maxMembers;
    private int clubPoints;

    public Club(String clubName, UUID presidentUUID, String presidentName) {
        this.clubName = clubName;
        this.presidentUUID = presidentUUID;
        this.presidentName = presidentName;
        this.members = new HashMap<>();
        this.members.put(presidentUUID, presidentName);
        this.maxMembers = 5;
        this.clubPoints = 0;
        ClubData.saveClubData();
    }

    public String getClubName() {
        return clubName;
    }

    public boolean isPresident(UUID uuid) {
        return presidentUUID.equals(uuid);
    }

    public String getPresidentName() {
        return presidentName;
    }

    public void setPresident(UUID uuid, String name) {
        this.presidentUUID = uuid;
        this.presidentName = name;
        ClubData.saveClubData();
    }

    public Map<UUID, String> getMembers() {
        return members;
    }

    public boolean isFull() {
        return members.size() >= maxMembers;
    }

    public void addMember(UUID playerUUID, String playerName) {
        members.put(playerUUID, playerName);
        ClubData.saveClubData();
    }

    public void removeMember(UUID playerUUID) {
        members.remove(playerUUID);
        ClubData.saveClubData();
    }

    public int getClubPoints() {
        return clubPoints;
    }

    public void addClubPoints(int amount) {
        clubPoints = clubPoints + amount;
        ClubData.saveClubData();
    }

    public void takeClubPoints(int amount) {
        clubPoints = clubPoints - amount;
        ClubData.saveClubData();
    }
}
