package me.hippofatale.jackspdmmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.StarterPackageTicketSelectC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class StarterPackageTicketScreen extends Screen {
    private final ITextComponent title = new TranslationTextComponent("item.jackspdmmod.starter_package_ticket");
    private final int buttonWidth = 80;
    private final int buttonHeight = 20;

    public StarterPackageTicketScreen() {
        super(new TranslationTextComponent("item.jackspdmmod.starter_package_ticket"));
    }

    @Override
    protected void init() {
        super.init();

        addButton(new Button(width / 2 - buttonWidth - 5, height / 2, buttonWidth, buttonHeight,
                new TranslationTextComponent("item.jackspdmmod.basic_pokemon_ticket").withStyle(TextFormatting.YELLOW), StarterPackageTicketScreen::selectBasicPokemonTicket));
        addButton(new Button(width / 2 + 5, height / 2, buttonWidth, buttonHeight,
                new TranslationTextComponent("item.jackspdmmod.digimon_spawn_gacha").withStyle(TextFormatting.BLUE), StarterPackageTicketScreen::selectDigimonSpawnGacha));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        drawCenteredString(p_230430_1_, font, title, width / 2, height / 2 - 20, 16777215);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static void selectBasicPokemonTicket(Button button) {
        ModMessages.sendToServer(new StarterPackageTicketSelectC2SPacket(0));
        Minecraft.getInstance().setScreen(null);
    }

    private static void selectDigimonSpawnGacha(Button button) {
        ModMessages.sendToServer(new StarterPackageTicketSelectC2SPacket(1));
        Minecraft.getInstance().setScreen(null);
    }
}
