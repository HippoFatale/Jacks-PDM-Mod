package me.hippofatale.jackspdmmod.client;

import me.hippofatale.jackspdmmod.screen.MenuScreen;
import me.hippofatale.jackspdmmod.screen.PlacardScreen;
import me.hippofatale.jackspdmmod.screen.SlotMachineScreen;
import me.hippofatale.jackspdmmod.screen.StarterPackageTicketScreen;
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

    public static void openSlotMachineScreen(int reel0, int reel1, int reel2, boolean spinReel, long balance) {
        Minecraft.getInstance().setScreen(new SlotMachineScreen(reel0, reel1, reel2, spinReel, balance));
    }
}
