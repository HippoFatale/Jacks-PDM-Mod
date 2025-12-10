package hippofatale.jackspdmmod.teleport;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class TeleportData {
    private static final List<ITextComponent> teleportNameList = Arrays.asList(new ITextComponent[]{
            //default
            new TranslationTextComponent("teleport.jackspdmmod.town_hall"),
            new TranslationTextComponent("teleport.jackspdmmod.stadium"),
            new TranslationTextComponent("teleport.jackspdmmod.mine"),
            new TranslationTextComponent("teleport.jackspdmmod.casino"),

            //towns
            new TranslationTextComponent("teleport.jackspdmmod.pokemon_lab"),
            new TranslationTextComponent("teleport.jackspdmmod.swamp_town"),
            new TranslationTextComponent("teleport.jackspdmmod.snowy_mountain_town"),
            new TranslationTextComponent("teleport.jackspdmmod.dancing_flower_town"),
            new TranslationTextComponent("teleport.jackspdmmod.anicent_jungle_town"),
            new TranslationTextComponent("teleport.jackspdmmod.burning_town"),
            new TranslationTextComponent("teleport.jackspdmmod.desert_town"),
            new TranslationTextComponent("teleport.jackspdmmod.savannah_field_town"),
            new TranslationTextComponent("teleport.jackspdmmod.deep_forest_town"),
            new TranslationTextComponent("teleport.jackspdmmod.sky_island"),
            new TranslationTextComponent("teleport.jackspdmmod.forgotten_island")
    });

    private static final List<Vector3d> teleportCoordinatesList = Arrays.asList(new Vector3d[]{
            //default
            new Vector3d(-93, 44, -8),
            new Vector3d(213, 39, -394),
            new Vector3d(-423, 45, 1359),
            new Vector3d(0, 0, 0), //TODO update casino coordinates

            //towns
            new Vector3d(-229, 38.5, -408),
            new Vector3d(667, 52, -597),
            new Vector3d(825, 46.5, 125),
            new Vector3d(636, 42, 780),
            new Vector3d(-108, 41, 723),
            new Vector3d(-687, 70, 422),
            new Vector3d(-824, 74.5, -84),
            new Vector3d(-727, 43.5, -502),
            new Vector3d(-172, 41.5, -725),
            new Vector3d(1, 62, -973),
            new Vector3d(-660, 39, 1140)
    });

    public static ITextComponent getTeleportName(int teleportIndex) {
        return teleportNameList.get(teleportIndex);
    }

    public static Vector3d getTeleportCoordinates(int teleportIndex) {
        return teleportCoordinatesList.get(teleportIndex);
    }

    public static List<Vector3d> getTeleportCoordinatesList() {
        return teleportCoordinatesList;
    }
}
