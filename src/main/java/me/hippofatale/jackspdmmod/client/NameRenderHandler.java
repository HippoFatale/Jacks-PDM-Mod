package me.hippofatale.jackspdmmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.hippofatale.jackspdmmod.JacksPDMMod;
import me.hippofatale.jackspdmmod.rank.RankManager;
import me.hippofatale.jackspdmmod.title.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JacksPDMMod.MOD_ID)
public class NameRenderHandler {

    @SubscribeEvent
    public static void onRenderPlayerName(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity)) {
            return;
        }

        net.minecraft.entity.player.PlayerEntity player = (net.minecraft.entity.player.PlayerEntity) event.getEntity();

        int rankPoints = ClientRankPointData.getRankPoints(player.getUUID());
        ResourceLocation rankTexture = RankManager.getRankTexture(rankPoints);

        MatrixStack matrixStack = event.getMatrixStack();
        IRenderTypeBuffer buffer = event.getBuffers();

        // Check if title is displayed
        int titleIndex = ClientTitleData.getPlayerTitleIndex(player.getUUID());
        boolean hasTitle = titleIndex != 0;
        
        // Render rank texture
        matrixStack.pushPose();
        // Position rank above name (or above title if title exists)
        float rankY = hasTitle ? player.getBbHeight() + 1.1f : player.getBbHeight() + 0.5f; //TODO adjust Y
        matrixStack.translate(0.0, rankY, 0.0);
        matrixStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        matrixStack.scale(-0.1F, -0.1F, 0.1F);

        // Try rendering with the buffer system
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(rankTexture));

        float width = 2.0f;
        float height = 2.0f;
        float x = - width / 2;
        float y = - height / 2;

        vertexBuilder.vertex(matrixStack.last().pose(), x, y + height, 0.0f)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .uv(0.0f, 1.0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(240, 240)
            .normal(0.0f, 0.0f, 1.0f)
            .endVertex();

        vertexBuilder.vertex(matrixStack.last().pose(), x + width, y + height, 0.0f)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .uv(1.0f, 1.0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(240, 240)
            .normal(0.0f, 0.0f, 1.0f)
            .endVertex();

        vertexBuilder.vertex(matrixStack.last().pose(), x + width, y, 0.0f)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .uv(1.0f, 0.0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(240, 240)
            .normal(0.0f, 0.0f, 1.0f)
            .endVertex();

        vertexBuilder.vertex(matrixStack.last().pose(), x, y, 0.0f)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .uv(0.0f, 0.0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(240, 240)
            .normal(0.0f, 0.0f, 1.0f)
            .endVertex();

        matrixStack.popPose();

        // Render title text above character name
        if (hasTitle) {
            ITextComponent titleText = TitleManager.getTitleBold(titleIndex);
            
            matrixStack.pushPose();
            matrixStack.translate(0.0, player.getBbHeight() + 0.9, 0.0); //TODO adjust Y
            matrixStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            matrixStack.scale(-0.02F, -0.02F, 0.02F);
            
            float textWidth = Minecraft.getInstance().font.width(titleText);
            float textX = -textWidth / 2.0f;
            
            Minecraft.getInstance().font.drawInBatch(
                titleText,
                textX,
                0.0f,
                0xFFFFFF,
                true,
                matrixStack.last().pose(),
                buffer,
                false,
                0,
                15728880
            );
            
            matrixStack.popPose();
        }
    }
}
