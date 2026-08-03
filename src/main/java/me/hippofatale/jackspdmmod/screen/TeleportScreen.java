package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.client.ClientTeleportData;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.*;
import me.hippofatale.jackspdmmod.networking.packet.ClubHomeTeleportC2SPacket;
import me.hippofatale.jackspdmmod.networking.packet.PersonalHomeTeleportC2SPacket;
import me.hippofatale.jackspdmmod.networking.packet.TeleportC2SPacket;
import me.hippofatale.jackspdmmod.teleport.TeleportManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TeleportScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.teleport"));
    private final int buttonWidth = 80;
    private final int buttonHeight = 20;

    protected TeleportScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.teleport"));
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < TeleportManager.getTeleportCount(); i++) {
            int teleportIndex = i;

            teleportButton((this.width - buttonWidth) / 2 - buttonWidth * (1 - i % 3), (this.height - buttonHeight) / 2 - buttonHeight * (3 - i / 3) - 10,
                    teleportIndex, button -> teleport(teleportIndex), false);
        }

        //homes
        this.addButton(new Button((this.width - buttonWidth) / 2 - buttonWidth * 0, (this.height - buttonHeight) / 2 - buttonHeight * -2, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_home"), TeleportScreen::teleportHome));
        this.addButton(new Button((this.width - buttonWidth) / 2 - buttonWidth * -1, (this.height - buttonHeight) / 2 - buttonHeight * -2, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport_club_home"), TeleportScreen::teleportClubHome));
    }

    private void teleportButton(int x, int y, int teleportIndex, Button.IPressable button, boolean isUnlocked) {
        if (ClientTeleportData.isTeleportUnlocked(teleportIndex)) {
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    ClientTeleportData.getTeleportName(teleportIndex), button));
        }
        else if (!isUnlocked){
            this.addButton(new Button(x, y, buttonWidth, buttonHeight,
                    new StringTextComponent("???"), TeleportScreen::teleportNotUnlocked));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        ScreenBackgrounds.drawReRBackground(p_230430_1_, width, height, 140, 140, 90, 80, font, title);
        int leftX = this.width / 2 - 140;
        int topY = this.height / 2 - 90;
        int rightX = this.width / 2 + 140;
        int bottomY = this.height / 2 + 60;

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

    private static void teleport(int index) {
        ModMessages.sendToServer(new TeleportC2SPacket(index));
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
