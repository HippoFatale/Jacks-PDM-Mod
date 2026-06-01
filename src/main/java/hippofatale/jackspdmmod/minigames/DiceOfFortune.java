package hippofatale.jackspdmmod.minigames;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.TreeMap;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class DiceOfFortune {
    private static MinecraftServer server;

    public static void prepareDiceOfFortune(MinecraftServer server) {
        if (server == null) {
            return;
        }
        DiceOfFortune.server = server;


        diceNumbers.clear();
        for (int i = 0; i < 100; i++) {
            diceNumbers.add(i + 1);
        }
    }

    public static void beginDiceOfFortune() {
        if (server == null) {
            return;
        }

        //if no one joined
        if (diceOfFortune.isEmpty()) {
            //end mini-game
            isMiniGameRunning = false;
            return;
        }

        //sort list, get winner
        int winningNumber;
        UUID winningPlayerUUID;
        ServerPlayerEntity winningPlayer;

        while (!diceOfFortune.isEmpty()) {
            winningNumber = new TreeMap<>(diceOfFortune).lastKey();
            winningPlayerUUID = diceOfFortune.get(winningNumber);
            winningPlayer = server.getPlayerList().getPlayer(winningPlayerUUID);

            if (winningPlayer != null) {
                //announce winner
                for (UUID playerUUID : diceOfFortune.values()) {
                    ServerPlayerEntity joinedPlayer = server.getPlayerList().getPlayer(playerUUID);
                    if (joinedPlayer != null) {
                        joinedPlayer.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.dice_of_fortune_winner",
                                new StringTextComponent(winningPlayer.getName().getString()).withStyle(TextFormatting.YELLOW),
                                new StringTextComponent(Integer.toString(winningNumber)).withStyle(TextFormatting.AQUA)), false);
                    }

                }
                //give prize
                winningPlayer.inventory.add(new ItemStack(Items.NETHER_STAR, 1));

                //reset list
                diceOfFortune.clear();

                //end mini-game
                isMiniGameRunning = false;
                return;

            } else {
                //if winner not present
                diceOfFortune.remove(winningNumber);
            }
        }
    }

}
