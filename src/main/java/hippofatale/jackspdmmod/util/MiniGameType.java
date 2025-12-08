package hippofatale.jackspdmmod.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public enum MiniGameType {
    MAGMA_FALL(new TranslationTextComponent("minigame.jackspdmmod.magma_fall").withStyle(TextFormatting.LIGHT_PURPLE)),
    JUMP_MAP_RACE(new TranslationTextComponent("minigame.jackspdmmod.jump_map_race").withStyle(TextFormatting.AQUA)),
    DICE_OF_FORTUNE(new TranslationTextComponent("minigame.jackspdmmod.dice_of_fortune").withStyle(TextFormatting.YELLOW))
    ;

    private final ITextComponent name;

    MiniGameType(ITextComponent name) {
        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }
}
