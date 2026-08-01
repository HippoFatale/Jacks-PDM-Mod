package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.enums.TMType;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.TechnicalMoveItem;
import me.hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class TRTradeTicketCommand {
    public TRTradeTicketCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("기술레코드")
                .then(Commands.argument("세대", IntegerArgumentType.integer(8, 8)).then(Commands.argument("번호", IntegerArgumentType.integer()).executes((command) -> {
                    return getTM(command.getSource(), IntegerArgumentType.getInteger(command, "세대"), IntegerArgumentType.getInteger(command, "번호"));
                })))
        );
    }

    private int getTM(CommandSource source, int generation, int id) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!player.getMainHandItem().sameItem(ModItems.TR_TRADE_TICKET.get().getDefaultInstance())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.does_not_have_item"), false);
            return 0;
        } else {
            switch (generation) {
                case 8: {
                    if (id < 0 || id > 99) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.tr_id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TR8, ITechnicalMove.getMoveFor(TMType.TR8, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                default: {
                    return 0;
                }
            }
        }
    }
}
