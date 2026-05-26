package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.CropPriceDataSyncRequestC2SPacket;
import hippofatale.jackspdmmod.networking.packet.OpenStorageC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MenuScreen extends Screen {
    private ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.menu"));
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
        int leftX = this.width / 2 - 60;
        int topY = this.height / 2 - 65;
        int rightX = this.width / 2 + 60;
        int bottomY = this.height / 2 + 65;
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
