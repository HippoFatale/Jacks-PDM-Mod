package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.events.battles.SpectateEvent;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonClientData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.*;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BattleSpectateCommand extends PixelCommand {

    public BattleSpectateCommand(CommandDispatcher<CommandSource> dispatcher) {
        super(dispatcher);
    }
    public String getName() {
        return "관전";
    }

    public String getUsage(CommandSource sender) {
        return "/관전 <플레이어>";
    }

    public void execute(CommandSource sender, String[] args) throws CommandException {
        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(sender);
        if (args.length == 0) {
            if (!BattleRegistry.removeSpectator(player)) {
                PixelmonCommandUtils.sendMessage(sender, TextFormatting.RED, this.getUsage(sender), new Object[0]);
            }

            NetworkHelper.sendPacket(new EndSpectatePacket(), player);
        } else if (args.length == 1) {
            ServerPlayerEntity target = PixelmonCommandUtils.requireEntityPlayer(args[0]);
            if (target == player) {
                PixelmonCommandUtils.endCommand("pixelmon.command.spectate.self", new Object[0]);
            }

            if (BattleRegistry.getBattle(player) != null) {
                PixelmonCommandUtils.endCommand("pixelmon.command.general.inbattle", new Object[0]);
            }

            BattleController base = (BattleController)PixelmonCommandUtils.require(BattleRegistry.getBattle(target), "pixelmon.command.spectate.nobattle", new Object[]{target.getDisplayName().getString()});
            PlayerParticipant watchedPlayer = (PlayerParticipant)PixelmonCommandUtils.require(base.getPlayer(target), "command.exception", new Object[0]);
            if (!Pixelmon.EVENT_BUS.post(new SpectateEvent.StartSpectate(player, base, target))) {
                if (sender.getEntity() != null) {
                    sender.getEntity().pushthrough = 1.0F;
                }

                NetworkHelper.sendPacket(new StartBattlePacket(base.battleIndex, base.getBattleType(watchedPlayer), base.rules), player);
                NetworkHelper.sendPacket(new SetAllBattlingPokemonPacket(PixelmonClientData.convertToGUI(Arrays.asList(watchedPlayer.allPokemon)), true), player);
                ArrayList<PixelmonWrapper> teamList = watchedPlayer.getTeamPokemonList();
                NetworkHelper.sendPacket(new SetBattlingPokemonPacket(teamList), player);
                NetworkHelper.sendPacket(new SetPokemonBattleDataPacket(PixelmonClientData.convertToGUI(teamList), false), player);
                NetworkHelper.sendPacket(new SetPokemonBattleDataPacket(watchedPlayer.getOpponentData(), true), player);
                if (base.getTeam(watchedPlayer).size() > 1) {
                    NetworkHelper.sendPacket(new SetPokemonTeamDataPacket(watchedPlayer.getAllyData()), player);
                }

                NetworkHelper.sendPacket(new StartSpectatePacket(watchedPlayer.player.getUUID(), (BattleType)base.rules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE)), player);
                base.addSpectator(new Spectator(player, target.getName().getString()));
            }
        } else {
            sender.sendSuccess(PixelmonCommandUtils.format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]), false);
            PixelmonCommandUtils.endCommand(this.getUsage(sender), new Object[0]);
        }

    }

    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) {
        return args.length == 1 ? PixelmonCommandUtils.tabCompleteUsernames() : Collections.emptyList();
    }
}
