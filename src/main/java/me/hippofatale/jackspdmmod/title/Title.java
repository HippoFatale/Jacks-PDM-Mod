package me.hippofatale.jackspdmmod.title;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class Title {
    private ITextComponent titleText;
    private boolean isObtainedByDefault;
    private boolean isHidden;

    public Title(ITextComponent titleText, boolean isObtainedByDefault) {
        this.titleText = titleText;
        this.isObtainedByDefault = isObtainedByDefault;
        this.isHidden = false;
    }

    public Title(ITextComponent titleText, boolean isObtainedByDefault, boolean isHidden) {
        this.titleText = titleText;
        this.isObtainedByDefault = isObtainedByDefault;
        this.isHidden = isHidden;
    }

    public ITextComponent getTitleText() {
        return titleText;
    }

    public boolean isObtainedByDefault() {
        return isObtainedByDefault;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
