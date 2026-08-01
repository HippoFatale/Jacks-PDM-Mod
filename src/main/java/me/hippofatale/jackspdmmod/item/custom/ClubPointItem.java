package me.hippofatale.jackspdmmod.item.custom;

import me.hippofatale.jackspdmmod.club.ClubManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ClubPointItem extends Item {
    public ClubPointItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            if (!ClubManager.belongingClubs.containsKey(player.getUUID())) {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
            } else {
                int clubPoints = player.getItemInHand(hand).getCount();
                player.setItemInHand(hand, ItemStack.EMPTY);
                ClubManager.belongingClubs.get(player.getUUID()).addClubPoints(clubPoints * 100);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.club_points_used", clubPoints * 100), false);
            }
        }
        return super.use(world, player, hand);
    }
}
