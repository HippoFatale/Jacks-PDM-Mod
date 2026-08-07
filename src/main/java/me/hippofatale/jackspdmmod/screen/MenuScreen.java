package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.config.ModClientConfig;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncRequestC2SPacket;
import me.hippofatale.jackspdmmod.networking.packet.OpenStorageC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class MenuScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.menu"));
    private final int buttonWidth = 80;
    private final int buttonHeight = 20;

    private final ResourceLocation themeIconPath = new ResourceLocation("jackspdmmod:textures/gui/theme_icon.png");

    public MenuScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.menu"));
    }


    @Override
    protected void init() {
        super.init();

        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 - 70, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport"), MenuScreen::openTeleport));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 - 40, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.storage"), MenuScreen::openStorage));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 - 10, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.title"), MenuScreen::openTitle));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 + 20, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.market"), MenuScreen::openMarket));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 + 50, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.theme"), MenuScreen::openTheme));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        ScreenBackgrounds.drawReRBackground(p_230430_1_, width, height, 60, 60, 80, 80, font, title);
        int leftX = width / 2 - 60;
        int topY = height / 2 - 65;
        int rightX = width / 2 + 60;
        int bottomY = height / 2 + 65;

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void openTeleport(Button button) {
        Minecraft.getInstance().setScreen(new TeleportScreen());
    }

    private static void openStorage(Button button) {
        ModMessages.sendToServer(new OpenStorageC2SPacket());
    }

    private static void openTitle(Button button) {
        Minecraft.getInstance().setScreen(new TitleScreen());
    }

    private static void openMarket(Button button) {
        ModMessages.sendToServer(new CropPriceDataSyncRequestC2SPacket());
        Minecraft.getInstance().setScreen(new MarketScreen());
    }

    private static void openTheme(Button button) {
        Minecraft.getInstance().setScreen(new ThemeSelectionScreen());
    }

    private static void closeMenu(Button button) {
        Minecraft.getInstance().setScreen(null);
    }
}
