package me.hippofatale.jackspdmmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.home.Home;
import me.hippofatale.jackspdmmod.home.HomeManager;
import me.hippofatale.jackspdmmod.home.HomeType;
import me.hippofatale.jackspdmmod.tileentity.PlacardTile;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.hippofatale.jackspdmmod.JacksPDMMod.*;
import static me.hippofatale.jackspdmmod.home.HomeType.CLUB;

public class HomeCommand {
    public HomeCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("땅")
                .then(Commands.literal("철거")
                        .then(Commands.literal("개인").executes((command) -> {
                            return demolishHome(command.getSource(), HomeType.PERSONAL);}))

                        .then(Commands.literal("길드").executes((command) -> {
                            return demolishHome(command.getSource(), CLUB);}))
                )

                .then(Commands.literal("권한")
                        .then(Commands.literal("주기").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                            return shareHome(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), true);})))

                        .then(Commands.literal("뺏기").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                            return shareHome(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), false);})))

                        .then(Commands.literal("정보").executes((command) -> {
                            return shareInfo(command.getSource());}))
                )
        );
    }

    private int demolishHome(CommandSource source, HomeType homeType) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        UUID playerUUID = player.getUUID();

        switch (homeType) {
            case PERSONAL: {
                Home personalHome = HomeManager.personalHomes.get(playerUUID);

                if (personalHome == null) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
                    return 0;
                }

                BlockPos placardPos = personalHome.getPlacardPos();
                if (placardPos != null && player.level.getBlockEntity(placardPos) instanceof PlacardTile) {
                    PlacardTile placard = (PlacardTile) player.level.getBlockEntity(placardPos);
                    placard.setPurchased(false);
                }

                HomeManager.personalHomes.remove(playerUUID);
                HomeManager.save();
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.demolished_personal_home"), false);
                return 1;
            }
            case CLUB: {
                Club playerClub = ClubManager.belongingClubs.get(playerUUID);

                if (playerClub == null) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                    return 0;
                }

                if (!playerClub.isPresident(playerUUID)) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
                    return 0;
                }

                Home clubHome = HomeManager.clubHomes.get(playerClub.getClubName());

                if (clubHome == null) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_club_home"), false);
                    return 0;
                }

                BlockPos placardPos = clubHome.getPlacardPos();
                if (placardPos != null && player.level.getBlockEntity(placardPos) instanceof PlacardTile) {
                    PlacardTile placard = (PlacardTile) player.level.getBlockEntity(placardPos);
                    placard.setPurchased(false);
                }

                HomeManager.clubHomes.remove(playerClub.getClubName());
                HomeManager.save();
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.demolished_club_home"), false);
                return 1;
            }
            default: return 0;
        }
    }

    private int shareHome(CommandSource source, ServerPlayerEntity target, boolean isAddSharing) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        UUID playerUUID = player.getUUID();
        Home sharingHome = HomeManager.personalHomes.get(playerUUID);

        if (sharingHome == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
            return 0;
        }

        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        }

        //TODO add translations
        UUID targetUUID = target.getUUID();

        if (targetUUID.equals(playerUUID)) {
            player.displayClientMessage(new StringTextComponent("자신에게 권한을 주거나 뺐을 수 없습니다."), false);
            return 0;
        }

        if (isAddSharing) {
            if (sharingHome.isSharingFull()) {
                player.displayClientMessage(new StringTextComponent("최대 2명까지만 권한을 줄 수 있습니다."), false);
                return 0;
            }

            if (sharingHome.getSharedPlayers().contains(targetUUID)) {
                player.displayClientMessage(new StringTextComponent("해당 플레이어는 이미 내 땅 권한을 가지고 있습니다.").withStyle(net.minecraft.util.text.TextFormatting.YELLOW), false);
                return 0;
            }

            sharingHome.addSharer(targetUUID);
            HomeManager.save();
            target.displayClientMessage(new StringTextComponent(player.getName().getString()).append("님의 개인 땅 권한을 공유받았습니다."), false);
            player.displayClientMessage(new StringTextComponent(target.getName().getString()).append("님에게 개인 땅 권한을 공유했습니다."), false);
            return 1;

        } else {
            if (!sharingHome.getSharedPlayers().contains(targetUUID)) {
                player.displayClientMessage(new StringTextComponent("개인 땅 권한이 없는 플레이어입니다.").withStyle(net.minecraft.util.text.TextFormatting.RED), false);
                return 0;
            }

            sharingHome.removeSharer(targetUUID);
            HomeManager.save();
            target.displayClientMessage(new StringTextComponent(player.getName().getString()).append("님이 개인 땅 권한을 회수했습니다."), false);
            player.displayClientMessage(new StringTextComponent(target.getName().getString()).append("님에게 개인 땅 권한을 회수했습니다."), false);
            return 1;
        }
    }

    private int shareInfo(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        UUID playerUUID = player.getUUID();
        Home sharingHome = HomeManager.personalHomes.get(playerUUID);

        if (sharingHome == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
            return 0;
        }

        PlayerProfileCache cache = source.getServer().getProfileCache();
        List<String> sharedMembers = new ArrayList<>();

        for (UUID memberUUID : sharingHome.getSharedPlayers()) {
            GameProfile profile = cache.get(memberUUID);

            if (profile != null) {
                sharedMembers.add(profile.getName());
            } else {
                sharedMembers.add("알 수 없는 유저");
            }
        }

        player.displayClientMessage(new StringTextComponent("주인: ").append(player.getName().getString()).withStyle(net.minecraft.util.text.TextFormatting.GOLD), false);

        if (sharedMembers.isEmpty()) {
            player.displayClientMessage(new StringTextComponent("공유: ").append("현재 권한을 공유 중인 유저가 없습니다.").withStyle(net.minecraft.util.text.TextFormatting.GRAY), false);
        } else {
            player.displayClientMessage(new StringTextComponent("공유: ").append(String.join(", ", sharedMembers)).withStyle(net.minecraft.util.text.TextFormatting.GREEN), false);
        }
        return 1;
    }
}
