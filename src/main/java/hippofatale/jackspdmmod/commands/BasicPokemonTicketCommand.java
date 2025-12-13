package hippofatale.jackspdmmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.api.pokemon.requirement.impl.SpeciesRequirement;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.api.pokemon.InitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class BasicPokemonTicketCommand extends PixelCommand {
    public BasicPokemonTicketCommand(CommandDispatcher<CommandSource> dispatcher) {
        super(dispatcher, "일반선택권", "/일반선택권 [포켓몬]", 0);
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws CommandException, CommandSyntaxException {
        if (args.length >= 1) {
            GameProfile profile = PixelmonCommandUtils.requireEntityPlayer(sender).getGameProfile();
            ServerPlayerEntity player = PixelmonCommandUtils.getEntityPlayer(profile.getId());
            PlayerPartyStorage pps = StorageProxy.getParty(profile.getId());
            PokemonSpecification spec = PokemonSpecificationProxy.create(args);
            if (!spec.getValue(SpeciesRequirement.class).isPresent()) {
                PixelmonCommandUtils.endCommand("pixelmon.command.general.invalid", new Object[0]);
            }

            if (!player.getMainHandItem().sameItem(ModItems.BASIC_SELECTION_TICKET.get().getDefaultInstance())) {
                PixelmonCommandUtils.endCommand("message.jackspdmmod.does_not_have_item", new Object[0]);
            }

            //create pokemon from args spec
            Pokemon pokemon = spec.create();
            Stats stats = pokemon.getForm();

            //init
            pokemon.setSpecies(stats.getBaseEvolution(), true);
            pokemon.initialize(InitializeCategory.INTRINSIC_FORCEFUL);

            //set data
            pokemon.setLevel(5);
            pokemon.setForm(stats);

            if (pokemon.isLegendary()) {
                PixelmonCommandUtils.endCommand("message.jackspdmmod.legendary_not_allowed", new Object[0]);
            }

            if (player != null && Pixelmon.EVENT_BUS.post(new PokemonReceivedEvent(player, pokemon, "Command"))) {
                return;
            }

            if (player != null && BattleRegistry.getBattle(player) != null) {
                StorageProxy.getPCForPlayer(profile.getId()).add(pokemon);
            } else {
                StorageProxy.getParty(profile.getId()).add(pokemon);
            }

            if (!pokemon.isEgg()) {
                PokedexEvent.Pre preEvent = new PokedexEvent.Pre(pps.uuid, pokemon, PokedexRegistrationStatus.CAUGHT, "commandGiven");
                if (!Pixelmon.EVENT_BUS.post(preEvent)) {
                    pps.playerPokedex.set(preEvent.getPokemon(), preEvent.getNewStatus());
                    pps.playerPokedex.update();
                    Pixelmon.EVENT_BUS.post(new PokedexEvent.Post(player.getUUID(), preEvent.getOldStatus(), preEvent.getPokemon(), preEvent.getNewStatus(), preEvent.getCause()));
                }
            }
            player.getMainHandItem().shrink(1);
            PixelmonCommandUtils.sendMessage(sender, "pixelmon.command.give.givesuccess" + (pokemon.isEgg() ? "egg" : ""), new Object[]{profile.getName(), pokemon.getSpecies().getTranslatedName()});
            PixelmonCommandUtils.notifyCommandListener(sender, this, 0, "pixelmon.command.give.notifygive" + (pokemon.isEgg() ? "egg" : ""), new Object[]{sender.getTextName(), profile.getName(), pokemon.getSpecies().getTranslatedName()});
        } else {
            sender.sendSuccess(PixelmonCommandUtils.format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]), false);
            PixelmonCommandUtils.endCommand(this.getUsage(sender), new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) throws CommandSyntaxException {
        List<String> pokemon = PixelmonCommandUtils.tabCompletePokemon();
        switch (args.length) {
            case 0:
                return super.getTabCompletions(server, sender, args, pos);
            case 1:
                pokemon.add("random");
                return pokemon;
            default:
                return PixelmonCommandUtils.SPEC_REQUIREMENTS;
        }
    }
}
