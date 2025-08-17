package hippofatale.jackspdmmod.home;

import hippofatale.jackspdmmod.club.Club;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.tileentity.PlacardTile;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Home implements Serializable {
    private HomeType homeType;
    private UUID owner;
    private String ownerName;
    private Club owningClub;
    private String owningClubName;
    private HomeSize homeSize;
    private int pivotX;
    private int pivotY;
    private int pivotZ;
    private int lowerBoundsX;
    private int lowerBoundsY;
    private int lowerBoundsZ;
    private int upperBoundsX;
    private int upperBoundsY;
    private int upperBoundsZ;
    private double teleportPointX;
    private double teleportPointY;
    private double teleportPointZ;
    private int placardPosX;
    private int placardPosY;
    private int placardPosZ;
    private Map<UUID, String> sharedPlayers;
    private int maxSharedPlayers;

    public Home(PlacardTile placard, UUID playerUUID, String name) {
        this.placardPosX = placard.getBlockPos().getX();
        this.placardPosY = placard.getBlockPos().getY();
        this.placardPosZ = placard.getBlockPos().getZ();
        this.homeType = placard.getPlotType();
        switch (homeType) {
            case PERSONAL: {
                this.owner = playerUUID;
                this.ownerName = name;
                break;
            }
            case CLUB: {
                Club belongingClub = ClubData.getBelongingClub(playerUUID);
                this.owningClub = belongingClub;
                this.owningClubName = name;
                break;
            }
        }
        this.homeSize = placard.getPlotSize();
        this.pivotX = placard.getPivot().getX();
        this.pivotY = placard.getPivot().getY();
        this.pivotZ = placard.getPivot().getZ();
        this.lowerBoundsX = pivotX;
        this.lowerBoundsY = pivotY - 1;
        this.lowerBoundsZ = pivotZ;
        this.upperBoundsX = pivotX + homeSize.size().getX();
        this.upperBoundsY = pivotY + homeSize.size().getY();
        this.upperBoundsZ = pivotZ + homeSize.size().getZ();
        this.teleportPointX = placard.getTeleportPoint().x();
        this.teleportPointY = placard.getTeleportPoint().y();
        this.teleportPointZ = placard.getTeleportPoint().z();
        this.sharedPlayers = new HashMap<>();
        this.maxSharedPlayers = 2;

        HomeData.saveHomeData();
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwningClubName() {
        return owningClubName;
    }

    public BlockPos getLowerBounds() {
        return new BlockPos(lowerBoundsX, lowerBoundsY, lowerBoundsZ);
    }

    public BlockPos getUpperBounds() {
        return new BlockPos(upperBoundsX, upperBoundsY, upperBoundsZ);
    }

    public boolean isInBounds(BlockPos blockPos) {
        if (blockPos.getX() < lowerBoundsX) {
            return false;
        }
        if (blockPos.getX() > upperBoundsX) {
            return false;
        }
        if (blockPos.getY() < lowerBoundsY) {
            return false;
        }
        if (blockPos.getY() > upperBoundsY) {
            return false;
        }
        if (blockPos.getZ() < lowerBoundsZ) {
            return false;
        }
        if (blockPos.getZ() > upperBoundsZ) {
            return false;
        }
        return true;
    }

    public Vector3d getTeleportPoint() {
        return new Vector3d(teleportPointX, teleportPointY, teleportPointZ);
    }

    public BlockPos getPlacardPos() {
        return new BlockPos(placardPosX, placardPosY, placardPosZ);
    }

    public Map<UUID, String> getSharedPlayers() {
        if (sharedPlayers == null) {
            sharedPlayers = new HashMap<>();
        }
        return sharedPlayers;
    }

    public void addSharer(UUID playerUUID, String playerName) {
        if (sharedPlayers == null) {
            sharedPlayers = new HashMap<>();
        }
        sharedPlayers.put(playerUUID, playerName);
        HomeData.saveHomeData();
    }

    public void removeSharer(UUID playerUUID) {
        if (sharedPlayers == null) {
            sharedPlayers = new HashMap<>();
        }
        sharedPlayers.remove(playerUUID);
        HomeData.saveHomeData();
    }

    public boolean isSharingFull() {
        if (sharedPlayers == null) {
            sharedPlayers = new HashMap<>();
        }
        maxSharedPlayers = 2;

        return sharedPlayers.size() >= maxSharedPlayers;
    }
}
