package me.hippofatale.jackspdmmod.networking.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import me.hippofatale.jackspdmmod.club.Club;
import me.hippofatale.jackspdmmod.club.ClubManager;
import me.hippofatale.jackspdmmod.home.Home;
import me.hippofatale.jackspdmmod.home.HomeManager;
import me.hippofatale.jackspdmmod.tileentity.PlacardTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static me.hippofatale.jackspdmmod.JacksPDMMod.*;

public class PurchasePlotC2SPacket {
    private BlockPos blockPos;

    public PurchasePlotC2SPacket(BlockPos placardPos) {
        this.blockPos = placardPos;
    }

    public PurchasePlotC2SPacket(ByteBuf buf) {
        this.blockPos = new BlockPos(new Vector3i(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            TileEntity tileEntity = null;
            if (player != null) {
                tileEntity = player.getLevel().getBlockEntity(blockPos);
            }
            if (tileEntity != null) {
                PlacardTile placard = (PlacardTile) tileEntity;
                if (placard.getPurchased()) {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_sold_home").withStyle(TextFormatting.YELLOW), false);
                    return;
                }
                switch (placard.getPlotType()) {
                    case PERSONAL: {
                        if (HomeManager.personalHomes.getOrDefault(player.getUUID(), null) != null) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_has_home"), false);
                            break;
                        }
                        BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                        if (account != null) {
                            if (account.hasBalance(placard.getPlotPrice())) {
                                account.take(placard.getPlotPrice());
                                placard.setPurchased(true);
                                HomeManager.personalHomes.put(player.getUUID(), new Home(placard, player.getUUID()));
                                HomeManager.save();

                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.purchased_home"), false);
                            } else {
                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_pokedollars"), false);
                            }
                        }
                        break;
                    }
                    case CLUB: {
                        if (!ClubManager.belongingClubs.containsKey(player.getUUID())) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                            break;
                        }
                        Club playerClub = ClubManager.belongingClubs.get(player.getUUID());
                        if (!playerClub.isPresident(player.getUUID())) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
                            break;
                        }
                        if (HomeManager.clubHomes.get(playerClub.getClubName()) != null) {
                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.already_has_club_home"), false);
                            break;
                        }
                        BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(player).orElse(null);
                        if (account != null) {
                            if (account.hasBalance(placard.getPlotPrice())) {
                                account.take(placard.getPlotPrice());
                                placard.setPurchased(true);
                                HomeManager.clubHomes.put(playerClub.getClubName(), new Home(placard, playerClub));
                                HomeManager.save();
                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.purchased_club_home"), false);
                            } else {
                                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_pokedollars"), false);
                            }
                        }
                        break;
//                        if (playerClub.getClubPoints() >= placard.getPlotPrice().intValue()) {
//                            playerClub.takeClubPoints(placard.getPlotPrice().intValue());
//                            placard.setPurchased(true);
//                            HomeData.clubHomes.put(playerClub, new Home(placard, player.getUUID(), playerClub.getClubName()));
//                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.purchased_club_home"), false);
//                        } else {
//                            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_enough_club_points"), false);
//                        }
//                        break;
                    }
                }
            }
        });
        return true;
    }
}
