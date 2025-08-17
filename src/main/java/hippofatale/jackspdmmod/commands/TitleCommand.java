package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.TitleDataSyncS2CPacket;
import hippofatale.jackspdmmod.title.PlayerTitleProvider;
import hippofatale.jackspdmmod.title.TitleData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class TitleCommand {
    public TitleCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("칭호").requires((command) -> {
            return command.hasPermission(2);})
                .then(Commands.argument("플레이어", EntityArgument.players()).then(Commands.argument("칭호번호", IntegerArgumentType.integer(1, 15)).executes((command) -> {
                    return unlockTitle(command.getSource(), EntityArgument.getPlayers(command, "플레이어"), IntegerArgumentType.getInteger(command, "칭호번호"), true);}).
                        then(Commands.literal("뺐기").executes((command) -> {
                            return unlockTitle(command.getSource(), EntityArgument.getPlayers(command, "플레이어"), IntegerArgumentType.getInteger(command, "칭호번호"), false);})))));
    }

    private int unlockTitle(CommandSource source, Collection<ServerPlayerEntity> playerEntities, int titleIndex, boolean isUnlock) throws CommandSyntaxException {
        for (ServerPlayerEntity player : playerEntities) {
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                if (isUnlock) {
                    playerTitle.unlockTitle(titleIndex);
                    ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), player);
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.title_unlocked", TitleData.getTitleTextBold(titleIndex)), false);
                }
                else {
                    playerTitle.lockTitle(titleIndex);
                    ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), player);
                }
            });
        }
        return playerEntities.size();
    }
}
