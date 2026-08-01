package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.storage.PlayerStorageProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenStorageC2SPacket {
    public OpenStorageC2SPacket() {

    }

    public OpenStorageC2SPacket(ByteBuf buf) {

    }

    public void toBytes(ByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            player.getCapability(PlayerStorageProvider.PLAYER_STORAGE).ifPresent(playerStorage -> {
                player.openMenu(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
                    return ChestContainer.sixRows(p_226928_1_, p_226928_2_, playerStorage.getStorageInventory());
                }, new TranslationTextComponent("")));
            });


        });
            return true;
    }
}
