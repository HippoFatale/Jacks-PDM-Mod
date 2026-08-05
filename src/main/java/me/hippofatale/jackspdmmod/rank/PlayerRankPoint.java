package me.hippofatale.jackspdmmod.rank;

import net.minecraft.nbt.CompoundNBT;

public class PlayerRankPoint {
    public static final int MIN_POINTS = 0;

    private int rankPoints = 0;

    public PlayerRankPoint() {
    }

    public int getRankPoints() {
        return this.rankPoints;
    }

    public void setRankPoints(int points) {
        this.rankPoints = Math.max(points, MIN_POINTS);
    }

    public void addRankPoints(int points) {
        this.rankPoints = Math.max(this.rankPoints + points, MIN_POINTS);
    }

    public void removeRankPoints(int points) {
        this.rankPoints = Math.max(this.rankPoints - points, MIN_POINTS);
    }

    public void copyFrom(PlayerRankPoint source) {
        this.rankPoints = source.rankPoints;
    }

    public void saveNBTData(CompoundNBT nbt) {
        nbt.putInt("rank_points", rankPoints);
    }

    public void loadNBTData(CompoundNBT nbt) {
        this.rankPoints = nbt.getInt("rank_points");
    }
}
