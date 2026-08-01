package me.hippofatale.jackspdmmod.datagen;

import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, JacksPDMMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.LEGENDARY_SPAWN_GACHA.get());
        basicItem(ModItems.MYTHICAL_SPAWN_GACHA.get());
        basicItem(ModItems.DIGIMON_SPAWN_GACHA.get());
        basicItem(ModItems.STARTER_PACKAGE.get());
    }

    private ItemModelBuilder basicItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(JacksPDMMod.MOD_ID, "item/" + item.getRegistryName().getPath()));
    }
}
