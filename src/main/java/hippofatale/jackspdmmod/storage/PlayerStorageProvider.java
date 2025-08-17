package hippofatale.jackspdmmod.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerStorageProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    @CapabilityInject(PlayerStorageInventory.class)
    public static Capability<PlayerStorageInventory> PLAYER_STORAGE = null;



    private PlayerStorageInventory playerStorage = null;
    private final LazyOptional<PlayerStorageInventory> optional = LazyOptional.of(this::createStorageInventory);

    private PlayerStorageInventory createStorageInventory() {
        if (this.playerStorage == null) {
            this.playerStorage = new PlayerStorageInventory();
        }
        return this.playerStorage;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_STORAGE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        createStorageInventory().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        createStorageInventory().loadNBTData(nbt);
    }
}
