package me.hippofatale.jackspdmmod.title;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerTitleProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    @CapabilityInject(PlayerTitle.class)
    public static Capability<PlayerTitle> PLAYER_TITLE = null;

    private PlayerTitle playerTitle = null;
    private final LazyOptional<PlayerTitle> optional = LazyOptional.of(this::createPlayerTitle);

    private PlayerTitle createPlayerTitle() {
        if (this.playerTitle == null) {
            this.playerTitle = new PlayerTitle();
        }
        return this.playerTitle;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_TITLE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        createPlayerTitle().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        createPlayerTitle().loadNBTData(nbt);
    }
}
