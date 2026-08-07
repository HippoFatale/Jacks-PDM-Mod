package me.hippofatale.jackspdmmod.events;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTiers;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.minigames.MiniGameManager;
import me.hippofatale.jackspdmmod.commands.*;
import me.hippofatale.jackspdmmod.home.HomeManager;
import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.networking.packet.PlayerTitleDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.networking.packet.RankPointDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.networking.packet.TeleportDataSyncS2CPacket;
import me.hippofatale.jackspdmmod.rank.PlayerRankPointProvider;
import me.hippofatale.jackspdmmod.teleport.PlayerTeleportUnlockProvider;
import me.hippofatale.jackspdmmod.teleport.TeleportManager;
import me.hippofatale.jackspdmmod.title.PlayerTitleProvider;
import me.hippofatale.jackspdmmod.title.TitleManager;
import me.hippofatale.jackspdmmod.item.custom.GachaLists;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.*;

import static me.hippofatale.jackspdmmod.JacksPDMMod.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ModEvents {
    //commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MenuCommand(event.getDispatcher());
        new TitleCommand(event.getDispatcher());
        new ClubCommand(event.getDispatcher());
//        new ClubPointCommand(event.getDispatcher());
        new BasicPokemonTicketCommand(event.getDispatcher());
        new HomeCommand(event.getDispatcher());
        new TMTradeTicketCommand(event.getDispatcher());
        new TRTradeTicketCommand(event.getDispatcher());
        new PDTransferCommand(event.getDispatcher());
        new BattleSpectateCommand(event.getDispatcher());
        new JoinMiniGameCommand(event.getDispatcher());
//        new CasinoSwitchingCommand(event.getDispatcher());
        new MiniGameCommand(event.getDispatcher());
        new ShinyTradeTicketCommand(event.getDispatcher());
        new RankPointCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    //first join reward
    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

            CompoundNBT forgeData = player.getPersistentData();
            CompoundNBT persistedData;

            if (forgeData.contains(ServerPlayerEntity.PERSISTED_NBT_TAG)) {
                persistedData = forgeData.getCompound(ServerPlayerEntity.PERSISTED_NBT_TAG);
            } else {
                persistedData = new CompoundNBT();
                forgeData.put(ServerPlayerEntity.PERSISTED_NBT_TAG, persistedData);
            }

            if (!persistedData.getBoolean("join_reward_claimed")) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.welcome"), false);

                player.inventory.add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "poke_ball")), 10));
                player.inventory.add(new ItemStack(PixelmonItems.exp_share));

                persistedData.putBoolean("join_reward_claimed", true);
                forgeData.put(ServerPlayerEntity.PERSISTED_NBT_TAG, persistedData);
            }
        }
    }

    //sync data at login
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            ClubManager.pendingInvites.putIfAbsent(player.getUUID(), new ArrayList<>());

            Map<String, MarketItem> cropDataToSync = new HashMap<>();
            for (MarketItem item : MarketManager.cropMarketItems) {
                if (item != null && item.getRegistryName() != null) {
                    cropDataToSync.put(item.getRegistryName(), item);
                }
            }
            ModMessages.sendToPlayer(new CropPriceDataSyncS2CPacket(cropDataToSync), player);

            // Sync player's title to all other players
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                ModMessages.sendToAll(new PlayerTitleDataSyncS2CPacket(player.getUUID(), playerTitle.getDisplayingTitleIndex()));
            });

            // Sync all existing players' titles to the new player
            for (ServerPlayerEntity otherPlayer : player.level.getServer().getPlayerList().getPlayers()) {
                if (otherPlayer != player) {
                    otherPlayer.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(otherTitle -> {
                        ModMessages.sendToPlayer(new PlayerTitleDataSyncS2CPacket(otherPlayer.getUUID(), otherTitle.getDisplayingTitleIndex()), player);
                    });
                }
            }
        }
    }

    //display title
    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        if (!event.getPlayer().level.isClientSide()) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            player.getCapability(PlayerTitleProvider.PLAYER_TITLE).ifPresent(playerTitle -> {
                if (playerTitle.getDisplayingTitleIndex() == 0) {
                    event.setDisplayname(player.getName());
                }
                else {
                    event.setDisplayname(new StringTextComponent("[")
                            .append(TitleManager.getTitleBold(playerTitle.getDisplayingTitleIndex()))
                            .append("]")
                            .append(player.getName()));
                }
            });
        }
    }

    //unlock teleport
    @SubscribeEvent
    public static void onPlayerVisitTown(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getTarget() instanceof NPCEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
                NPCEntity npc = (NPCEntity) event.getTarget();

                List<Vector3d> coordinates = TeleportManager.getTeleportPosList();
                for (Vector3d coordinate : coordinates) {
                    if (new Vector3d(npc.getX(), npc.getY(), npc.getZ()).distanceTo(coordinate) < 5) {
                        int townIndex = coordinates.indexOf(coordinate);
                        player.getCapability(PlayerTeleportUnlockProvider.PLAYER_TELEPORT_UNLOCK).ifPresent(playerTeleportUnlock -> {
                            if (!playerTeleportUnlock.isTeleportUnlocked(townIndex)) {
                                playerTeleportUnlock.unlockTeleport(townIndex);
                                ModMessages.sendToPlayer(new TeleportDataSyncS2CPacket(playerTeleportUnlock.getTeleportUnlockedList(), playerTeleportUnlock.isHomeUnlocked(), playerTeleportUnlock.isClubHomeUnlocked()), player);
                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleport_unlocked", TeleportManager.getTeleportName(townIndex)), false);
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    //rank point
    @SubscribeEvent
    public static void onBeatTrainer(BeatTrainerEvent event) {
        ServerPlayerEntity player = event.player;
        String tierId = event.trainer.getBossTier().getID();
        int gainedPoints;
        switch (tierId) {
            case BossTiers.EQUAL:
                gainedPoints = 1;
                break;
            case BossTiers.COMMON:
                gainedPoints = 3;
                break;
            case BossTiers.UNCOMMON:
                gainedPoints = 5;
                break;
            default:
                return;
        }

        player.getCapability(PlayerRankPointProvider.PLAYER_RANK_POINT).ifPresent(playerRankPoint -> {
            int oldRankPoint = playerRankPoint.getRankPoints();
            playerRankPoint.addRankPoints(gainedPoints);
            int newRankPoint = playerRankPoint.getRankPoints();
            ModMessages.sendToPlayer(new RankPointDataSyncS2CPacket(player.getUUID(), playerRankPoint.getRankPoints()), player);
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.rank_point_earned", Integer.toString(gainedPoints), Integer.toString(oldRankPoint), Integer.toString(newRankPoint)), false);
        });
    }

    //data
    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        ClubManager.load();
        HomeManager.load();
        MarketManager.load();
        MiniGameManager.load();

        GachaLists.buildLists();
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        ClubManager.save();
        HomeManager.save();
        MarketManager.save();
        MiniGameManager.save();
    }
}
