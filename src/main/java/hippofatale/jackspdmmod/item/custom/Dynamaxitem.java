package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Dynamaxitem extends Item {
    public Dynamaxitem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            PlayerPartyStorage party = StorageProxy.getParty((ServerPlayerEntity) player);
            if (!party.getMegaItemsUnlocked().canDynamax()) {
                party.setMegaItem(EnumMegaItem.DynamaxBand, false);
                party.unlockDynamax(false);
                player.getItemInHand(hand).shrink(1);

                PixelmonCommandUtils.sendMessage((ServerPlayerEntity) player, "pixelmon.command.dynamaxband.received", new Object[0]);
            } else {
                PixelmonCommandUtils.sendMessage((ServerPlayerEntity) player, "pixelmon.command.dynamaxband.alreadyhas", new Object[]{player.getName()});
            }
        }
        return super.use(world, player, hand);
    }
}
