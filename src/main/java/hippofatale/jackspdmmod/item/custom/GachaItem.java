package hippofatale.jackspdmmod.item.custom;

import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import hippofatale.jackspdmmod.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

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
                ItemStack gachaResult = new ItemStack(null);
                switch (gachaCode) {
                    case ('R'): {
                        gachaResult = new ItemStack(getRareToolGachaCubeItem());
                        break;
                    }
                    case ('G'): {
                        gachaResult = getGreatGachaBoxItem();
                        break;
                    }
                    case ('U'): {
                        gachaResult = getUltraGachaBoxItem();
                        break;
                    }
                    case ('M'): {
                        gachaResult = getMasterGachaBoxItem();
                        break;
                    }
                }
                player.inventory.add(gachaResult);
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

    private static Item getRareToolGachaCubeItem() {
        List<Item> gachaItemList = Arrays.asList(new Item[]{
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
        return gachaItemList.get(random.nextInt(gachaItemList.size()));
    }

    private static ItemStack getGreatGachaBoxItem() {
        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(PixelmonItems.rare_candy, 2),
                new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "great_ball")), 5)
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                2,
                1,
                1
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);

        return gachaTableList.get(random.nextInt(gachaTableList.size()));
    }

    private static ItemStack getUltraGachaBoxItem() {
        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get()),
                new ItemStack(PixelmonItems.rare_candy, 10),
                new ItemStack(ModItems.SHINY_TRADE_TICKET.get()),
                new ItemStack(ModItems.BASIC_SELECTION_TICKET.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "ultra_ball")), 10)
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                1,
                2,
                2,
                1,
                4
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);

        return gachaTableList.get(random.nextInt(gachaTableList.size()));
    }

    private static ItemStack getMasterGachaBoxItem() {
        List<ItemStack> gachaItemList = Arrays.asList(new ItemStack[]{
                new ItemStack(ModItems.RARE_TOOL_GACHA_CUBE.get()),
                new ItemStack(ModItems.LEGENDARY_SPAWN_GACHA.get()),
                new ItemStack(ModItems.MYTHICAL_SPAWN_GACHA.get()),
                new ItemStack(ModItems.DIGIMON_SPAWN_GACHA.get()),
                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "master_ball"))),
                new ItemStack(ModItems.INVITATION_CARD.get())
        });

        List<Integer> gachaChanceList = Arrays.asList(new Integer[]{
                12,
                1, 1, 1,
                6,
                9
        });

        List<ItemStack> gachaTableList = getGachaTableList(gachaItemList, gachaChanceList);

        return gachaTableList.get(random.nextInt(gachaTableList.size()));
    }

    private static List<ItemStack> getGachaTableList(List<ItemStack> itemStackList, List<Integer> chanceList) {
        List<ItemStack> gachaTableList = new ArrayList<>(Collections.emptyList());

        if (itemStackList.size() != chanceList.size()) {
            return itemStackList;
        }

        for (int i = 0; i < itemStackList.size(); i++) {
            for (int j = 0; j < chanceList.get(i); j++) {
                gachaTableList.add(itemStackList.get(i));
            }
        }
        return gachaTableList;
    }
}
