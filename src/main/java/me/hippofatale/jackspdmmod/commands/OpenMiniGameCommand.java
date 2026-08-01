package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.events.MiniGameRunEvents;
import me.hippofatale.jackspdmmod.minigames.MiniGameManager;
import me.hippofatale.jackspdmmod.minigames.MiniGameType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenMiniGameCommand {
    public OpenMiniGameCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("미니게임").requires((command) -> {
                    return command.hasPermission(2);
                })
                .then(Commands.literal("마그마드롭").executes((command) -> {
                    return openMagmaDrop(command.getSource());
                }))
                .then(Commands.literal("달려라점프맵").executes((command) -> {
                    return openJumpMapRace(command.getSource());
                }))
                .then(Commands.literal("운명의주사위").executes((command) -> {
                    return openDiceOfFortune(command.getSource());
                }))
        );
    }

    private int openMagmaDrop(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (MiniGameManager.isMiniGameOpen || MiniGameManager.isMiniGameRunning) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_already_running").withStyle(TextFormatting.YELLOW), false);
            return 0;
        }
        else {
            MiniGameRunEvents.openMiniGame(MiniGameType.MAGMA_FALL);
            return 1;
        }
    }

    private int openJumpMapRace(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (MiniGameManager.isMiniGameOpen || MiniGameManager.isMiniGameRunning) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_already_running").withStyle(TextFormatting.YELLOW), false);
            return 0;
        }
        else {
            MiniGameRunEvents.openMiniGame(MiniGameType.JUMP_MAP_RACE);
            return 1;
        }
    }

    private int openDiceOfFortune(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (MiniGameManager.isMiniGameOpen || MiniGameManager.isMiniGameRunning) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_already_running").withStyle(TextFormatting.YELLOW), false);
            return 0;
        }
        else {
            MiniGameRunEvents.openMiniGame(MiniGameType.DICE_OF_FORTUNE);
            return 1;
        }
    }
}
