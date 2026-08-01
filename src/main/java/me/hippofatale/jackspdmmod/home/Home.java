package me.hippofatale.jackspdmmod.home;

import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.tileentity.PlacardTile;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.*;

public class Home {
    private static final int DEFAULT_MAX_SHARED_PLAYERS = 2;

    private BlockPos placardPos;
    private HomeType homeType;
    private UUID owningPlayer;
    private Club owningClub;
    private HomeSize homeSize;
    private final BlockPos startPos;
    private final BlockPos endPos;
    private final transient AxisAlignedBB homeArea;
    private final BlockPos teleportPos;
    private List<UUID> sharedPlayers;
    private int maxSharedPlayers;

    public Home(PlacardTile placard, UUID playerUUID) {
        this.placardPos = placard.getBlockPos();
        this.homeType = placard.getPlotType();
        this.owningPlayer = playerUUID;

        this.homeSize = placard.getPlotSize();
        this.startPos = placard.getPivot();
        this.endPos = placard.getPivot().offset(homeSize.getSize());
        this.homeArea = new AxisAlignedBB(startPos, endPos).expandTowards(1, 1, 1);

        this.teleportPos = placard.getTeleportPos();
        this.sharedPlayers = new ArrayList<>();
        this.maxSharedPlayers = DEFAULT_MAX_SHARED_PLAYERS;
    }

    public Home(PlacardTile placard, Club club) {
        this.placardPos = placard.getBlockPos();
        this.homeType = placard.getPlotType();
        this.owningClub = club;

        this.homeSize = placard.getPlotSize();
        this.startPos = placard.getPivot();
        this.endPos = placard.getPivot().offset(homeSize.getSize());
        this.homeArea = new AxisAlignedBB(startPos, endPos).expandTowards(1, 1, 1);

        this.teleportPos = placard.getTeleportPos();
        this.sharedPlayers = new ArrayList<>();
        this.maxSharedPlayers = DEFAULT_MAX_SHARED_PLAYERS;
    }

    public Optional<Object> getOwningPlayerOrClub() {
        switch (this.homeType) {
            case PERSONAL:
                return Optional.ofNullable(this.owningPlayer);
            case CLUB:
                return Optional.ofNullable(this.owningClub);
        }
        return Optional.empty();
    }

    public BlockPos getStartPos() {
        return startPos;
    }

    public BlockPos getEndPos() {
        return endPos;
    }

    public boolean isInBounds(BlockPos blockPos) {
        return homeArea.contains(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public BlockPos getTeleportPos () {
        return teleportPos;
    }

    public BlockPos getPlacardPos() {
        return placardPos;
    }

    public List<UUID> getSharedPlayers() {
        return sharedPlayers;
    }

    public void addSharer(UUID playerUUID) {
        sharedPlayers.add(playerUUID);
    }

    public void removeSharer(UUID playerUUID) {
        sharedPlayers.remove(playerUUID);
    }

    public boolean isSharingFull() {
        return sharedPlayers.size() >= maxSharedPlayers;
    }
}
