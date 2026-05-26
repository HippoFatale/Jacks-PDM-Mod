package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.client.ClientTeleportData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TeleportScreen extends Screen {
    private ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.teleport"));
    private int buttonWidth = 80;
    private int buttonHeight = 20;

    protected TeleportScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.teleport"));
    }

    @Override
    protected void init() {
        super.init();

        //default
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, 0, TeleportScreen::teleport0, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, 1, TeleportScreen::teleport1, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, 2, TeleportScreen::teleport2, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 10, 3, TeleportScreen::teleport3, false);

        //homes
        this.addButton(new Button((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_home"), TeleportScreen::teleportHome));
        this.addButton(new Button((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * 2 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_club_home"), TeleportScreen::teleportClubHome));

        //towns
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 1, 4, TeleportScreen::teleport4, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 1, 5, TeleportScreen::teleport5, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * 1, 6, TeleportScreen::teleport6, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * -0, 7, TeleportScreen::teleport7, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 0, 8, TeleportScreen::teleport8, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * 0, 9, TeleportScreen::teleport9, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * -1, 10, TeleportScreen::teleport10, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * -1, 11, TeleportScreen::teleport11, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * -1, 12, TeleportScreen::teleport12, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * -2, 13, TeleportScreen::teleport13, false);
        teleportButton((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * -2, 14, TeleportScreen::teleport14, false);
    }

    private void teleportButton(int x, int y, int teleportIndex, Button.IPressable button, boolean isHidden) {
        if (ClientTeleportData.getTeleportUnlocked(teleportIndex) == 1) {
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    ClientTeleportData.getTeleportName(teleportIndex), button));
        }
        else if (!isHidden){
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    new StringTextComponent("???"), TeleportScreen::teleportNotUnlocked));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
//        drawCenteredString(p_230430_1_, this.font, title, (this.width - buttonWidth) / 2, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 5, 16777215);
        int leftX = this.width / 2 - 140;
        int topY = this.height / 2 - 90;
        int rightX = this.width / 2 + 140;
        int bottomY = this.height / 2 + 60;
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
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void teleportHome(Button button) {
        ModMessages.sendToServer(new PersonalHomeTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleportClubHome(Button button) {
        ModMessages.sendToServer(new ClubHomeTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleport0(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(0));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport1(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(1));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport2(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(2));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport3(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(3));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport4(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(4));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport5(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(5));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport6(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(6));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport7(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(7));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport8(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(8));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport9(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(9));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport10(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(10));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport11(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(11));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport12(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(12));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport13(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(13));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleport14(Button button) {
        ModMessages.sendToServer(new TeleportC2SPacket(14));
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleportNotUnlocked(Button button) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.teleport_not_visited"), false);
        }
        Minecraft.getInstance().setScreen(null);
    }

    private static void backToMenu(Button button) {
        Minecraft.getInstance().setScreen(new MenuScreen());
    }
}
