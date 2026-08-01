package me.hippofatale.jackspdmmod.item.custom;

import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.SetStarterPackageTicketScreenS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class StarterPackageTicketItem extends Item {
    public StarterPackageTicketItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            ModMessages.sendToPlayer(new SetStarterPackageTicketScreenS2CPacket(), (ServerPlayerEntity) player);
        }
        return super.use(world, player, hand);
    }
}
