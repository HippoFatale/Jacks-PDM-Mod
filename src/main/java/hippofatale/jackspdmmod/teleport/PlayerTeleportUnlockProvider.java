package hippofatale.jackspdmmod.teleport;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerTeleportUnlockProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    @CapabilityInject(PlayerTeleportUnlock.class)
    public static Capability<PlayerTeleportUnlock> PLAYER_TELEPORT_UNLOCK = null;

    private PlayerTeleportUnlock teleportUnlock = null;
    private final LazyOptional<PlayerTeleportUnlock> optional = LazyOptional.of(this::createPlayerTeleportUnlock);

    private PlayerTeleportUnlock createPlayerTeleportUnlock() {
        if (this.teleportUnlock == null) {
            this.teleportUnlock = new PlayerTeleportUnlock();
        }
        return this.teleportUnlock;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_TELEPORT_UNLOCK) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        createPlayerTeleportUnlock().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        createPlayerTeleportUnlock().loadNBTData(nbt);
    }
}
