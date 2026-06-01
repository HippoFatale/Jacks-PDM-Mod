package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.util.helpers.CollectionHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import hippofatale.jackspdmmod.item.ModItems;
import hippofatale.jackspdmmod.util.GachaLists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Set;

import static com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies.fromDex;

public class GachaSpawnItem extends Item {
    private char speciesType;
    public GachaSpawnItem(Properties properties, char speciesType) {
        super(properties);
        this.speciesType = speciesType;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            player.getItemInHand(hand).shrink(1);
            ITextComponent spawnGachaName = null;
            PokemonSpecification spec = PokemonSpecificationProxy.create();
            Pokemon pokemon = spec.create();
            ITextComponent spawnedName = null;
            switch (speciesType) {
                case 'L':
                    spawnGachaName = (new ItemStack(ModItems.LEGENDARY_SPAWN_GACHA.get())).getHoverName().copy().withStyle(TextFormatting.GOLD);
                    pokemon.setSpecies(getSpawnGachaResult(GachaLists.getLegendarySpawnGacha()), true);
                    spawnedName = pokemon.getTranslatedName().withStyle(TextFormatting.YELLOW);
                    break;
                case 'M':
                    spawnGachaName = (new ItemStack(ModItems.MYTHICAL_SPAWN_GACHA.get())).getHoverName().copy().withStyle(TextFormatting.LIGHT_PURPLE);
                    pokemon.setSpecies(getSpawnGachaResult(GachaLists.getMythicalSpawnGacha()), true);
                    spawnedName = pokemon.getFormattedDisplayName().copy().withStyle(TextFormatting.YELLOW);
                    break;
                case 'D':
                    spawnGachaName = (new ItemStack(ModItems.DIGIMON_SPAWN_GACHA.get())).getHoverName().copy().withStyle(TextFormatting.BLUE);
                    pokemon.setSpecies(getSpawnGachaResult(GachaLists.getDigimonSpawnGacha()), true);
                    spawnedName = pokemon.getDisplayNameWithRibbon().copy().withStyle(TextFormatting.YELLOW);
                    break;
                default:
                    return super.use(world, player, hand);
            }
            PixelmonEntity entity = PokemonSpecificationProxy.create().create(world);
            entity.setPokemon(pokemon);
            BlockPos pos = new BlockPos(player.position());
            entity.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            entity.canDespawn = false;
            entity.setSpawnLocation(entity.getDefaultSpawnLocation());
            world.addFreshEntity(entity);
            entity.resetAI();
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.spawn_gacha_result",
                    spawnGachaName.copy().withStyle(TextFormatting.BOLD), spawnedName.copy().withStyle(TextFormatting.BOLD)), false);
        }
        return super.use(world, player, hand);
    }

    private Species getSpawnGachaResult(Set<Integer> species) {
        return (Species) fromDex((Integer) CollectionHelper.getRandomElement(species)).orElse(null);
    }
}
