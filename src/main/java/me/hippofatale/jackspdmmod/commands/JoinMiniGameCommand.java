package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.minigames.MiniGameManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class JoinMiniGameCommand {
    public JoinMiniGameCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("참가").executes((command) -> {
            return joinMiniGame(command.getSource());
        }));
    }

    private int joinMiniGame(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        if (!MiniGameManager.isMiniGameOpen) {
            //not open
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_not_open"), false);
            return 0;
        }

        UUID playerUUID = player.getUUID();
        switch (MiniGameManager.miniGameType) {
            case MAGMA_FALL:
            case JUMP_MAP_RACE:
                //already joined
                if (MiniGameManager.miniGameApplicants.contains(playerUUID)) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_already_joined").withStyle(TextFormatting.YELLOW), false);
                    return 0;
                }

                //add to list
                MiniGameManager.miniGameApplicants.add(playerUUID);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_joined", MiniGameManager.miniGameType.getName()), false);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.mini_game_join_notice").withStyle(TextFormatting.YELLOW), false);

                break;

            case DICE_OF_FORTUNE:
                //already joined
                if (MiniGameManager.diceOfFortune.containsValue(playerUUID)) {
                    for (Integer number : MiniGameManager.diceOfFortune.keySet()) {
                        if (MiniGameManager.diceOfFortune.get(number).equals(playerUUID)) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_already_joinded",
                                    new StringTextComponent(Integer.toString(number)).withStyle(TextFormatting.AQUA)), false);
                            return 0;
                        }
                    }
                }

                //full
                if (MiniGameManager.diceNumbers.isEmpty()) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_full").withStyle(TextFormatting.YELLOW), false);
                    return 0;
                }

                //pick number
                int pickedIndex = (int) (Math.random() * MiniGameManager.diceNumbers.size());
                int pickedNumber = MiniGameManager.diceNumbers.remove(pickedIndex);
                MiniGameManager.diceOfFortune.put(pickedNumber, playerUUID);
                if (pickedNumber >= 100) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_100",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.YELLOW)), false);
                } else if (pickedNumber >= 99) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_99",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.LIGHT_PURPLE)), false);
                } else if (pickedNumber >= 95) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_95",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.GOLD)), false);
                } else if (pickedNumber >= 75) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_75",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.DARK_PURPLE)), false);
                } else if (pickedNumber >= 50) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_50",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.BLUE)), false);
                } else if (pickedNumber >= 25) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_25",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.GREEN)), false);
                } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_1",
                            new StringTextComponent(Integer.toString(pickedNumber)).withStyle(TextFormatting.GRAY)), false);
                }
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_join_notice").withStyle(TextFormatting.YELLOW), false);


                break;
        }

        return 1;
    }

}
