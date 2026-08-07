package me.hippofatale.jackspdmmod.teleport;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TeleportManager {
    private static final List<TeleportPoint> teleportList = Arrays.asList(new TeleportPoint[] {
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.mine"),
                new Vector3d(-794, 80, 2048), true),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.field"),
                new Vector3d(-117, 171, 1498), true),

        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.revolution_city"),
                new Vector3d(13, 69, 964), true),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.revolution_city_north"),
                new Vector3d(9, 69, 821), true),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.revolution_city_south"),
                new Vector3d( -71, 69, 1084), true),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.revolution_city_west"),
                new Vector3d(-130, 69, 963), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.revolution_city_east"),
                new Vector3d(146, 69, 1010), false),

        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.start_town"),
                new Vector3d(-201, 65, 242), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.spooky_town"),
                new Vector3d(276, 84, 756), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.flutter_town"),
                new Vector3d(-123, 64, 652), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.wave_town"),
                new Vector3d(685, 70, 811), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.water_drop_town"),
                new Vector3d(682, 82,606), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.forest_town"),
                new Vector3d(756, 70, 1033), false),
        new TeleportPoint(new TranslationTextComponent("teleport.jackspdmmod.fly_town"),
                new Vector3d(648, 72, 1072), false),
    });

    public static ITextComponent getTeleportName(int teleportIndex) {
        return teleportList.get(teleportIndex).getTeleportName();
    }

    public static Vector3d getTeleportPos(int teleportIndex) {
        return teleportList.get(teleportIndex).getTeleportPos();
    }

    public static List<Vector3d> getTeleportPosList() {
        return teleportList.stream().map(TeleportPoint::getTeleportPos).collect(Collectors.toList());
    }

    public static int getTeleportCount() {
        return teleportList.size();
    }

    public static boolean isOpenByDefault(int teleportIndex) {
        return teleportList.get(teleportIndex).isOpenByDefault();
    }
}
