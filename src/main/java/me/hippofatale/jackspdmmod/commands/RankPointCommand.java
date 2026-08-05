package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.RankPointDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.rank.PlayerRankPointProvider;
import me.hippofatale.jackspdmmod.rank.RankManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class RankPointCommand {
    public RankPointCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("랭크")
                .executes((command) -> {
                    return checkRank(command.getSource());
                })
                .then(Commands.argument("플레이어", EntityArgument.players())
                        .requires((command) -> {
                            return command.hasPermission(2);
                        })
                        .then(Commands.literal("확인")
                                .executes((command) -> {
                                    return checkRank(command.getSource());
                                })
                        )
                        .then(Commands.argument("포인트", IntegerArgumentType.integer())
                                .then(Commands.literal("변경")
                                        .executes((command) -> {
                                            return setRankPoint(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), IntegerArgumentType.getInteger(command, "포인트"));
                                        })
                                )
                                .then(Commands.literal("증가")
                                        .executes((command) -> {
                                            return addRankPoints(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), IntegerArgumentType.getInteger(command, "포인트"));
                                        })
                                )
                                .then(Commands.literal("감소")
                                        .executes((command) -> {
                                            return subtractRankPoints(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), IntegerArgumentType.getInteger(command, "포인트"));
                                        })
                                )
                        )
                )
        );
    }

    private int checkRank(CommandSource source) throws CommandSyntaxException {
        return this.checkRank(source, source.getPlayerOrException());
    }

    private int checkRank(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        target.getCapability(PlayerRankPointProvider.PLAYER_RANK_POINT).ifPresent(playerRankPoint -> {
            int rankPoint = playerRankPoint.getRankPoints();
            String rankName = RankManager.getRankTier(rankPoint).getName();
            target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_info", rankName, Integer.toString(rankPoint)), false);
        });

        return 1;
    }

    private int setRankPoint(CommandSource source, ServerPlayerEntity target, int point) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        target.getCapability(PlayerRankPointProvider.PLAYER_RANK_POINT).ifPresent(playerRankPoint -> {
            playerRankPoint.setRankPoints(point);
            ModMessages.sendToPlayer(new RankPointDataSyncS2CPacket(target.getUUID(), playerRankPoint.getRankPoints()), target);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
            target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
        });

        return 1;
    }

    private int addRankPoints(CommandSource source, ServerPlayerEntity target, int point) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        target.getCapability(PlayerRankPointProvider.PLAYER_RANK_POINT).ifPresent(playerRankPoint -> {
            playerRankPoint.addRankPoints(point);
            ModMessages.sendToPlayer(new RankPointDataSyncS2CPacket(target.getUUID(), playerRankPoint.getRankPoints()), target);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
            target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
        });

        return 1;
    }

    private int subtractRankPoints(CommandSource source, ServerPlayerEntity target, int point) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        target.getCapability(PlayerRankPointProvider.PLAYER_RANK_POINT).ifPresent(playerRankPoint -> {
            playerRankPoint.removeRankPoints(point);
            ModMessages.sendToPlayer(new RankPointDataSyncS2CPacket(target.getUUID(), playerRankPoint.getRankPoints()), target);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
            target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_changed", target.getName(), Integer.toString(playerRankPoint.getRankPoints())), false);
        });

        return 1;
    }
}
