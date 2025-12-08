package hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.PlaySlotMachineC2SPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class SlotMachineScreen extends Screen {
    private final ITextComponent title = new TranslationTextComponent("menu.jackspdmmod.slot_machine");
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

    public SlotMachineScreen(int reel0, int reel1, int reel2, boolean spinReel) {
        super(new TranslationTextComponent("menu.jackspdmmod.slot_machine"));
        this.reel0 = reel0;
        this.reel1 = reel1;
        this.reel2 = reel2;
        this.spinReel = spinReel;
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new Button(this.width / 2 + 5, this.height / 2 + 20, 60, 20,
                new TranslationTextComponent("menu.jackspdmmod.slot_machine_play"), SlotMachineScreen::play));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        fillGradient(p_230430_1_, this.width / 2 - squareWidth / 2 - 10 - squareWidth, this.height / 2 - squareHeight,
                this.width / 2 - squareWidth / 2 - 10, this.height / 2, 0xffffffff, 0xff808080);
        fillGradient(p_230430_1_, this.width / 2 - squareWidth / 2, this.height / 2 - squareHeight,
                this.width / 2 + squareWidth / 2, this.height / 2, 0xffffffff, 0xff808080);
        fillGradient(p_230430_1_, this.width / 2 + squareWidth / 2 + 10 , this.height / 2 - squareHeight,
                this.width / 2 + squareWidth / 2 + 10 + squareWidth, this.height / 2, 0xffffffff, 0xff808080);

        drawCenteredString(p_230430_1_, this.font, new TranslationTextComponent("menu.jackspdmmod.slot_machine_cost"), this.width / 2 - 35, this.height / 2 + 25, 16777215);

        minecraft.getTextureManager().bind(imagePaths.get(reel0));
        blit(p_230430_1_, this.width / 2 - squareWidth / 2 - 10 - squareWidth / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);
        minecraft.getTextureManager().bind(imagePaths.get(reel1));
        blit(p_230430_1_, this.width / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);
        minecraft.getTextureManager().bind(imagePaths.get(reel2));
        blit(p_230430_1_, this.width / 2 + squareWidth / 2 + 10 + squareWidth / 2 - imageSize / 2, this.height / 2 - squareHeight / 2 - imageSize / 2, 10,
                0f, 0f, imageSize, imageSize, imageSize, imageSize);

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
