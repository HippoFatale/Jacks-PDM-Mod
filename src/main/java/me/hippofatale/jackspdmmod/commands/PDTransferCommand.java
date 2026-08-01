package me.hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class PDTransferCommand extends PixelCommand {
    public PDTransferCommand(CommandDispatcher<CommandSource> dispatcher) {
        super(dispatcher, "송금", "/송금 <플레이어> <금액>", 0);
    }

    public void execute(CommandSource sender, String[] args) throws CommandException, CommandSyntaxException {
        if (args.length == 2) {
            ServerPlayerEntity player = sender.getPlayerOrException();
            ServerPlayerEntity target = PixelmonCommandUtils.requireEntityPlayer(args[0]);
            if (player == target) {
                PixelmonCommandUtils.endCommand("pixelmon.command.transfer.sameplayer", new Object[0]);
            }

            int amount = PixelmonCommandUtils.requireInt(args[1], 1, 999999, "parsing.int.invalid", new Object[0]);
            BankAccount userAccount = (BankAccount) BankAccountProxy.getBankAccount(player).orElseThrow(() -> new NullPointerException("bank account"));
            BankAccount targetAccount = (BankAccount)BankAccountProxy.getBankAccount(target).orElseThrow(() -> new NullPointerException("bank account"));
            if (userAccount.getBalance().doubleValue() < (double)amount) {
                PixelmonCommandUtils.endCommand("pixelmon.command.transfer.notenoughmoney", new Object[0]);
            }

            int beforeCurrency = targetAccount.getBalance().intValue();
            targetAccount.add(amount);
            int currencyDifference = targetAccount.getBalance().intValue() - beforeCurrency;
            if (currencyDifference == 0) {
                if (amount > 0) {
                    PixelmonCommandUtils.sendMessage(sender, "pixelmon.command.givemoney.moneylimit", new Object[]{target.getDisplayName()});
                } else {
                    PixelmonCommandUtils.sendMessage(sender, "pixelmon.command.givemoney.nomoney", new Object[]{target.getDisplayName()});
                }

                targetAccount.setBalance(beforeCurrency);
            } else {
                userAccount.take(currencyDifference);
                String currencyString = Integer.toString(currencyDifference);
//                sender.sendSuccess(new TranslationTextComponent("pixelmon.command.transfer.notifytransfer", new Object[]{sender.getTextName(), currencyString, target.getDisplayName()}), true);
                PixelmonCommandUtils.sendMessage(sender, "pixelmon.command.transfer.transferred", new Object[]{currencyString, target.getDisplayName()});
                target.sendMessage(new TranslationTextComponent( "pixelmon.command.transfer.received", new Object[]{player.getDisplayName(), currencyString}), Util.NIL_UUID);
            }
        } else {
            sender.sendSuccess(PixelmonCommandUtils.format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]), false);
            PixelmonCommandUtils.endCommand(this.getUsage(sender), new Object[0]);
        }

    }

    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) {
        return args.length == 1 ? PixelmonCommandUtils.tabCompleteUsernames() : Collections.emptyList();
    }
}
