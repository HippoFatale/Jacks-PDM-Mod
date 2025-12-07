package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import static hippofatale.jackspdmmod.JacksPDMMod.playerClubs;

public class ClubPointCommand {
    public ClubPointCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("문파자금")
                .then(Commands.literal("인출").then(Commands.argument("수치", IntegerArgumentType.integer(100, 6400)).executes((command) -> {
                    return withdrawClubPoints(command.getSource(), IntegerArgumentType.getInteger(command, "수치"));})))
        );
    }

    private int withdrawClubPoints(CommandSource source, int amount) throws CommandSyntaxException {
        try {
            int amountHundreds = amount / 100;
            ServerPlayerEntity player = source.getPlayerOrException();
            if (!playerClubs.containsKey(player.getUUID())) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                return 0;
            } else if (!ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
                return 0;
            } else if (ClubData.getBelongingClub(player.getUUID()).getClubPoints() < amountHundreds * 100) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_club_points"), false);
                return 0;
            } else if (getEmptySlots(player) <= 0) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.inventory_is_full"), false);
                return 0;
            } else {
                ClubData.getBelongingClub(player.getUUID()).takeClubPoints(amountHundreds * 100);
                for (int i = 0; i < amountHundreds; i++) {
                    player.inventory.add(ModItems.CLUB_POINTS.get().getDefaultInstance());
                }
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_points_withdrawed", amountHundreds * 100), false);
                return 1;
            }
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        
    }

    private int getEmptySlots(PlayerEntity player) {
        int emptySlots = 0;
        for (int i = 0; i < player.inventory.items.size(); i++) {
            if (player.inventory.items.get(i).isEmpty()) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }
}
