package me.hippofatale.jackspdmmod.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.home.HomeManager;
import me.hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClubCommand {
    public ClubCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("길드")
                .then(Commands.literal("만들기").then(Commands.argument("길드명", StringArgumentType.greedyString()).executes((command) -> {
                    return createClub(command.getSource(), StringArgumentType.getString(command, "길드명"));})))

                .then(Commands.literal("초대").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                    return invite(command.getSource(), EntityArgument.getPlayer(command, "플레이어"));})))

                .then(Commands.literal("추방").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                    return kick(command.getSource(), EntityArgument.getPlayer(command, "플레이어"));})))

                .then(Commands.literal("가입").then(Commands.argument("길드명", StringArgumentType.greedyString()).executes((command) -> {
                    return join(command.getSource(), StringArgumentType.getString(command, "길드명"));})))

                .then(Commands.literal("탈퇴").executes((command) -> {
                    return leave(command.getSource());}))

                .then(Commands.literal("길드장위임").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                    return changePresident(command.getSource(), EntityArgument.getPlayer(command, "플레이어"));})))

                .then(Commands.literal("정보").executes((command) -> {
                    return info(command.getSource());}))

                .then(Commands.literal("목록").executes((command) -> {
                    return list(command.getSource());}))
        );
    }

    private int createClub(CommandSource source, String clubName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        clubName = clubName.trim();

        if (clubName.isEmpty()) {
            //TODO add translation
            player.displayClientMessage(new StringTextComponent("올바른 길드 이름을 입력해주세요.").withStyle(net.minecraft.util.text.TextFormatting.RED), false);
            return 0;
        }

        if (clubName.length() < 2 || clubName.length() > 10) {
            //TODO add translation
            player.displayClientMessage(new StringTextComponent("길드 이름은 2자 이상 10자 이하로 입력해주세요.").withStyle(net.minecraft.util.text.TextFormatting.RED), false);
            return 0;
        }

        if (ClubManager.belongingClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_in_club"), false);
            return 0;
        }

        if (player.getMainHandItem().getItem() != ModItems.CLUB_PAPER.get()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_club_paper"), false);
            return 0;
        }

        boolean isNameExists = false;
        for (Club club : ClubManager.clubs) {
            if (club.getClubName().equalsIgnoreCase(clubName)) {
                isNameExists = true;
                break;
            }
        }
        if (isNameExists) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_with_same_name_exists"), false);
            return 0;
        }

        player.getMainHandItem().shrink(1);
        Club newClub = new Club(clubName, player.getUUID());
        ClubManager.clubs.add(newClub);
        ClubManager.belongingClubs.put(player.getUUID(), newClub);
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_created", newClub.getClubName()), false);
        return 1;
    }

    private int invite(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Club playerClub = ClubManager.belongingClubs.get(player.getUUID());

        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        }

        if (playerClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        }

        if (!playerClub.isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        }

        if (playerClub.isFull()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_full"), false);
            return 0;
        }

        if (ClubManager.belongingClubs.containsKey(target.getUUID())) {
            //TODO add translation
            player.displayClientMessage(new StringTextComponent("해당 플레이어는 이미 다른 길드에 가입되어 있습니다.").withStyle(net.minecraft.util.text.TextFormatting.RED), false);
            return 0;
        }

        ClubManager.pendingInvites.get(target.getUUID()).add(ClubManager.belongingClubs.get(player.getUUID()));
        target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invited_to_club", ClubManager.belongingClubs.get(player.getUUID()).getClubName()), false);
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invited_player", target.getName()), false);
        return 1;

    }

    private int kick(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Club playerClub = ClubManager.belongingClubs.get(player.getUUID());

        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        }

        if (playerClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        }

        if (!playerClub.isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        }

        if (player.getUUID().equals(target.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.cannot_kick_slef"), false);
            return 0;
        }

        Club targetClub = ClubManager.belongingClubs.get(target.getUUID());
        if (targetClub == null || !playerClub.getClubName().equals(targetClub.getClubName())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_same_club"), false);

            if (playerClub.getMembers().contains(target.getUUID())) {
                playerClub.removeMember(target.getUUID());
            }
            return 0;
        }

        playerClub.removeMember(target.getUUID());
        ClubManager.belongingClubs.remove(target.getUUID());
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.kicked_player", target.getName()), false);
        return 1;
    }

    private int join(CommandSource source, String clubName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        UUID playerUUID = player.getUUID();

        if (ClubManager.belongingClubs.containsKey(playerUUID)) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_in_club"), false);
            return 0;
        }

        Club targetClub = null;
        for (Club club : ClubManager.clubs) {
            if (club.getClubName().equalsIgnoreCase(clubName)) {
                targetClub = club;
                break;
            }
        }
        if (targetClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_not_found"), false);
            return 0;
        }

        List<Club> invites = ClubManager.pendingInvites.get(playerUUID);
        if (invites == null || !invites.contains(targetClub)) {
            //TODO add translation
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invitation_not_found"), false);
            return 0;
        }

        if (targetClub.isFull()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_full"), false);
            return 0;
        }

        else {
            ClubManager.belongingClubs.put(playerUUID, targetClub);
            targetClub.addMember(playerUUID);
            invites.remove(targetClub);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.joined_club", targetClub.getClubName()), false);
            return 1;
        }
    }

    private int leave(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        UUID playerUUID = player.getUUID();
        Club playerClub = ClubManager.belongingClubs.get(playerUUID);

        if (playerClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        }

        if (playerClub.isPresident(playerUUID)) {
            if (playerClub.getMembers().size() > 1) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_alone"), false);
                return 0;
            }

            if (HomeManager.clubHomes.containsKey(playerClub)) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.has_club_home"), false);
                return 0;
            }

            playerClub.removeMember(player.getUUID());
            ClubManager.belongingClubs.remove(playerUUID);
            ClubManager.clubs.remove(playerClub);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.left_and_disbanded_club"), false);
            return 1;
        } else {
            ClubManager.belongingClubs.remove(playerUUID);
            playerClub.removeMember(player.getUUID());
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.left_club"), false);
            return 1;
        }
    }

    private int changePresident(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Club playerClub = ClubManager.belongingClubs.get(player.getUUID());

        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        }

        if (playerClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        }

        if (!playerClub.isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        }

        if (!playerClub.getMembers().contains(target.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_same_club"), false);
            return 0;
        }

        if (player.getUUID().equals(target.getUUID())) {
            //TODO add translation
            player.displayClientMessage(new StringTextComponent("자기 자신에게는 길드장을 위임할 수 없습니다.").withStyle(net.minecraft.util.text.TextFormatting.RED), false);
            return 0;
        }

        playerClub.setPresident(target.getUUID());
        player.displayClientMessage(new TranslationTextComponent("changed_president_to_player", target.getName()), false);
        target.displayClientMessage(new TranslationTextComponent("changed_president_to_player", target.getName()), false);
        return 1;
    }

    private int info(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        Club playerClub = ClubManager.belongingClubs.get(player.getUUID());

        if (playerClub == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        }

        String clubName = playerClub.getClubName();

        PlayerProfileCache cache = source.getServer().getProfileCache();
        GameProfile presProfile = cache.get(playerClub.getPresident());
        String presidentName = presProfile != null ? presProfile.getName() : "알 수 없는 유저";

        List<String> clubMembers = new ArrayList<>();
        for (UUID memberUUID : playerClub.getMembers()) {
            GameProfile memberProfile = cache.get(memberUUID);

            if (memberProfile != null) {
                clubMembers.add(memberProfile.getName());
            } else {
                clubMembers.add("알 수 없는 유저");
            }
        }

        int clubPoints = playerClub.getClubPoints();

        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_name_info", clubName), false);
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.president_info", presidentName), false);
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.member_info", String.join(", ", clubMembers)), false);
        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_point_info", clubPoints), false);
        return 1;
    }

    private int list(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        if (ClubManager.clubs.isEmpty()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_clubs"), false);
        }

        List<String> clubList = new ArrayList<>();
        for (Club club : ClubManager.clubs) {
            if (club != null) {
                clubList.add(club.getClubName());
            }
        }
        player.displayClientMessage(new StringTextComponent(String.join(", ", clubList)), false);
        return 1;
    }
}
