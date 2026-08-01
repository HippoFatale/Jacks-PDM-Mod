package me.hippofatale.jackspdmmod.config;

import me.hippofatale.jackspdmmod.screen.ScreenBackgrounds;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue SCREEN_THEME;

    static {
        BUILDER.push("Client UI Settings");

        SCREEN_THEME = BUILDER.comment("GUI Theme ID").defineInRange("screenThemeId", 0, 0, 3);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
