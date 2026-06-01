package hippofatale.jackspdmmod.item.custom;

import hippofatale.jackspdmmod.item.ModItems;
import hippofatale.jackspdmmod.util.GachaLists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.*;

public class GachaItem extends Item {
    private char gachaCode;
    public GachaItem(Properties properties, char gachaCode) {
        super(properties);
        this.gachaCode = gachaCode;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            if (getEmptySlots(player) > 0) {
                ITextComponent gachaName = null;
                ItemStack gachaResult = null;
                ITextComponent resultName = null;
                int quantity = 0;
                switch (gachaCode) {
                    case 'R':
                        gachaName = (new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get())).getHoverName().copy().withStyle(TextFormatting.DARK_PURPLE);
                        gachaResult = new ItemStack(getGachaCubeResult(GachaLists.getRareToolGacha()));
                        resultName = gachaResult.getHoverName().copy().withStyle(TextFormatting.YELLOW);
                        break;
                    case 'G':
                        gachaName = (new ItemStack(ModItems.GREAT_GACHA_BOX.get())).getHoverName().copy().withStyle(TextFormatting.BLUE);
                        gachaResult = getGachaBoxResult(GachaLists.getGreatGacha());
                        resultName = gachaResult.getHoverName().copy().withStyle(TextFormatting.YELLOW);
                        break;
                    case 'U':
                        gachaName = (new ItemStack(ModItems.ULTRA_GACHA_BOX.get())).getHoverName().copy().withStyle(TextFormatting.GRAY);
                        gachaResult = getGachaBoxResult(GachaLists.getUltraGacha());
                        resultName = gachaResult.getHoverName().copy().withStyle(TextFormatting.YELLOW);
                        break;
                    case 'M':
                        gachaName = (new ItemStack(ModItems.MASTER_GACHA_BOX.get())).getHoverName().copy().withStyle(TextFormatting.LIGHT_PURPLE);;
                        gachaResult = getGachaBoxResult(GachaLists.getMasterGacha());
                        resultName = gachaResult.getHoverName().copy().withStyle(TextFormatting.YELLOW);
                        break;
                    case 'A':
                        gachaName = (new ItemStack(ModItems.ALL_TOOL_GACHA_CUBE.get())).getHoverName().copy().withStyle(TextFormatting.DARK_GREEN);
                        gachaResult = new ItemStack(getGachaCubeResult(GachaLists.getAllToolGacha()));
                        resultName = gachaResult.getHoverName().copy().withStyle(TextFormatting.YELLOW);
                        break;
                    default:
                        return super.use(world, player, hand);
                }
                quantity = gachaResult.getCount();
                player.inventory.add(gachaResult);
                player.getItemInHand(hand).shrink(1);
                player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.gacha_result",
                        gachaName.copy().withStyle(TextFormatting.BOLD), resultName.copy().withStyle(TextFormatting.BOLD), quantity), false);
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

    private Item getGachaCubeResult(List<Item> gachaCubeList) {
        return gachaCubeList.get(random.nextInt(gachaCubeList.size()));
    }

    private ItemStack getGachaBoxResult(List<ItemStack> gachaBoxList) {
        return gachaBoxList.get(random.nextInt(gachaBoxList.size())).copy();
    }
}
