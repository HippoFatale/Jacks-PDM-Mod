package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncRequestC2SPacket;
import hippofatale.jackspdmmod.networking.packet.OpenStorageC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MenuScreen extends Screen {
    private ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.menu");
    private int buttonWidth = 80;
    private int buttonHeight = 20;

    public MenuScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.menu"));
    }


    @Override
    protected void init() {
        super.init();

        this.addButton(new Button(this.width / 2 - buttonWidth / 2, this.height / 2 - 55, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.teleport"), MenuScreen::openTeleport));
        this.addButton(new Button(this.width / 2 - buttonWidth / 2, this.height / 2 - 25, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.storage"), MenuScreen::openStorage));
        this.addButton(new Button(this.width / 2 - buttonWidth / 2, this.height / 2 + 5, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.title"), MenuScreen::openTitle));
        this.addButton(new Button(this.width / 2 - buttonWidth / 2, this.height / 2 + 35, buttonWidth, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.market"), MenuScreen::openMarket));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        drawCenteredString(p_230430_1_, this.font, title, this.width / 2, this.height / 2 - 70, 16777215);
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

    private static void closeMenu(Button button) {
        Minecraft.getInstance().setScreen(null);
    }
}
