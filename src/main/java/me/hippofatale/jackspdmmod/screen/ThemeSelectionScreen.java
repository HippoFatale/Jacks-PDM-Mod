package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.config.ModClientConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThemeSelectionScreen extends Screen {
    private final ITextComponent title = new StringTextComponent("· ").append(new TranslationTextComponent("menu.jackspdmmod.theme"));
    private final int buttonWidth = 80;
    private final int buttonHeight = 20;

    public final List<ResourceLocation> themeIconPaths = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/theme_icon_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/theme_icon_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/theme_icon_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/theme_icon_04.png")
    });

    protected ThemeSelectionScreen() {
        super(new TranslationTextComponent("menu.jackspdmmod.theme"));
    }

    @Override
    protected void init() {
        super.init();

        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 - 55, buttonWidth, buttonHeight,
                new TranslationTextComponent("theme.jackspdmmod.devil").withStyle(TextFormatting.BLUE),
                button -> {
                    ModClientConfig.SCREEN_THEME.set(0);
                    ModClientConfig.SPEC.save();
                }));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 - 25, buttonWidth, buttonHeight,
                new TranslationTextComponent("theme.jackspdmmod.joker").withStyle(TextFormatting.GOLD),
                button -> {
                    ModClientConfig.SCREEN_THEME.set(1);
                    ModClientConfig.SPEC.save();
                }));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 + 5, buttonWidth, buttonHeight,
                new TranslationTextComponent("theme.jackspdmmod.blagon").withStyle(TextFormatting.RED),
                button -> {
                    ModClientConfig.SCREEN_THEME.set(2);
                    ModClientConfig.SPEC.save();
                }));
        addButton(new Button(width / 2 - buttonWidth / 2, height / 2 + 35, buttonWidth, buttonHeight,
                new TranslationTextComponent("theme.jackspdmmod.soteria").withStyle(TextFormatting.GREEN),
                button -> {
                    ModClientConfig.SCREEN_THEME.set(3);
                    ModClientConfig.SPEC.save();
                }));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        ScreenBackgrounds.drawReRBackground(p_230430_1_, width, height, 60, 60, 65, 65, font, title);
        int leftX = width / 2 - 60;
        int topY = height / 2 - 65;
        int rightX = width / 2 + 60;
        int bottomY = height / 2 + 65;

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
