package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.home.HomeSize;
import hippofatale.jackspdmmod.home.HomeType;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.PurchasePlotC2SPacket;
import hippofatale.jackspdmmod.tileentity.PlacardTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.math.BigDecimal;

public class PlacardScreen extends Screen {
    private ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.placard");
    private int buttonWidth = 80;
    private int buttonHeight = 20;

    private boolean purchased;
    private int plotTypeIndex;
    private int plotSizeIndex;
    private BigDecimal plotPrice;
    private static BlockPos placardPos;

    ITextComponent typeInfo;
    ITextComponent sizeInfo;
    ITextComponent priceInfo;

    public PlacardScreen(boolean purchased, int homeTypeIndex, int homeSizeIndex, long plotPriceLong, BlockPos placardPos) {
        super(new TranslationTextComponent("menu.jackspdmmod.placard"));
        this.purchased = purchased;
        this.plotTypeIndex = homeTypeIndex;
        this.plotSizeIndex = homeSizeIndex;
        plotPrice = new BigDecimal(plotPriceLong);
        PlacardScreen.placardPos = placardPos;
    }

    @Override
    protected void init() {
        super.init();

        if (!purchased) {
            this.addButton(new Button((this.width - buttonWidth) / 2, this.height / 2 + 30, buttonWidth, buttonHeight,
                    new TranslationTextComponent("menu.jackspdmmod.buy_plot"), PlacardScreen::purchase));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        drawCenteredString(p_230430_1_, this.font, title, this.width / 2, this.height / 2 - 40, 16777215);
        setText();
        drawCenteredString(p_230430_1_, this.font, typeInfo, this.width / 2, this.height / 2 - 20, 16777215);
        drawCenteredString(p_230430_1_, this.font, sizeInfo, this.width / 2, this.height / 2, 16777215);
        drawCenteredString(p_230430_1_, this.font, priceInfo, this.width / 2, this.height / 2 + 20, 16777215);

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    private void setText() {
        switch (plotTypeIndex) {
            case 1: {
                typeInfo = new TranslationTextComponent("menu.jackspdmmod.plot_type_info").append("개인 땅");
                priceInfo = new TranslationTextComponent("menu.jackspdmmod.plot_price_info").append(Integer.toString(plotPrice.intValue())).append("P");
                break;
            }
            case 2: {
                typeInfo = new TranslationTextComponent("menu.jackspdmmod.plot_type_info").append("동아리 땅");
                priceInfo = new TranslationTextComponent("menu.jackspdmmod.plot_price_info").append(Integer.toString(plotPrice.intValue())).append("P");
//                priceInfo = new TranslationTextComponent("menu.jackspdmmod.plot_price_info").append(Integer.toString(plotPrice.intValue())).append("학점");
                break;
            }
        }
        switch (plotSizeIndex) {
            case 1: {
                sizeInfo = new TranslationTextComponent("menu.jackspdmmod.plot_size_info").append("소형");
                break;
            }
            case 2: {
                sizeInfo = new TranslationTextComponent("menu.jackspdmmod.plot_size_info").append("중형");
                break;
            }
            case 3: {
                sizeInfo = new TranslationTextComponent("menu.jackspdmmod.plot_size_info").append("대형");
                break;
            }
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void purchase(Button button) {
        ModMessages.sendToServer(new PurchasePlotC2SPacket(placardPos));
        Minecraft.getInstance().setScreen(null);
    }
}
