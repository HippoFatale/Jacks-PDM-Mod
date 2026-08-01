package me.hippofatale.jackspdmmod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup JACKS_PDM_GROUP =  new ItemGroup("jackspdmmodtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.LEGENDARY_SPAWN_GACHA.get());
        }
    };
}
