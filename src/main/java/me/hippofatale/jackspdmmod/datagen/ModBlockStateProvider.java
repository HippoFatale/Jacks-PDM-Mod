package me.hippofatale.jackspdmmod.datagen;

import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, JacksPDMMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ORE_SEED.get(), cubeAll(Blocks.BEDROCK));
        simpleBlockItem(ModBlocks.ORE_SEED.get(), cubeAll(Blocks.BEDROCK));
        //horizontalBlock(ModBlocks.PERSONAL_SMALL_PLACARD_BLOCK.get(), models().);
    }
}
