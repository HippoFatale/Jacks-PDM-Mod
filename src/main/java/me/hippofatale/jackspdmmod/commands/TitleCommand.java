package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.PlayerTitleDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.networking.packet.TitleDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.title.PlayerTitleProvider;
import me.hippofatale.jackspdmmod.title.TitleManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class TitleCommand {
    public TitleCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("칭호").requires((command) -> {
            return command.hasPermission(2);})
                .then(Commands.argument("플레이어", EntityArgument.player()).then(Commands.argument("칭호번호", IntegerArgumentType.integer(1, 15)).executes((command) -> {
                    return unlockTitle(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), IntegerArgumentType.getInteger(command, "칭호번호"), true);}).
                        then(Commands.literal("뺏기").executes((command) -> {
                            return unlockTitle(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), IntegerArgumentType.getInteger(command, "칭호번호"), false);})))));
    }

    private int unlockTitle(CommandSource source, ServerPlayerEntity target, int titleIndex, boolean isUnlock) throws CommandSyntaxException {
        target.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
            if (isUnlock) {
                playerTitle.unlockTitle(titleIndex);
                ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), target);
                ModMessages.sendToAll(new PlayerTitleDataSyncS2CPacket(target.getUUID(), playerTitle.getDisplayingTitleIndex()));
                target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.title_unlocked", TitleManager.getTitleBold(titleIndex)), false);
            }
            else {
                playerTitle.lockTitle(titleIndex);
                ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), target);
                ModMessages.sendToAll(new PlayerTitleDataSyncS2CPacket(target.getUUID(), playerTitle.getDisplayingTitleIndex()));
            }
        });

        return 1;
    }
}
