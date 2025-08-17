package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarterPackageItem extends Item {
    public StarterPackageItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            List<Item> packageList = Arrays.asList(new Item[]{
                    ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "poke_ball")),
                    ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "ultra_ball")),
                    PixelmonItems.old_running_boots,
                    ModItems.STARTER_PACKAGE_TICKET.get(),
                    PixelmonItems.rare_candy
            });
            List<Integer> quantityList = Arrays.asList(new Integer[]{
                    10,
                    3,
                    1,
                    1,
                    10
            });

            if (getEmptySlots(player) >= packageList.size()) {
                for (int i = 0; i < packageList.size(); i++) {
                    for (int j = 0; j < quantityList.get(i); j++) {
                        player.inventory.add(new ItemStack(packageList.get(i)));
                    }
                }

                BankAccount account = (BankAccount) BankAccountProxy.getBankAccount((ServerPlayerEntity) player).orElse(null);
                if (account != null) {
                    account.add(1000);
                }

                player.getItemInHand(hand).shrink(1);
            } else {
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.inventory_need_free_slot", Integer.toString(packageList.size())), true);
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
