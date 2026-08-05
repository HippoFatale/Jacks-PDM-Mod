package me.hippofatale.jackspdmmod.rank;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerRankPointProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    @CapabilityInject(PlayerRankPoint.class)
    public static Capability<PlayerRankPoint> PLAYER_RANK_POINT = null;

    private PlayerRankPoint playerRankPoint = null;
    private final LazyOptional<PlayerRankPoint> optional = LazyOptional.of(this::createPlayerRankPoint);

    private PlayerRankPoint createPlayerRankPoint() {
        if (this.playerRankPoint == null) {
            this.playerRankPoint = new PlayerRankPoint();
        }
        return this.playerRankPoint;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_RANK_POINT) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        createPlayerRankPoint().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        createPlayerRankPoint().loadNBTData(nbt);
    }
}
