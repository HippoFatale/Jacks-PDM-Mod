package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GachaItem extends Item {
    public GachaItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            List<Item> rareGachaCubeList = Arrays.asList(new Item[]{
                    PixelmonItems.leftovers,
                    PixelmonItems.life_orb,
                    PixelmonItems.choice_band,
                    PixelmonItems.choice_scarf,
                    PixelmonItems.choice_specs,
                    PixelmonItems.sitrus_berry,
                    PixelmonItems.lum_berry,
                    PixelmonItems.muscle_band,
                    PixelmonItems.black_glasses,
                    PixelmonItems.black_sludge,
                    PixelmonItems.focus_sash,
                    PixelmonItems.focus_band,
            });

            if (getEmptySlots(player) > 0) {
                Random random = new Random();
                player.inventory.add(new ItemStack(rareGachaCubeList.get(random.nextInt(rareGachaCubeList.size()))));
                player.getItemInHand(hand).shrink(1);
            }
            else {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.inventory_is_full"), false);
            }
        }
        return super.use(world, player, hand);
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
