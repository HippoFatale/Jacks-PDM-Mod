package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.enums.TMType;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.TechnicalMoveItem;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class TMTradeTicketCommand {
    public TMTradeTicketCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("기술머신")
                .then(Commands.argument("세대", IntegerArgumentType.integer(1, 9)).then(Commands.argument("번호", IntegerArgumentType.integer()).executes((command) -> {
                    return getTM(command.getSource(), IntegerArgumentType.getInteger(command, "세대"), IntegerArgumentType.getInteger(command, "번호"));
                })))
        );
    }

    private int getTM(CommandSource source, int generation, int id) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (!player.getMainHandItem().sameItem(ModItems.TM_TRADE_TICKET.get().getDefaultInstance())) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.does_not_have_item"), false);
            return 0;
        } else {
            switch (generation) {
                case 1: {
                    if (id < 1 || id > 50) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM1, ITechnicalMove.getMoveFor(TMType.TM1, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 2: {
                    if (id < 1 || id > 50) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM2, ITechnicalMove.getMoveFor(TMType.TM2, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 3: {
                    if (id < 1 || id > 50) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM3, ITechnicalMove.getMoveFor(TMType.TM3, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 4: {
                    if (id < 1 || id > 92) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM4, ITechnicalMove.getMoveFor(TMType.TM4, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 5: {
                    if (id < 1 || id > 95) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM5, ITechnicalMove.getMoveFor(TMType.TM5, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 6: {
                    if (id < 1 || id > 101) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM6, ITechnicalMove.getMoveFor(TMType.TM6, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 7: {
                    if (id < 1 || id > 100) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM7, ITechnicalMove.getMoveFor(TMType.TM7, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 8: {
                    if (id < 0 || id > 99) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM8, ITechnicalMove.getMoveFor(TMType.TM8, id)));
                        player.getMainHandItem().shrink(1);
                        return 1;
                    }
                }
                case 9: {
                    if (id < 1 || id > 224) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.id_out_of_range"), false);
                        return 0;
                    } else {
                        player.inventory.add(TechnicalMoveItem.of(TMType.TM9, ITechnicalMove.getMoveFor(TMType.TM9, id)));
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
