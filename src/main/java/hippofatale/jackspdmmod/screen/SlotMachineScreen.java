package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.PlaySlotMachineC2SPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class SlotMachineScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.slot_machine"));
    private final int squareWidth = 80;
    private final int squareHeight = 80;
    private final List<ResourceLocation> imagePaths = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("minecraft:textures/item/apple.png"),
            new ResourceLocation("minecraft:textures/item/bone.png"),
            new ResourceLocation("minecraft:textures/item/diamond.png"),
            new ResourceLocation("minecraft:textures/item/glowstone_dust.png")
    });
    private final int imageSize = 64;
    private int reel0 = 0;
    private int reel1 = 0;
    private int reel2 = 0;
    private boolean spinReel = true;
    private long balance = 0;

    public SlotMachineScreen(int reel0, int reel1, int reel2, boolean spinReel, long balance) {
        super(new TranslationTextComponent("menu.jackspdmmod.slot_machine"));
        this.reel0 = reel0;
        this.reel1 = reel1;
        this.reel2 = reel2;
        this.spinReel = spinReel;
        this.balance = balance;
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new Button(this.width / 2 + squareWidth / 2 + 10, this.height / 2 + 10, 60, 20,
                new TranslationTextComponent("menu.jackspdmmod.slot_machine_play"), SlotMachineScreen::play));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        int leftX = this.width / 2 - 140;
        int topY = this.height / 2 - 90;
        int rightX = this.width / 2 + 140;
        int bottomY = this.height / 2 + 90;
        //background
        fill(p_230430_1_, leftX, topY - 9, rightX, bottomY, 0xff1556bc); //blue outline
        fill(p_230430_1_, leftX + 2, topY - 7, rightX - 2, bottomY, 0xff000000); //black fill
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_top_left.png"));
        blit(p_230430_1_, leftX, topY - 8,0, 0f, 0f, 16, 16, 16, 16);
        //title
        fill(p_230430_1_, this.width / 2, topY - 16, rightX - 16, topY, 0xff1556bc);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_title_left.png"));
        blit(p_230430_1_, this.width / 2 - 16, topY - 16,0, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_title_right.png"));
        blit(p_230430_1_, rightX - 16, topY - 16, 0,  0f, 0f, 16, 16, 16, 16);
        //bottom
        fill(p_230430_1_, leftX - 12, bottomY, rightX - 12, bottomY + 12, 0xff1556bc);
        fill(p_230430_1_, leftX - 10, bottomY, rightX - 12, bottomY + 10, 0xff000000);
        fill(p_230430_1_, leftX - 12, bottomY, this.width / 2 - 12, bottomY + 2, 0xff1556bc);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right.png"));
        blit(p_230430_1_, rightX - 16, bottomY,0, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center.png"));
        blit(p_230430_1_, this.width / 2 - 12, bottomY,0, 0f, 0f, 16, 16, 16, 16);
        //bottom dec
        hLine(p_230430_1_, this.width / 2, rightX - 16, bottomY + 5, 0xff1556bc); //blue arrow
        fill(p_230430_1_, leftX - 4, bottomY + 5, leftX - 2, bottomY + 7, 0xff1556bc); //dots
        fill(p_230430_1_, leftX + 4, bottomY + 5, leftX + 6, bottomY + 7, 0xff1556bc);
        fill(p_230430_1_, leftX + 12, bottomY + 5, leftX + 14, bottomY + 7, 0xff1556bc);

        drawCenteredString(p_230430_1_, this.font, title.copy().withStyle(TextFormatting.BOLD), (this.width / 2 + rightX - 16) / 2, topY - 12, 0xffffff);


        fillGradient(p_230430_1_, this.width / 2 - squareWidth / 2 - 10 - squareWidth, this.height / 2 - squareHeight,
                this.width / 2 - squareWidth / 2 - 10, this.height / 2, 0xffffffff, 0xff808080);
        fillGradient(p_230430_1_, this.width / 2 - squareWidth / 2, this.height / 2 - squareHeight,
                this.width / 2 + squareWidth / 2, this.height / 2, 0xffffffff, 0xff808080);
        fillGradient(p_230430_1_, this.width / 2 + squareWidth / 2 + 10 , this.height / 2 - squareHeight,
                this.width / 2 + squareWidth / 2 + 10 + squareWidth, this.height / 2, 0xffffffff, 0xff808080);

        drawCenteredString(p_230430_1_, this.font, new TranslationTextComponent("menu.jackspdmmod.slot_machine_cost"), this.width / 2, this.height / 2 + 14, 0xffffff);
        drawCenteredString(p_230430_1_, this.font, new TranslationTextComponent("menu.jackspdmmod.slot_machine_balance", Math.toIntExact(balance)), this.width / 2, this.height / 2 + 30, 0xffffff);

        //reels
        minecraft.getTextureManager().bind(imagePaths.get(reel0));
        blit(p_230430_1_, this.width / 2 - squareWidth / 2 - 10 - squareWidth / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);
        minecraft.getTextureManager().bind(imagePaths.get(reel1));
        blit(p_230430_1_, this.width / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);
        minecraft.getTextureManager().bind(imagePaths.get(reel2));
        blit(p_230430_1_, this.width / 2 + squareWidth / 2 + 10 + squareWidth / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);

        //rewards
        minecraft.getTextureManager().bind(new ResourceLocation("jackspdmmod:textures/gui/slot_machine_rewards.png"));
        blit(p_230430_1_, this.width / 2 - squareWidth / 2 - 10 - squareWidth, this.height / 2 + 10, 10,
                0f, 0f, 64, 64, 64, 64);
        drawString(p_230430_1_, this.font, " = ×10", this.width / 2 - squareWidth / 2 - 10 - squareWidth + 16 * 3, this.height / 2 + 14 + 16 * 0, 0xffffff);
        drawString(p_230430_1_, this.font, " = ×10", this.width / 2 - squareWidth / 2 - 10 - squareWidth + 16 * 3, this.height / 2 + 14 + 16 * 1, 0xffffff);
        drawString(p_230430_1_, this.font, " = ×10", this.width / 2 - squareWidth / 2 - 10 - squareWidth + 16 * 3, this.height / 2 + 14 + 16 * 2, 0xffffff);
        drawString(p_230430_1_, this.font, " = ×10", this.width / 2 - squareWidth / 2 - 10 - squareWidth + 16 * 3, this.height / 2 + 14 + 16 * 3, 0xffffff);

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public void tick() {
        super.tick();

        if (spinReel) {
            reel0 = (int) (Math.random() * imagePaths.size());
            reel1 = (int) (Math.random() * imagePaths.size());
            reel2 = (int) (Math.random() * imagePaths.size());
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void play(Button button) {
        ModMessages.sendToServer(new PlaySlotMachineC2SPacket());
    }
}
