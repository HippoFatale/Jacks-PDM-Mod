package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.minigames.MiniGameManager;
import me.hippofatale.jackspdmmod.teleport.TeleportManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportC2SPacket {
    private int teleportIndex;

    public TeleportC2SPacket(int teleportIndex) {
        this.teleportIndex = teleportIndex;
    }

    public TeleportC2SPacket(ByteBuf buf) {
        this.teleportIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(teleportIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                if (teleportIndex == 3 && !player.isCreative()) {
                    if (!MiniGameManager.isCasinoOpen) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.casino_is_closed").withStyle(TextFormatting.YELLOW), false);
                        return;
                    }
                }

                if (player.getVehicle() != null) {
                    Entity vehicle = player.getVehicle();
                    player.stopRiding();
                    player.moveTo(TeleportManager.getTeleportPos(teleportIndex));
                    vehicle.moveTo(TeleportManager.getTeleportPos(teleportIndex));
                    player.startRiding(vehicle);
                } else {
                    player.moveTo(TeleportManager.getTeleportPos(teleportIndex));
                }
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleported",
                        TeleportManager.getTeleportName(teleportIndex).copy().withStyle(TextFormatting.YELLOW)), false);
            }
        });
        return true;
    }
}
