package me.hippofatale.jackspdmmod.networking.packet;

import me.hippofatale.jackspdmmod.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class StarterPackageTicketSelectC2SPacket {
    private int selection;

    public StarterPackageTicketSelectC2SPacket(int townIndex) {
        this.selection = townIndex;
    }

    public StarterPackageTicketSelectC2SPacket(ByteBuf buf) {
        this.selection = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(selection);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            List<Item> selectableItemsList = Arrays.asList(new Item[]{
                    ModItems.BASIC_SELECTION_TICKET.get(),
                    ModItems.DIGIMON_SPAWN_GACHA.get()
            });
            if (player != null) {
                if (player.getMainHandItem().sameItem(ModItems.STARTER_PACKAGE_TICKET.get().getDefaultInstance())) {
                    if (getEmptySlots(player) > 0) {
                        player.inventory.add(selectableItemsList.get(selection).getDefaultInstance());
                        player.getMainHandItem().shrink(1);
                    }
                    else {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.inventory_is_full"), true);
                    }
                } else {
                    player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.does_not_have_item"), false);
                }

            }
        });
        return true;
    }

    private int getEmptySlots(PlayerEntity player) {
        int emptySlots = 0;
        for (int i = 0; i < player.inventory.items.size(); i++) {
            if (player.inventory.items.get(i).isEmpty()) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }
}
