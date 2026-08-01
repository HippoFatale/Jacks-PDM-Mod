package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.market.MarketItem;
import me.hippofatale.jackspdmmod.market.MarketManager;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.MarketSellAllOfTypeC2SPacket;
import me.hippofatale.jackspdmmod.networking.packet.MarketSellAllOresC2SPacket;
import me.hippofatale.jackspdmmod.networking.packet.MarketSellC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class MarketScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.market"));
    private final int buttonWidth = 60;
    private final int buttonHeight = 20;
    private final List<ResourceLocation> oreImagePaths = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("minecraft:textures/item/coal.png"),
            new ResourceLocation("minecraft:textures/item/lapis_lazuli.png"),
            new ResourceLocation("minecraft:textures/item/redstone.png"),
            new ResourceLocation("minecraft:textures/item/iron_ingot.png"),
            new ResourceLocation("minecraft:textures/item/gold_ingot.png"),
            new ResourceLocation("minecraft:textures/item/diamond.png"),
            new ResourceLocation("minecraft:textures/item/emerald.png")
    });
    private final List<ResourceLocation> cropImagePaths = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("minecraft:textures/item/melon_slice.png"),
            new ResourceLocation("minecraft:textures/block/pumpkin_side.png"),
            new ResourceLocation("minecraft:textures/item/cocoa_beans.png"),
            new ResourceLocation("minecraft:textures/item/wheat.png"),
            new ResourceLocation("minecraft:textures/item/potato.png"),
            new ResourceLocation("minecraft:textures/item/carrot.png")
    });
    public MarketScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.market"));
    }

    @Override
    protected void init() {
        super.init();

        addButton(new Button(width / 2 - buttonWidth * 2, (height - buttonHeight) / 2 - buttonHeight * -3, 75, buttonHeight,
                new StringTextComponent("모든 광물 판매"), MarketScreen::sellAllOres));

        for (int i = 0; i < MarketManager.cropMarketItems.size(); i++) {
            int itemIndex = i;
            MarketItem marketItem = MarketManager.cropMarketItems.get(itemIndex);
            addButton(new Button(width / 2 + 35, (height - buttonHeight) / 2 - buttonHeight * (3 - i) - 2, buttonWidth, buttonHeight,
                    new StringTextComponent(String.valueOf(marketItem.getDefaultPrice())).append("P"), button -> {ModMessages.sendToServer(new MarketSellC2SPacket(itemIndex));}));
        }

        for (int i = 0; i < MarketManager.cropMarketItems.size(); i++) {
            int itemIndex = i;
            MarketItem marketItem = MarketManager.cropMarketItems.get(itemIndex);
            addButton(new Button(width / 2 + buttonWidth + 35, (height - buttonHeight) / 2 - buttonHeight * (3 - i) - 2, buttonWidth, buttonHeight,
                    new StringTextComponent("모두 판매"), button -> {ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(itemIndex));}));
        }

        addButton(new Button(width / 2 + buttonWidth / 2 + 35, (height - buttonHeight) / 2 - buttonHeight * -3, buttonWidth, buttonHeight,
                new StringTextComponent("나가기"), MarketScreen::closeMenu));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        ScreenBackgrounds.drawReRBackground(p_230430_1_, width, height, 130, 165, 100, 80, font, title);
        int leftX = width / 2 - 130;
        int topY = height / 2 - 100;
        int rightX = width / 2 + 165;
        int bottomY = height / 2 + 80;

        for (int i = 0; i < MarketManager.oreMarketItems.size(); i++) {
            minecraft.getTextureManager().bind(oreImagePaths.get(i));
            MarketItem marketItem = MarketManager.oreMarketItems.get(i);
            blit(p_230430_1_, width / 2 - buttonWidth * 2, (height - buttonHeight) / 2 - buttonHeight * (4 - i),
                    10, 0f, 0f, 16, 16, 16, 16);
            drawString(p_230430_1_, font, new StringTextComponent("×").append(String.valueOf(marketItem.getQuantity())).append(": ").append(String.valueOf(marketItem.getPrice())).append("P"),
                    width / 2 - buttonWidth * 2 + 17, (height - buttonHeight) / 2 - buttonHeight * (4 - i) + 4, 16777215);
        }

        drawCenteredString(p_230430_1_, font, new StringTextComponent("오늘의 가격"),
                width / 2 + 65, (height - buttonHeight) / 2 - buttonHeight * 4 + 4, 16777215);

        for (int i = 0; i < MarketManager.cropMarketItems.size(); i++) {
            minecraft.getTextureManager().bind(cropImagePaths.get(i));
            MarketItem marketItem = MarketManager.cropMarketItems.get(i);
            blit(p_230430_1_, width / 2 - buttonWidth + 20, (height - buttonHeight) / 2 - buttonHeight * (3 - i),
                    10, 0f, 0f, 16, 16, 16, 16);
            drawString(p_230430_1_, font, new StringTextComponent("×").append(String.valueOf(marketItem.getQuantity())).append(": ").append(String.valueOf(marketItem.getPrice())).append("P"),
                    width / 2 - buttonWidth + 20 + 17, (height - buttonHeight) / 2 - buttonHeight * (3 - i) + 4, 16777215);
        }

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void sellAllOres(Button button) {
        ModMessages.sendToServer(new MarketSellAllOresC2SPacket());
    }

    private static void closeMenu(Button button) {
        Minecraft.getInstance().setScreen(null);
    }
}
