package me.hippofatale.jackspdmmod.networking;

import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.networking.packet.*;
import me.hippofatale.jackspdmmod.networking.packet.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JacksPDMMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        //teleport
        net.messageBuilder(TeleportC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TeleportC2SPacket::new)
                .encoder(TeleportC2SPacket::toBytes)
                .consumer(TeleportC2SPacket::handle)
                .add();
        net.messageBuilder(TeleportDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TeleportDataSyncS2CPacket::new)
                .encoder(TeleportDataSyncS2CPacket::toBytes)
                .consumer(TeleportDataSyncS2CPacket::handle)
                .add();
        net.messageBuilder(PersonalHomeTeleportC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PersonalHomeTeleportC2SPacket::new)
                .encoder(PersonalHomeTeleportC2SPacket::toBytes)
                .consumer(PersonalHomeTeleportC2SPacket::handle)
                .add();
        net.messageBuilder(ClubHomeTeleportC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClubHomeTeleportC2SPacket::new)
                .encoder(ClubHomeTeleportC2SPacket::toBytes)
                .consumer(ClubHomeTeleportC2SPacket::handle)
                .add();

        //storage
        net.messageBuilder(OpenStorageC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OpenStorageC2SPacket::new)
                .encoder(OpenStorageC2SPacket::toBytes)
                .consumer(OpenStorageC2SPacket::handle)
                .add();

        //title
        net.messageBuilder(TitleDisplayC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TitleDisplayC2SPacket::new)
                .encoder(TitleDisplayC2SPacket::toBytes)
                .consumer(TitleDisplayC2SPacket::handle)
                .add();
        net.messageBuilder(TitleDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TitleDataSyncS2CPacket::new)
                .encoder(TitleDataSyncS2CPacket::toBytes)
                .consumer(TitleDataSyncS2CPacket::handle)
                .add();

        //market
        net.messageBuilder(MarketSellC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MarketSellC2SPacket::new)
                .encoder(MarketSellC2SPacket::toBytes)
                .consumer(MarketSellC2SPacket::handle)
                .add();
        net.messageBuilder(MarketSellAllOresC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MarketSellAllOresC2SPacket::new)
                .encoder(MarketSellAllOresC2SPacket::toBytes)
                .consumer(MarketSellAllOresC2SPacket::handle)
                .add();
        net.messageBuilder(MarketSellAllOfTypeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MarketSellAllOfTypeC2SPacket::new)
                .encoder(MarketSellAllOfTypeC2SPacket::toBytes)
                .consumer(MarketSellAllOfTypeC2SPacket::handle)
                .add();
        net.messageBuilder(CropPriceDataSyncRequestC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CropPriceDataSyncRequestC2SPacket::new)
                .encoder(CropPriceDataSyncRequestC2SPacket::toBytes)
                .consumer(CropPriceDataSyncRequestC2SPacket::handle)
                .add();
        net.messageBuilder(CropPriceDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CropPriceDataSyncS2CPacket::new)
                .encoder(CropPriceDataSyncS2CPacket::toBytes)
                .consumer(CropPriceDataSyncS2CPacket::handle)
                .add();

        //home
        net.messageBuilder(PurchasePlotC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PurchasePlotC2SPacket::new)
                .encoder(PurchasePlotC2SPacket::toBytes)
                .consumer(PurchasePlotC2SPacket::handle)
                .add();

        //screen
        net.messageBuilder(SetMenuScreenS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetMenuScreenS2CPacket::new)
                .encoder(SetMenuScreenS2CPacket::toBytes)
                .consumer(SetMenuScreenS2CPacket::handle)
                .add();
        net.messageBuilder(SetPlacardScreenS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetPlacardScreenS2CPacket::new)
                .encoder(SetPlacardScreenS2CPacket::toBytes)
                .consumer(SetPlacardScreenS2CPacket::handle)
                .add();
        net.messageBuilder(SetStarterPackageTicketScreenS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetStarterPackageTicketScreenS2CPacket::new)
                .encoder(SetStarterPackageTicketScreenS2CPacket::toBytes)
                .consumer(SetStarterPackageTicketScreenS2CPacket::handle)
                .add();
        net.messageBuilder(SetSlotMachineScreenS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetSlotMachineScreenS2CPacket::new)
                .encoder(SetSlotMachineScreenS2CPacket::toBytes)
                .consumer(SetSlotMachineScreenS2CPacket::handle)
                .add();

        //item
        net.messageBuilder(StarterPackageTicketSelectC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StarterPackageTicketSelectC2SPacket::new)
                .encoder(StarterPackageTicketSelectC2SPacket::toBytes)
                .consumer(StarterPackageTicketSelectC2SPacket::handle)
                .add();

        //slot machine
        net.messageBuilder(PlaySlotMachineC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlaySlotMachineC2SPacket::new)
                .encoder(PlaySlotMachineC2SPacket::toBytes)
                .consumer(PlaySlotMachineC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
