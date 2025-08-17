package hippofatale.jackspdmmod.storage;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class PlayerStorageInventory extends Inventory {
    public PlayerStorageInventory() {super(54);}

    public PlayerStorageInventory getStorageInventory() {
        return this;
    }

    public void saveNBTData(CompoundNBT nbt) {
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putByte("Slot", (byte)i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        nbt.put("storage_inventory", listnbt);
    }

    public void loadNBTData(CompoundNBT nbt) {
        ListNBT p_70486_1_ = (ListNBT) nbt.get("storage_inventory");
        for(int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for(int k = 0; k < p_70486_1_.size(); ++k) {
            CompoundNBT compoundnbt = p_70486_1_.getCompound(k);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.of(compoundnbt));
            }
        }

    }

    public void fromTag(ListNBT p_70486_1_) {
        for(int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for(int k = 0; k < p_70486_1_.size(); ++k) {
            CompoundNBT compoundnbt = p_70486_1_.getCompound(k);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.of(compoundnbt));
            }
        }

    }

    public ListNBT createTag() {
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putByte("Slot", (byte)i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        return listnbt;
    }
}
