package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.market.MarketData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncS2CPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;
import static hippofatale.jackspdmmod.market.MarketData.*;

public class OriginalPriceCommand {
    public OriginalPriceCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("원가").requires((command) -> {
            return command.hasPermission(2);
                })
                .then(Commands.argument("채집물", StringArgumentType.string())
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), StringArgumentType.getString(command, "채집물"), IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("수박")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "melon_slice", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("호박")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "pumpkin", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("코코아콩")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "melon_slice", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("밀")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "wheat", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("감자")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "potato", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
                .then(Commands.literal("당근")
                        .then(Commands.argument("가격", IntegerArgumentType.integer(100))
                                .executes((command) -> {
                                    return changePrice(command.getSource(), "carrot", IntegerArgumentType.getInteger(command, "가격"));
                                })
                        )
                )
        );
    }

    private int changePrice(CommandSource source, String item, int price) throws CommandSyntaxException {
        switch (item) {
            case "melon_slice":
                melonOriginalPrice = price;
                break;
            case "pumpkin":
                pumpkinOriginalPrice = price;
                break;
            case "cocoa_beans":
                cocoaOriginalPrice = price;
                break;
            case "wheat":
                wheatOriginalPrice = price;
                break;
            case "potato":
                potatoOriginalPrice = price;
                break;
            case "carrot":
                carrotOriginalPrice = price;
                break;
            default:
                return 0;
        }
        MarketData.saveMarketData();
        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            int[] cropPrices = {marketPrices.get("melon_slice"), marketPrices.get("pumpkin"), marketPrices.get("cocoa_beans"), marketPrices.get("wheat"), marketPrices.get("potato"), marketPrices.get("carrot")};
            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropPrices), player);
        }
        return 1;
    }
}
