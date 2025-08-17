package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.CollectionHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
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
            PokemonSpecification spec = PokemonSpecificationProxy.create();
            Pokemon pokemon = spec.create();
        switch (speciesType) {
            case ('L'): {
                pokemon.setSpecies(getRandomLegendary(), true);
                break;
            }
            case ('M'): {
                pokemon.setSpecies(getRandomMythical(), true);
                break;
            }
            case ('D'): {
                pokemon.setSpecies(getRandomDigimon(), true);
                break;
            }
        }
            PixelmonEntity entity = PokemonSpecificationProxy.create().create(world);
            entity.setPokemon(pokemon);
            BlockPos pos = new BlockPos(player.position());
            entity.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            entity.canDespawn = false;
            entity.setSpawnLocation(entity.getDefaultSpawnLocation());
            world.addFreshEntity(entity);
            entity.resetAI();
        }
        return super.use(world, player, hand);
    }

    public static Species getRandomDigimon() {
        Set<Integer> species = new HashSet<>();
        species.add(2002);
        species.add(2155);
        species.add(2111);
        species.add(2003);
        species.add(2112);
        species.add(2091);
        species.add(2052);
        species.add(2008);
        species.add(2154);
        species.add(2005);
        species.add(2001);
        species.add(2174);
        species.add(2062);
        species.add(2134);
        species.add(2006);
        species.add(2113);
        species.add(2090);
        species.add(2007);
        species.add(2004);
        species.add(2153);
        species.add(2135);
        species.add(2069);
        species.add(2079);
        species.add(2148);
        species.add(2073);
        species.add(2204);
        species.add(2200);
        species.add(2193);
        species.add(2186);
        species.add(2211);
        return (Species) fromDex((Integer) CollectionHelper.getRandomElement(species)).orElse(null);
    }

    public static Species getRandomMythical() {
        Set<Integer> species = new IntOpenHashSet(PixelmonSpecies.getMythicals());
        species.removeAll(PixelmonSpecies.getGenerationDex(20));
        species.remove(PixelmonSpecies.ARCEUS.getValueUnsafe().getDex());
        return (Species) fromDex((Integer) CollectionHelper.getRandomElement(species)).orElse(null);
    }

    public static Species getRandomLegendary() {
        Set<Integer> species = new IntOpenHashSet(PixelmonSpecies.getLegendaries(true));
        species.removeAll(PixelmonSpecies.getGenerationDex(20));
        return (Species) fromDex((Integer) CollectionHelper.getRandomElement(species)).orElse(null);
    }
}
