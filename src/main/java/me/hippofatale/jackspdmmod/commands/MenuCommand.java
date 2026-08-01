package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.SetMenuScreenS2CPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class MenuCommand {
    public MenuCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("메뉴").executes((command) -> {
            return openMenu(command.getSource());
        }));
    }

    private int openMenu(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (BattleRegistry.getBattle(player) != null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.menu_not_allowed_in_battle").withStyle(TextFormatting.YELLOW), false);
            return 0;
        }
        ModMessages.sendToPlayer(new SetMenuScreenS2CPacket(), player);
        return 1;
    }

}
