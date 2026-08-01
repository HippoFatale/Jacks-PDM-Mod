package me.hippofatale.jackspdmmod.tileentity;

import me.hippofatale.jackspdmmod.home.HomeSize;
import me.hippofatale.jackspdmmod.home.HomeType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.math.BigDecimal;

public class PlacardTile extends SignTileEntity {
    private boolean purchased;
    private HomeType plotType;
    private HomeSize plotSize;
    private BlockPos pivot;
    private BlockPos teleportPos;

    public PlacardTile() {
        super();
        this.purchased = false;
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
        return this.plotSize.getPrice();
    }

    public void setRotation(int rotation) {
        switch (rotation) {
            case 0: {
                this.pivot = this.getBlockPos().offset(0, 0, -this.plotSize.getSize().getZ() - 2);
                this.teleportPos = this.getBlockPos().offset(0, 0, 2);
                break;
            }
            case 4: {
                this.pivot = this.getBlockPos().offset(2, 0, 0);
                this.teleportPos = this.getBlockPos().offset(-2, 0, 0);
                break;
            }
            case 8: {
                this.pivot = this.getBlockPos().offset(-this.plotSize.getSize().getX(), 0, 2);
                this.teleportPos = this.getBlockPos().offset(0, 0, -2);
                break;
            }
            case 12: {
                this.pivot = this.getBlockPos().offset(-this.plotSize.getSize().getX() - 2, 0, -this.plotSize.getSize().getZ());
                this.teleportPos = this.getBlockPos().offset(2, 0, 0);
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

    public BlockPos getTeleportPos() {
        return teleportPos;
    }

    public void setTeleportPos(BlockPos teleportPoint) {
        this.teleportPos = teleportPos;
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
