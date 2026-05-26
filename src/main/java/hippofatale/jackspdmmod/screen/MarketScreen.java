package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.client.ClientCropPriceData;
import hippofatale.jackspdmmod.market.MarketData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncRequestC2SPacket;
import hippofatale.jackspdmmod.networking.packet.MarketSellAllOfTypeC2SPacket;
import hippofatale.jackspdmmod.networking.packet.MarketSellAllOresC2SPacket;
import hippofatale.jackspdmmod.networking.packet.MarketSellC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;

public class MarketScreen extends Screen {
    private ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.market"));
    private int buttonWidth = 60;
    private int buttonHeight = 20;
    private final List<ResourceLocation> imagePaths = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("minecraft:textures/item/coal.png"),
            new ResourceLocation("minecraft:textures/item/lapis_lazuli.png"),
            new ResourceLocation("minecraft:textures/item/redstone.png"),
            new ResourceLocation("minecraft:textures/item/iron_ingot.png"),
            new ResourceLocation("minecraft:textures/item/gold_ingot.png"),
            new ResourceLocation("minecraft:textures/item/diamond.png"),
            new ResourceLocation("minecraft:textures/item/emerald.png"),

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

        this.addButton(new Button(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * -3, 75, buttonHeight,
                new StringTextComponent("모든 광물 판매"), MarketScreen::sellAllOres));

        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(0))).append("P"), MarketScreen::sellItem7));
        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(1))).append("P"), MarketScreen::sellItem8));
        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * 1 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(2))).append("P"), MarketScreen::sellItem9));
        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * 0 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(3))).append("P"), MarketScreen::sellItem10));
        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * -1 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(4))).append("P"), MarketScreen::sellItem11));
        this.addButton(new Button(this.width / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * -2 - 2, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(5))).append("P"), MarketScreen::sellItem12));

        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems7));
        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems8));
        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * 1 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems9));
        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * 0 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems10));
        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * -1 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems11));
        this.addButton(new Button(this.width / 2 + buttonWidth + 35, (this.height - buttonHeight) / 2 - buttonHeight * -2 - 2, buttonWidth, buttonHeight,
                new StringTextComponent("모두 판매"), MarketScreen::sellAllItems12));

        this.addButton(new Button(this.width / 2 + buttonWidth / 2 + 35, (this.height - buttonHeight) / 2 - buttonHeight * -3, buttonWidth, buttonHeight,
                new StringTextComponent("나가기"), MarketScreen::closeMenu));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        int leftX = this.width / 2 - 130;
        int topY = this.height / 2 - 100;
        int rightX = this.width / 2 + 165;
        int bottomY = this.height / 2 + 80;
        //background
        fill(p_230430_1_, leftX, topY - 9, rightX, bottomY, 0xff1556bc); //blue outline
        fill(p_230430_1_, leftX + 2, topY - 7, rightX - 2, bottomY, 0xff000000); //black fill
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_top_left.png"));
        blit(p_230430_1_, leftX, topY - 8,10, 0f, 0f, 16, 16, 16, 16);
        //title
        fill(p_230430_1_, this.width / 2, topY - 16, rightX - 16, topY, 0xff1556bc);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_title_left.png"));
        blit(p_230430_1_, this.width / 2 - 16, topY - 16,10, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_title_right.png"));
        blit(p_230430_1_, rightX - 16, topY - 16, 10,  0f, 0f, 16, 16, 16, 16);
        //bottom
        fill(p_230430_1_, leftX - 12, bottomY, rightX - 12, bottomY + 12, 0xff1556bc);
        fill(p_230430_1_, leftX - 10, bottomY, rightX - 12, bottomY + 10, 0xff000000);
        fill(p_230430_1_, leftX - 12, bottomY, this.width / 2 - 12, bottomY + 2, 0xff1556bc);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right.png"));
        blit(p_230430_1_, rightX - 16, bottomY,10, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center.png"));
        blit(p_230430_1_, this.width / 2 - 12, bottomY,10, 0f, 0f, 16, 16, 16, 16);
        //bottom dec
        hLine(p_230430_1_, this.width / 2, rightX - 16, bottomY + 5, 0xff1556bc); //blue arrow
        fill(p_230430_1_, leftX - 4, bottomY + 5, leftX - 2, bottomY + 7, 0xff1556bc); //dots
        fill(p_230430_1_, leftX + 4, bottomY + 5, leftX + 6, bottomY + 7, 0xff1556bc);
        fill(p_230430_1_, leftX + 12, bottomY + 5, leftX + 14, bottomY + 7, 0xff1556bc);

        drawCenteredString(p_230430_1_, this.font, title.copy().withStyle(TextFormatting.BOLD), (this.width / 2 + rightX - 16) / 2, topY - 12, 0xffffff);


        minecraft.getTextureManager().bind(imagePaths.get(0));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 4,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×12: ").append(new StringTextComponent(String.valueOf(marketPrices.get("coal"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 4 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(1));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 3,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×12: ").append(new StringTextComponent(String.valueOf(marketPrices.get("lapis_lazuli"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 3 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(2));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 2,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×12: ").append(new StringTextComponent(String.valueOf(marketPrices.get("redstone"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 2 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(3));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 1,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×1: ").append(new StringTextComponent(String.valueOf(marketPrices.get("iron_ore"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 1 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(4));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 0,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×1: ").append(new StringTextComponent(String.valueOf(marketPrices.get("gold_ore"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 0 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(5));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * -1,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×1: ").append(new StringTextComponent(String.valueOf(marketPrices.get("diamond"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * -1 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(6));
        blit(p_230430_1_, this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * -2,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×1: ").append(new StringTextComponent(String.valueOf(marketPrices.get("emerald"))).append("P")),
                this.width / 2 - buttonWidth * 2 + 17, (this.height - buttonHeight) / 2 - buttonHeight * -2 + 4, 16777215);


        drawCenteredString(p_230430_1_, this.font, new StringTextComponent("오늘의 가격"),
                this.width / 2 + 65, (this.height - buttonHeight) / 2 - buttonHeight * 4 + 4, 16777215);


        minecraft.getTextureManager().bind(imagePaths.get(7));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 3,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×32: ").append(new StringTextComponent(String.valueOf(MarketData.melonOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 3 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(8));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 2,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×32: ").append(new StringTextComponent(String.valueOf(MarketData.pumpkinOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 2 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(9));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 1,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×64: ").append(new StringTextComponent(String.valueOf(MarketData.cocoaOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 1 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(10));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 0,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×32: ").append(new StringTextComponent(String.valueOf(MarketData.wheatOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * 0 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(11));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * -1,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×32: ").append(new StringTextComponent(String.valueOf(MarketData.potatoOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * -1 + 4, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(12));
        blit(p_230430_1_, this.width / 2 - buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * -2,
                10, 0f, 0f, 16, 16, 16, 16);
        drawString(p_230430_1_, this.font, new StringTextComponent("×32: ").append(new StringTextComponent(String.valueOf(MarketData.carrotOriginalPrice)).append("P")),
                this.width / 2 - buttonWidth + 20 + 17, (this.height - buttonHeight) / 2 - buttonHeight * -2 + 4, 16777215);



        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void sellItem7(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(7));
    }
    public static void sellItem8(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(8));
    }
    public static void sellItem9(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(9));
    }
    public static void sellItem10(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(10));
    }
    public static void sellItem11(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(11));
    }
    public static void sellItem12(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(12));
    }

    public static void sellAllItems7(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(7));
    }
    public static void sellAllItems8(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(8));
    }
    public static void sellAllItems9(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(9));
    }
    public static void sellAllItems10(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(10));
    }
    public static void sellAllItems11(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(11));
    }
    public static void sellAllItems12(Button button) {
        ModMessages.sendToServer(new MarketSellAllOfTypeC2SPacket(12));
    }

    public static void sellAllOres(Button button) {
        ModMessages.sendToServer(new MarketSellAllOresC2SPacket());
    }

    private static void closeMenu(Button button) {
        Minecraft.getInstance().setScreen(null);
    }
}
