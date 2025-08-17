package hippofatale.jackspdmmod.client;

import hippofatale.jackspdmmod.screen.MenuScreen;
import hippofatale.jackspdmmod.screen.PlacardScreen;
import hippofatale.jackspdmmod.screen.StarterPackageTicketScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class ClientHooks {
    public static void openMenuScreen() {
        Minecraft.getInstance().setScreen(new MenuScreen());
    }

    public static void openStarterPackageTicketScreen() {
        Minecraft.getInstance().setScreen(new StarterPackageTicketScreen());
    }

    public static void openPlacardScreen(boolean purchased, int homeTypeIndex, int homeSizeIndex, long plotPriceLong, BlockPos placardPos) {
        Minecraft.getInstance().setScreen(new PlacardScreen(purchased, homeTypeIndex, homeSizeIndex, plotPriceLong, placardPos));
    }
}
