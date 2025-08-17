package hippofatale.jackspdmmod.tileentity;

import hippofatale.jackspdmmod.JacksPDMMod;
import hippofatale.jackspdmmod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JacksPDMMod.MOD_ID);

    public static RegistryObject<TileEntityType<OreSeedTile>> ORE_SEED_TILE = TILE_ENTITIES.register("ore_seed_tile",
            () -> TileEntityType.Builder.of(OreSeedTile::new, ModBlocks.ORE_SEED.get()).build(null));

    public static RegistryObject<TileEntityType<PlacardTile>> PLACARD_TILE = TILE_ENTITIES.register("placard_tile",
            () -> TileEntityType.Builder.of(PlacardTile::new,
                    ModBlocks.PERSONAL_SMALL_PLACARD_BLOCK.get(),
                    ModBlocks.PERSONAL_MEDIUM_PLACARD_BLOCK.get(),
                    ModBlocks.CLUB_LARGE_PLACARD_BLOCK.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
