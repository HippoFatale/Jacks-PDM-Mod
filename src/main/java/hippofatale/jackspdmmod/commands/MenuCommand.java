package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.SetMenuScreenS2CPacket;
import hippofatale.jackspdmmod.util.TickDelay;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class MenuCommand {
    public MenuCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("메뉴").executes((command) -> {
            return openMenu(command.getSource());
        }));
    }

    private int openMenu(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        ModMessages.sendToPlayer(new SetMenuScreenS2CPacket(), player);
        return 1;
    }

}
