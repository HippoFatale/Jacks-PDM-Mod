package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.client.ClientTitleData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.TitleDisplayC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TitleScreen extends Screen {
    private ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.title");
    private int buttonWidth = 80;
    private int bigButtonWidth = 120;
    private int buttonHeight = 20;

    protected TitleScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.title"));
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new Button((this.width - buttonWidth) / 2 - buttonWidth * (-1), this.height / 2 - buttonHeight * 4 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.remove_title"), TitleScreen::removeTitle));

        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 1, this.height / 2 - buttonHeight * 3, 1, TitleScreen::displayTitle1, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 0, this.height / 2 - buttonHeight * 3, 2, TitleScreen::displayTitle2, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * -1, this.height / 2 - buttonHeight * 3, 3, TitleScreen::displayTitle3, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 1, this.height / 2 - buttonHeight * 2, 4, TitleScreen::displayTitle4, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 0, this.height / 2 - buttonHeight * 2, 5, TitleScreen::displayTitle5, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * -1, this.height / 2 - buttonHeight * 2, 6, TitleScreen::displayTitle6, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 1, this.height / 2 - buttonHeight * 1, 7, TitleScreen::displayTitle7, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 0, this.height / 2 - buttonHeight * 1, 8, TitleScreen::displayTitle8, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * -1, this.height / 2 - buttonHeight * 1, 9, TitleScreen::displayTitle9, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 1, this.height / 2 - buttonHeight * 0, 10, TitleScreen::displayTitle10, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 0, this.height / 2 - buttonHeight * 0, 11, TitleScreen::displayTitle11, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * -1, this.height / 2 - buttonHeight * 0, 12, TitleScreen::displayTitle12, false);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 1, this.height / 2 - buttonHeight * -1, 13, TitleScreen::displayTitle13, true);
        titleButton((this.width - buttonWidth) / 2 - buttonWidth * 0, this.height / 2 - buttonHeight * -1, 14, TitleScreen::displayTitle14, true);

        titleButtonBig(this.width / 2 - bigButtonWidth * 1, this.height / 2 - buttonHeight * -2 + 10, 15, TitleScreen::displayTitle15, true);
        titleButtonBig(this.width / 2 - bigButtonWidth * 0, this.height / 2 - buttonHeight * -2 + 10, 16, TitleScreen::displayTitle16, true);
    }

    private void titleButton(int x, int y, int titleIndex, Button.IPressable button, boolean isHidden) {
        if (ClientTitleData.getTitleUnlocked(titleIndex) == 1) {
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    ClientTitleData.getTitleText(titleIndex), button));
        }
        else if (!isHidden){
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    new StringTextComponent("???"), TitleScreen::doesNotHaveTitle));
        }
    }
    private void titleButtonBig(int x, int y, int titleIndex, Button.IPressable button, boolean isHidden) {
        if (ClientTitleData.getTitleUnlocked(titleIndex) == 1) {
            this.addButton(new Button(x, y, bigButtonWidth, buttonHeight,
                    ClientTitleData.getTitleText(titleIndex), button));
        }
        else if (!isHidden){
            this.addButton(new Button(x, y, bigButtonWidth, buttonHeight,
                    new StringTextComponent("???"), TitleScreen::doesNotHaveTitle));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        drawCenteredString(p_230430_1_, this.font, title, (this.width - buttonWidth) / 2, this.height / 2 - buttonHeight * 4 - 5, 16777215);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void removeTitle(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(0));
        Minecraft.getInstance().setScreen(null);
    }

    private static void displayTitle1(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(1));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle2(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(2));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle3(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(3));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle4(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(4));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle5(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(5));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle6(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(6));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle7(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(7));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle8(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(8));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle9(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(9));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle10(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(10));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle11(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(11));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle12(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(12));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle13(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(13));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle14(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(14));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle15(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(15));
        Minecraft.getInstance().setScreen(null);
    }
    private static void displayTitle16(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(16));
        Minecraft.getInstance().setScreen(null);
    }

    private static void doesNotHaveTitle(Button button) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.does_not_have_title"), false);
        }
    }
}
