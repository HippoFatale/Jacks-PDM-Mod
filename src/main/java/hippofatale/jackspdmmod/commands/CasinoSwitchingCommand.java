package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import static hippofatale.jackspdmmod.JacksPDMMod.isCasinoOpen;

public class CasinoSwitchingCommand {
    public CasinoSwitchingCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("도박장").requires((command) -> {
            return command.hasPermission(2);})
                .then(Commands.literal("열기").executes((command) -> {
                    return openCasino(command.getSource());}))

                .then(Commands.literal("닫기").executes((command) -> {
                    return closeCasino(command.getSource());}))
        );
    }

    private int openCasino(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        isCasinoOpen = true;
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.open_casino"), false);
        return 1;
    }

    private int closeCasino(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        isCasinoOpen = false;
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.close_casino"), false);
        return 1;
    }
}
