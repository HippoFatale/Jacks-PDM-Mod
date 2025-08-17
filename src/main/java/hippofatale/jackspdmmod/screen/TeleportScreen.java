package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.client.ClientTeleportData;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TeleportScreen extends Screen {
    private ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.teleport");
    private int buttonWidth = 60;
    private int buttonHeight = 20;

    protected TeleportScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.teleport"));
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new Button(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_school"), TeleportScreen::teleportSchool));
        this.addButton(new Button(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_mine"), TeleportScreen::teleportMine));
        this.addButton(new Button(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_home"), TeleportScreen::teleportHome));
        this.addButton(new Button(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * 3 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_club_home"), TeleportScreen::teleportClubHome));

        teleportButton(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 2, 0, TeleportScreen::teleportTown0, false);
        teleportButton(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 2, 1, TeleportScreen::teleportTown1, false);
        teleportButton(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 2, 2, TeleportScreen::teleportTown2, false);
        teleportButton(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * 2, 3, TeleportScreen::teleportTown3, false);
        teleportButton(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 1, 4, TeleportScreen::teleportTown4, false);
        teleportButton(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 1, 5, TeleportScreen::teleportTown5, false);
        teleportButton(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 1, 6, TeleportScreen::teleportTown6, false);
        teleportButton(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * 1, 7, TeleportScreen::teleportTown7, false);
        teleportButton(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * 0, 8, TeleportScreen::teleportTown8, false);
        teleportButton(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * 0, 9, TeleportScreen::teleportTown9, false);
        teleportButton(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * 0, 10, TeleportScreen::teleportTown10, false);
        teleportButton(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * 0, 11, TeleportScreen::teleportTown11, false);
        teleportButton(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * (-1), 12, TeleportScreen::teleportTown12, false);
        teleportButton(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * (-1), 13, TeleportScreen::teleportTown13, false);
        teleportButton(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * (-1), 14, TeleportScreen::teleportTown14, false);
        teleportButton(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * (-1), 15, TeleportScreen::teleportTown15, false);
        teleportButton(this.width / 2 - buttonWidth * 2, (this.height - buttonHeight) / 2 - buttonHeight * (-2), 16, TeleportScreen::teleportTown16, false);
        teleportButton(this.width / 2 - buttonWidth * 1, (this.height - buttonHeight) / 2 - buttonHeight * (-2), 17, TeleportScreen::teleportTown17, false);
        teleportButton(this.width / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * (-2), 18, TeleportScreen::teleportTown18, false);
        teleportButton(this.width / 2 - buttonWidth * (-1), (this.height - buttonHeight) / 2 - buttonHeight * (-2), 19, TeleportScreen::teleportTown19, false);
    }

    private void teleportButton(int x, int y, int townIndex, Button.IPressable button, boolean isHidden) {
        if (ClientTeleportData.getTownUnlocked(townIndex) == 1) {
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    ClientTeleportData.getTownText(townIndex), button));
        }
        else if (!isHidden){
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    new StringTextComponent("???"), TeleportScreen::townNotVisited));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
//        drawCenteredString(p_230430_1_, this.font, title, (this.width - buttonWidth) / 2, (this.height - buttonHeight) / 2 - buttonHeight * 3 - 5, 16777215);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void teleportSchool(Button button) {
        ModMessages.sendToServer(new SchoolTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleportMine(Button button) {
        ModMessages.sendToServer(new MineTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleportHome(Button button) {
        ModMessages.sendToServer(new PersonalHomeTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }

    private static void teleportClubHome(Button button) {
        ModMessages.sendToServer(new ClubHomeTeleportC2SPacket());
        Minecraft.getInstance().setScreen(null);
    }


    private static void teleportTown0(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(0));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown1(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(1));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown2(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(2));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown3(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(3));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown4(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(4));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown5(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(5));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown6(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(6));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown7(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(7));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown8(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(8));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown9(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(9));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown10(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(10));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown11(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(11));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown12(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(12));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown13(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(13));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown14(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(14));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown15(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(15));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown16(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(16));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown17(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(17));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown18(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(18));
        Minecraft.getInstance().setScreen(null);
    }
    private static void teleportTown19(Button button) {
        ModMessages.sendToServer(new TownTeleportC2SPacket(19));
        Minecraft.getInstance().setScreen(null);
    }

    private static void townNotVisited(Button button) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.town_not_visited"), false);
        }
        Minecraft.getInstance().setScreen(null);
    }

    private static void backToMenu(Button button) {
        Minecraft.getInstance().setScreen(new MenuScreen());
    }
}
