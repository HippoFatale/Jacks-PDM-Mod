package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.config.ModClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class ScreenBackgrounds {
    private static final List<ResourceLocation> TOP_LEFT_TEXTURE = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/screen_top_left_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_top_left_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_top_left_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_top_left_04.png")
    });
    private static final List<ResourceLocation> TITLE_LEFT_TEXTURE = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_left_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_left_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_left_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_left_04.png")
    });
    private static final List<ResourceLocation> TITLE_RIGHT_TEXTURE = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_right_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_right_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_right_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_title_right_04.png")
    });
    private static final List<ResourceLocation> BOTTOM_RIGHT_TEXTURE = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_right_04.png")
    });
    private static final List<ResourceLocation> BOTTOM_CENTER_TEXTURE = Arrays.asList(new ResourceLocation[]{
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center_01.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center_02.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center_03.png"),
            new ResourceLocation("jackspdmmod:textures/gui/screen_bottom_center_04.png")
    });

    private static final List<Integer> COLOR = Arrays.asList(new Integer[]{
            0xff1556bc,
            0xffff882a,
            0xffe83d4a,
            0xff389e12
    });

    public static void drawReRBackground(MatrixStack matrixStack, int screenWidth, int screenHeight, int leftOffset, int rightOffset, int topOffset, int bottomOffset, FontRenderer font, ITextComponent title) {
        Minecraft minecraft = Minecraft.getInstance();

        int leftX = screenWidth / 2 - leftOffset;
        int rightX = screenWidth / 2 + rightOffset;
        int topY = screenHeight / 2 - topOffset;
        int bottomY = screenHeight / 2 + bottomOffset;

        int theme = ModClientConfig.SCREEN_THEME.get();
        int color = COLOR.get(theme);

        //background
        AbstractGui.fill(matrixStack, leftX, topY - 9, rightX, bottomY, color); //blue outline
        AbstractGui.fill(matrixStack, leftX + 2, topY - 7, rightX - 2, bottomY, 0xff000000); //black fill
        minecraft.getTextureManager().bind(TOP_LEFT_TEXTURE.get(theme));
        AbstractGui.blit(matrixStack, leftX, topY - 8,0, 0f, 0f, 16, 16, 16, 16);
        //title
        AbstractGui.fill(matrixStack, screenWidth / 2, topY - 16, rightX - 16, topY, color);
        minecraft.getTextureManager().bind(TITLE_LEFT_TEXTURE.get(theme));
        AbstractGui.blit(matrixStack, screenWidth / 2 - 16, topY - 16,0, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(TITLE_RIGHT_TEXTURE.get(theme));
        AbstractGui.blit(matrixStack, rightX - 16, topY - 16, 0,  0f, 0f, 16, 16, 16, 16);
        //bottom
        AbstractGui.fill(matrixStack, leftX - 12, bottomY, rightX - 12, bottomY + 12, color);
        AbstractGui.fill(matrixStack, leftX - 10, bottomY, rightX - 12, bottomY + 10, 0xff000000);
        AbstractGui.fill(matrixStack, leftX - 12, bottomY, screenWidth / 2 - 12, bottomY + 2, color);
        minecraft.getTextureManager().bind(BOTTOM_RIGHT_TEXTURE.get(theme));
        AbstractGui.blit(matrixStack, rightX - 16, bottomY,0, 0f, 0f, 16, 16, 16, 16);
        minecraft.getTextureManager().bind(BOTTOM_CENTER_TEXTURE.get(theme));
        AbstractGui.blit(matrixStack, screenWidth / 2 - 12, bottomY,0, 0f, 0f, 16, 16, 16, 16);
        //bottom dec
        AbstractGui.fill(matrixStack, screenWidth / 2, bottomY + 5, rightX - 16, bottomY + 6, color); //arrow
        AbstractGui.fill(matrixStack, leftX - 4, bottomY + 5, leftX - 2, bottomY + 7, color); //dots
        AbstractGui.fill(matrixStack, leftX + 4, bottomY + 5, leftX + 6, bottomY + 7, color);
        AbstractGui.fill(matrixStack, leftX + 12, bottomY + 5, leftX + 14, bottomY + 7, color);

        AbstractGui.drawCenteredString(matrixStack, font, title.copy().withStyle(TextFormatting.BOLD), (screenWidth / 2 + rightX - 16) / 2, topY - 12, 0xffffff);
    }





}
