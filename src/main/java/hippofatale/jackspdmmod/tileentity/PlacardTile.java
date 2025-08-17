package hippofatale.jackspdmmod.tileentity;

import hippofatale.jackspdmmod.home.HomeSize;
import hippofatale.jackspdmmod.home.HomeType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

import java.math.BigDecimal;

public class PlacardTile extends SignTileEntity {
    private boolean purchased = false;
    private HomeType plotType;
    private HomeSize plotSize;
    private BigDecimal plotPrice;
    private int rotation;
    private BlockPos pivot;
    private Vector3d teleportPoint;

    public PlacardTile() {
        super();
    }

    @Override
    public TileEntityType<?> getType() {
        return ModTileEntities.PLACARD_TILE.get();
    }

    public boolean getPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public HomeType getPlotType() {
        return plotType;
    }

    public void setPlotType(HomeType plotType) {
        this.plotType = plotType;
    }

    public HomeSize getPlotSize() {
        return plotSize;
    }

    public void setPlotSize(HomeSize plotSize) {
        this.plotSize = plotSize;
    }

    public BigDecimal getPlotPrice() {
        return plotPrice;
    }

    public void setPlotPrice(BigDecimal plotPrice) {
        this.plotPrice = plotPrice;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        switch (rotation) {
            case 0: {
                this.pivot = this.getBlockPos().offset(0, 0, -this.plotSize.size().getZ() - 2);
                BlockPos teleportBlockPos = this.getBlockPos().offset(0, 0, 2);
                this.teleportPoint = new Vector3d(teleportBlockPos.getX(), teleportBlockPos.getY(), teleportBlockPos.getZ());
                break;
            }
            case 4: {
                this.pivot = this.getBlockPos().offset(2, 0, 0);
                BlockPos teleportBlockPos = this.getBlockPos().offset(-2, 0, 0);
                this.teleportPoint = new Vector3d(teleportBlockPos.getX(), teleportBlockPos.getY(), teleportBlockPos.getZ());
                break;
            }
            case 8: {
                this.pivot = this.getBlockPos().offset(-this.plotSize.size().getX(), 0, 2);
                BlockPos teleportBlockPos = this.getBlockPos().offset(0, 0, -2);
                this.teleportPoint = new Vector3d(teleportBlockPos.getX(), teleportBlockPos.getY(), teleportBlockPos.getZ());
                break;
            }
            case 12: {
                this.pivot = this.getBlockPos().offset(-this.plotSize.size().getX() - 2, 0, -this.plotSize.size().getZ());
                BlockPos teleportBlockPos = this.getBlockPos().offset(2, 0, 0);
                this.teleportPoint = new Vector3d(teleportBlockPos.getX(), teleportBlockPos.getY(), teleportBlockPos.getZ());
                break;
            }
        }


    }

    public BlockPos getPivot() {
        return pivot;
    }

    public void setPivot(BlockPos pivotPos) {
        this.pivot = pivotPos;
    }

    public Vector3d getTeleportPoint() {
        return teleportPoint;
    }

    public void setTeleportPoint(Vector3d teleportPoint) {
        this.teleportPoint = teleportPoint;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("purchased", purchased);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        purchased = nbt.getBoolean("purchased");
        super.load(blockState, nbt);
    }
}
