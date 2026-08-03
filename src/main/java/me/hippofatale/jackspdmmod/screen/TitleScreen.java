package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.client.ClientTitleData;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.TitleDisplayC2SPacket;
import me.hippofatale.jackspdmmod.title.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TitleScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.title"));
    private int pageOffset = 0;
    private static final int TITLES_PER_PAGE = 5;

    protected TitleScreen() {
        this(0);
    }

    protected TitleScreen(int pageOffset) {
        super(new TranslationTextComponent("menu.jackspdmmod.title"));
        this.pageOffset = pageOffset;
    }

    @Override
    protected void init() {
        super.init();

        buttons.clear();
        children.clear();

        int buttonWidth = 120;
        int buttonHeight = 20;
        int startY = height / 2 - 100;
        int spacing = 25;

        int startIndex = 1 + pageOffset * TITLES_PER_PAGE;
        int buttonRow = 0;

        for (int i = startIndex; i < TitleManager.getTitleCount() && buttonRow < TITLES_PER_PAGE; i++) {
            // Skip only unobtained hidden titles
            if (TitleManager.isHidden(i) && !ClientTitleData.isTitleUnlocked(i)) {
                continue;
            }

            int titleIndex = i;
            if (ClientTitleData.isTitleUnlocked(i)) {
                // Obtained titles: show button with title
                addButton(new Button(width / 2 - buttonWidth / 2, startY + spacing * (buttonRow + 1), buttonWidth, buttonHeight,
                        ClientTitleData.getTitleText(i),
                        (b) -> displayTitle(titleIndex)));
            } else {
                // Unobtained not hidden titles: show button with "???"
                addButton(new Button(width / 2 - buttonWidth / 2, startY + spacing * (buttonRow + 1), buttonWidth, buttonHeight,
                        new StringTextComponent("???"),
                        (b) -> doesNotHaveTitle()));
            }
            buttonRow++;
        }

        int navY = startY + spacing * (TITLES_PER_PAGE + 1);

        addButton(new Button(width / 2 - 30, navY, 60, buttonHeight,
                new TranslationTextComponent("menu.jackspdmmod.remove_title"), TitleScreen::removeTitle));

        if (pageOffset > 0) {
            addButton(new Button(width / 2 - 60, navY, 20, 20,
                    new StringTextComponent("<"), (b) -> changePage(-1)));
        }

        // Check if there are more visible titles beyond current page
        int visibleCount = 0;
        for (int i = startIndex; i < TitleManager.getTitleCount(); i++) {
            // Skip only unobtained hidden titles
            if (TitleManager.isHidden(i) && !ClientTitleData.isTitleUnlocked(i)) {
                continue;
            }
            visibleCount++;
            if (visibleCount > TITLES_PER_PAGE) {
                addButton(new Button(width / 2 + 40, navY, 20, 20,
                        new StringTextComponent(">"), (b) -> changePage(1)));
                break;
            }
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ScreenBackgrounds.drawReRBackground(matrixStack, width, height, 80, 80, 85, 80, font, title);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void removeTitle(Button button) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(0));
        Minecraft.getInstance().setScreen(null);
    }

    private static void displayTitle(int titleIndex) {
        ModMessages.sendToServer(new TitleDisplayC2SPacket(titleIndex));
        Minecraft.getInstance().setScreen(null);
    }

    private static void doesNotHaveTitle() {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.does_not_have_title"), false);
        }
    }

    private void changePage(int delta) {
        pageOffset += delta;
        init();
    }
}
