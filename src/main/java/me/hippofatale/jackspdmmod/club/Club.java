package me.hippofatale.jackspdmmod.club;

import java.util.*;

public class Club {
    private static final int DEFAULT_MAX_MEMBERS = 5;

    private String clubName;
    private UUID president;
    private List<UUID> members;
    private int maxMembers;
    private int clubPoints;

    public Club(String clubName, UUID president) {
        this.clubName = clubName;
        this.president = president;
        this.members = new ArrayList<>();
        this.members.add(president);
        this.maxMembers = DEFAULT_MAX_MEMBERS;
        this.clubPoints = 0;
    }

    public void ensureValidData() {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        if (this.maxMembers <= 0) {
            this.maxMembers = DEFAULT_MAX_MEMBERS;
        }
    }

    public String getClubName() {
        return clubName;
    }

    public boolean isPresident(UUID uuid) {
        return president.equals(uuid);
    }

    public UUID getPresident() {
        return president;
    }

    public void setPresident(UUID uuid) {
        president = uuid;
        ClubManager.save();
    }

    public List<UUID> getMembers() {
        return members;
    }

    public boolean isFull() {
        return members.size() >= maxMembers;
    }

    public void addMember(UUID uuid) {
        if (!this.members.contains(uuid)) {
            this.members.add(uuid);
            ClubManager.save();
        }
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        ClubManager.save();
    }

    public int getClubPoints() {
        return clubPoints;
    }

    public void addClubPoints(int amount) {
        clubPoints += amount;
        ClubManager.save();
    }

    public void takeClubPoints(int amount) {
        clubPoints -= amount;
        ClubManager.save();
    }
}
