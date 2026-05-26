package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.club.Club;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.home.HomeData;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class ClubCommand {
    public ClubCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("길드")
                .then(Commands.literal("만들기").then(Commands.argument("문파명", StringArgumentType.string()).executes((command) -> {
                    return createClub(command.getSource(), StringArgumentType.getString(command, "문파명"));})))

                .then(Commands.literal("초대").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                    return invite(command.getSource(), EntityArgument.getPlayer(command, "플레이어"));})))

                .then(Commands.literal("추방").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                    return kick(command.getSource(), EntityArgument.getPlayer(command, "플레이어"));})))

                .then(Commands.literal("가입").then(Commands.argument("문파명", StringArgumentType.string()).executes((command) -> {
                    return join(command.getSource(), StringArgumentType.getString(command, "문파명"));})))

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
        if (playerClubs.getOrDefault(player.getUUID(), null) != null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_in_club"), false);
            return 0;
        } else if (!player.getMainHandItem().sameItem(ModItems.CLUB_PAPER.get().getDefaultInstance())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_club_paper"), false);
            return 0;
        } else if (playerClubs.containsKey(clubName)) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_with_same_name_exists"), false);
            return 0;
        } else {
            player.getMainHandItem().shrink(1);
            clubs.put(clubName, new Club(clubName, player.getUUID(), player.getName().getString()));
            playerClubs.put(player.getUUID(), clubName);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_created", ClubData.getBelongingClub(player.getUUID()).getClubName()), false);
            return 1;
        }
    }

    private int invite(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        } else if (!playerClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        } else if (!ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        } else if (ClubData.getBelongingClub(player.getUUID()).isFull()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_full"), false);
            return 0;
        } else {
            pendingInvites.get(target.getUUID()).add(ClubData.getBelongingClub(player.getUUID()).getClubName());
            target.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invited_to_club", ClubData.getBelongingClub(player.getUUID()).getClubName()), false);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invited_player", target.getName()), false);
            return 1;
        }
    }

    private int kick(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        } else if (!playerClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);

            //data fix
            ClubData.getBelongingClub(player.getUUID()).removeMember(target.getUUID());
            return 0;
        } else if (!ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        } else if (ClubData.getBelongingClub(player.getUUID()).isPresident(target.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.cannot_kick_slef"), false);
            return 0;
        } else if (!ClubData.arePlayersInSameClub(player, target)) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_same_club"), false);

            //data fix
            ClubData.getBelongingClub(player.getUUID()).removeMember(target.getUUID());
            return 0;
        } else {
            playerClubs.remove(target.getUUID());
            ClubData.getBelongingClub(player.getUUID()).removeMember(target.getUUID());
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.kicked_player", target.getName()), false);
            return 1;
        }
    }

    private int join(CommandSource source, String clubName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (playerClubs.getOrDefault(player.getUUID(), null) != null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_in_club"), false);
            return 0;
        } else if (clubs.get(clubName) == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_not_found"), false);
            return 0;
        } else if (!pendingInvites.get(player.getUUID()).contains(clubName)) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.invited_to_club", clubName), false);
            return 0;
        } else if (clubs.get(clubName).isFull()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_full"), false);
            return 0;
        } else {
            playerClubs.put(player.getUUID(), clubName);
            clubs.get(clubName).addMember(player.getUUID(), player.getName().getString());
            pendingInvites.get(player.getUUID()).remove(clubName);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.joined_club", clubName), false);
            return 1;
        }
    }

    private int leave(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!playerClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        } else if (ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
            if (playerClubs.values().stream().filter(clubName -> clubName.equals(ClubData.getBelongingClub(player.getUUID()).getClubName())).count() > 1) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_alone"), false);
                return 0;
            } else if (clubNameHomes.get(playerClubs.get(player.getUUID())) != null) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.has_club_home"), false);
                return 0;
            } else {
                ClubData.getBelongingClub(player.getUUID()).removeMember(player.getUUID());
                clubs.remove(ClubData.getBelongingClub(player.getUUID()).getClubName());
                playerClubs.remove(player.getUUID());
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.left_and_disbanded_club"), false);
                return 1;
            }
        } else {
            Club belongingClub = ClubData.getBelongingClub(player.getUUID());
            playerClubs.remove(player.getUUID());
            belongingClub.removeMember(player.getUUID());
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.left_club"), false);
            return 1;
        }
    }

    private int changePresident(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!playerClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        } else if (!ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
            return 0;
        } else if (target == null || !playerClubs.get(target.getUUID()).equals(ClubData.getBelongingClub(player.getUUID()).getClubName())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_same_club"), false);
            return 0;
        } else {
            ClubData.getBelongingClub(player.getUUID()).setPresident(target.getUUID(), target.getName().getString());
            player.displayClientMessage(new TranslationTextComponent("changed_president_to_player", target.getName()), false);
            target.displayClientMessage(new TranslationTextComponent("changed_president_to_player", target.getName()), false);
            return 1;
        }
    }

    private int info(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!playerClubs.containsKey(player.getUUID())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            return 0;
        } else {
            String clubName = ClubData.getBelongingClub(player.getUUID()).getClubName();
            String presidentName = ClubData.getBelongingClub(player.getUUID()).getPresidentName();
            List<String> clubMembers = new ArrayList<>();
            for (Map.Entry<UUID, String> entry : ClubData.getBelongingClub(player.getUUID()).getMembers().entrySet()) {
                clubMembers.add(entry.getValue());
            }
            int clubPoints = ClubData.getBelongingClub(player.getUUID()).getClubPoints();

            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_name_info", clubName), false);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.president_info", presidentName), false);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.member_info", String.join(", ", clubMembers)), false);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_point_info", clubPoints), false);
            return 1;
        }
    }

    private int list(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        List<String> clubList = new ArrayList<>();
        for (Map.Entry<String, Club> entry : clubs.entrySet()) {
            clubList.add(entry.getValue().getClubName());
        }

        if (clubList.isEmpty()) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_clubs"), false);
        } else {
            player.displayClientMessage(new StringTextComponent(String.join(", ", clubList)), false);
        }
        return 1;
    }
}
