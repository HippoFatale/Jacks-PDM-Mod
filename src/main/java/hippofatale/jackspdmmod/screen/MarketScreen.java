package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.client.ClientCropPriceData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncRequestC2SPacket;
import hippofatale.jackspdmmod.networking.packet.MarketSellAllOresC2SPacket;
import hippofatale.jackspdmmod.networking.packet.MarketSellC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static hippofatale.jackspdmmod.JacksPDMMod.marketPrices;

public class MarketScreen extends Screen {
    private ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.market");
    private int buttonWidth = 60;
    private int buttonHeight = 20;
    public MarketScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.market"));
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * 4 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("coal"))).append("P"), MarketScreen::sellItem0));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("lapis_lazuli"))).append("P"), MarketScreen::sellItem1));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("redstone"))).append("P"), MarketScreen::sellItem2));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * 1 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("iron_ore"))).append("P"), MarketScreen::sellItem3));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * 0 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("gold_ore"))).append("P"), MarketScreen::sellItem4));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * (-1) - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("diamond"))).append("P"), MarketScreen::sellItem5));
        this.addButton(new Button(this.width / 2 - buttonWidth, (this.height - buttonHeight) / 2 - buttonHeight * (-2) - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(marketPrices.get("emerald"))).append("P"), MarketScreen::sellItem6));
        this.addButton(new Button((this.width  - buttonWidth) / 2 - buttonWidth - 10, (this.height - buttonHeight) / 2 - buttonHeight * (-3) + 5, 80, buttonHeight,
                new StringTextComponent("모든 광물 판매"), MarketScreen::sellAllOres));

        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 4 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(0))).append("P"), MarketScreen::sellItem7));
        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(1))).append("P"), MarketScreen::sellItem8));
        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(2))).append("P"), MarketScreen::sellItem9));
        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 1 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(3))).append("P"), MarketScreen::sellItem10));
        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * 0 - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(4))).append("P"), MarketScreen::sellItem11));
        this.addButton(new Button(this.width / 2 + buttonWidth + 20, (this.height - buttonHeight) / 2 - buttonHeight * (-1) - 5, buttonWidth, buttonHeight,
                new StringTextComponent(String.valueOf(ClientCropPriceData.getCropPrice(5))).append("P"), MarketScreen::sellItem12));

        this.addButton(new Button(this.width / 2 + buttonWidth / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * (-2) + 5, buttonWidth, buttonHeight,
                new StringTextComponent("나가기"), MarketScreen::closeMenu));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        drawString(p_230430_1_, this.font, new StringTextComponent("석탄 12개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * 4, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("청금석 12개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * 3, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("레드스톤 12개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * 2, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("철광석 1개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * 1, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("긍광석 1개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * 0, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("다이아몬드 1개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * (-1), 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("에메랄드 1개"), this.width / 2 - buttonWidth * 2 - 5, (this.height - buttonHeight) / 2 - buttonHeight * (-2), 16777215);

        drawString(p_230430_1_, this.font, new StringTextComponent("수박 32개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * 4, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("호박 32개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * 3, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("코코아 콩 64개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * 2, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("밀 32개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * 1, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("감자 32개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * 0, 16777215);
        drawString(p_230430_1_, this.font, new StringTextComponent("당근 32개"), this.width / 2 + 10, (this.height - buttonHeight) / 2 - buttonHeight * (-1), 16777215);

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void sellItem0(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(0));
    }
    public static void sellItem1(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(1));
    }
    public static void sellItem2(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(2));
    }
    public static void sellItem3(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(3));
    }
    public static void sellItem4(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(4));
    }
    public static void sellItem5(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(5));
    }
    public static void sellItem6(Button button) {
        ModMessages.sendToServer(new MarketSellC2SPacket(6));
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

    public static void sellAllOres(Button button) {
        ModMessages.sendToServer(new MarketSellAllOresC2SPacket());
    }

    private static void closeMenu(Button button) {
        Minecraft.getInstance().setScreen(null);
    }
}
