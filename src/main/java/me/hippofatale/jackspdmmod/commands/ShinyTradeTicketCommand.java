package me.hippofatale.jackspdmmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import me.hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ShinyTradeTicketCommand extends PixelCommand {
    public ShinyTradeTicketCommand(CommandDispatcher<CommandSource> dispatcher) {
        super(dispatcher, "이로치교환권", "/이로치교환권 [번호]", 0);
    }

    public void execute(CommandSource sender, String[] args) throws CommandException {
        if (args.length != 1) {
            sender.sendSuccess(PixelmonCommandUtils.format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]), false);
            PixelmonCommandUtils.endCommand(this.getUsage(sender), new Object[0]);
        }

        GameProfile profile = PixelmonCommandUtils.requireEntityPlayer(sender).getGameProfile();
        ServerPlayerEntity player = PixelmonCommandUtils.getEntityPlayer(profile.getId());
        PlayerPartyStorage storage = StorageProxy.getParty(profile.getId());

        //has item
        if (!player.getMainHandItem().sameItem(ModItems.SHINY_TRADE_TICKET.get().getDefaultInstance())) {
            PixelmonCommandUtils.endCommand("message.jackspdmmod.does_not_have_item", new Object[0]);
        }

        //not at battle
        if (BattleRegistry.getBattle(player) != null) {
            PixelmonCommandUtils.endCommand("message.jackspdmmod.cannot_use_during_battle", new Object[0]);
        }

        int slot = PixelmonCommandUtils.requireInt(args[0], 1, 6, "pixelmon.command.pokeedit.slot", new Object[0]);
        Pokemon pokemon = (Pokemon)PixelmonCommandUtils.require(storage.get(slot - 1), "pixelmon.command.partyslot.nothing", new Object[]{player.getName()});

        //already shiny
        if (pokemon.isShiny()) {
            PixelmonCommandUtils.endCommand("message.jackspdmmod.already_shiny", new Object[0]);
        }

        String[] specList = {"shiny"};
        PixelmonCommandUtils.applySpecs(pokemon, specList);
        player.getMainHandItem().shrink(1);
        sender.sendSuccess(PixelmonCommandUtils.format(TextFormatting.GREEN, "pixelmon.command.pokeedit.edited", new Object[]{pokemon.getLocalizedName()}), false);
    }

    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) throws CommandSyntaxException {
        if (args.length == 1) {
            return PixelmonCommandUtils.PARTY_SLOTS;
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}
