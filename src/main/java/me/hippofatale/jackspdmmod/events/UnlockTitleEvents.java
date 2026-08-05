package me.hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.economy.EconomyEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.TitleDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.title.PlayerTitleProvider;
import me.hippofatale.jackspdmmod.title.TitleManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.math.BigDecimal;

import static me.hippofatale.jackspdmmod.JacksPDMMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class UnlockTitleEvents {
    private static void unlockTitle(ServerPlayerEntity player, int titleIndex) {
        player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
            if (!playerTitle.isTitleUnlocked(titleIndex)) {
                playerTitle.unlockTitle(titleIndex);
                ModMessages.sendToPlayer(new TitleDataSyncS2CPacket(playerTitle.getDisplayingTitleIndex(), playerTitle.getTitleUnlockedList()), player);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.title_unlocked", TitleManager.getTitleBold(titleIndex)), false);
            }
        });
    }

    @SubscribeEvent
    public static void onToolBreak(PlayerDestroyItemEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack itemStack = event.getOriginal();

        //life of a miner
        if (itemStack.sameItem(Items.DIAMOND_PICKAXE.getDefaultInstance())) {
            unlockTitle(player, 5);
        }
        //life of a farmer
        if (itemStack.sameItem(Items.DIAMOND_HOE.getDefaultInstance())) {
            unlockTitle(player, 6);
        }
    }

    @SubscribeEvent
    public static void onGetPokemon(PokemonReceivedEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();

        //digidestined
        if (pokemon.getSpecies().getGeneration() == 20) {
            unlockTitle(player, 9);
        //mythical owner
        } else if (pokemon.isMythical()) {
            unlockTitle(player, 8);
        //legendary owner
        } else if (pokemon.isLegendary(true)) {
            unlockTitle(player, 7);
        }
    }

    private static final int checkPlayTimeTickPeriod = 60 * 20;
    @SubscribeEvent
    public static void onCheckPlayTime(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (player.tickCount % checkPlayTimeTickPeriod == 0) {
                int playTimeTicks = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_ONE_MINUTE));

                //no server no life
                if (playTimeTicks >= 24 * 60 * 60 * 20) {
                    unlockTitle(player, 10);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEconomyEvent(EconomyEvent.GetBalance event) {
        ServerPlayerEntity player = event.getPlayer();
        BigDecimal balance = event.getBalance();

        //gold rich
        if (balance.compareTo(new BigDecimal("1000000.0")) >= 0) {
            unlockTitle(player, 12);
        }
        //diamond rich
        if (balance.compareTo(new BigDecimal("10000000.0")) >= 0) {
            unlockTitle(player, 13);
        }
    }
}
