package hippofatale.jackspdmmod.teleport;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class TeleportData {
    private static Vector3d schoolCoordinates = new Vector3d(-536, 67, -747);
    private static Vector3d mineCoordinates = new Vector3d(-924, 85, -902);

    private static List<ITextComponent> townList = Arrays.asList(new ITextComponent[]{
            //Johto
            new TranslationTextComponent("town.jackspdmmod.new_bark_town"),
            new TranslationTextComponent("town.jackspdmmod.cherrygrove_city"),
            new TranslationTextComponent("town.jackspdmmod.violet_city"),
            new TranslationTextComponent("town.jackspdmmod.azalea_town"),
            new TranslationTextComponent("town.jackspdmmod.goldenrod_city"),
            new TranslationTextComponent("town.jackspdmmod.ecruteak_city"),
            new TranslationTextComponent("town.jackspdmmod.olivine_city"),
            new TranslationTextComponent("town.jackspdmmod.cianwood_city"),
            new TranslationTextComponent("town.jackspdmmod.mahogany_town"),
            new TranslationTextComponent("town.jackspdmmod.blackthorn_city"),

            //Kanto
            new TranslationTextComponent("town.jackspdmmod.pallet_town"),
            new TranslationTextComponent("town.jackspdmmod.viridian_city"),
            new TranslationTextComponent("town.jackspdmmod.pewter_city"),
            new TranslationTextComponent("town.jackspdmmod.cerulean_city"),
            new TranslationTextComponent("town.jackspdmmod.vermilion_city"),
            new TranslationTextComponent("town.jackspdmmod.celadon_city"),
            new TranslationTextComponent("town.jackspdmmod.fuchsia_city"),
            new TranslationTextComponent("town.jackspdmmod.saffron_city"),
            new TranslationTextComponent("town.jackspdmmod.lavender_town"),
            new TranslationTextComponent("town.jackspdmmod.cinnabar_island")
    });

    private static List<Vector3d> coordinatesList = Arrays.asList(new Vector3d[]{
            //Johto
            new Vector3d(-738, 64, -534),
            new Vector3d(-217, 64, -540),
            new Vector3d(105, 64, -84),
            new Vector3d(317, 64, -742),
            new Vector3d(460, 64, -376),
            new Vector3d(405, 64, 222),
            new Vector3d(746, 64, -34),
            new Vector3d(1196, 64, -431),
            new Vector3d(-155, 64, 164),
            new Vector3d(-599, 64, 193),

            //Kanto
            new Vector3d(-1689, 64, -284),
            new Vector3d(-1694, 64, 72),
            new Vector3d(-1705, 64, 562),
            new Vector3d(-2759, 64, 774),
            new Vector3d(-2761, 64, -7),
            new Vector3d(-2360, 64, 292),
            new Vector3d(-2376, 64, -520),
            new Vector3d(-2837, 64, 308),
            new Vector3d(-3241, 64, 338),
            new Vector3d(-1673, 64, -907)
    });

    public static Vector3d getSchoolCoordinates() {
        return schoolCoordinates;
    }

    public static Vector3d getMineCoordinates() {
        return mineCoordinates;
    }

    public static ITextComponent getTownText(int townIndex) {
        return townList.get(townIndex);
    }

    public static Vector3d getTownCoordinates(int townIndex) {
        return coordinatesList.get(townIndex);
    }

    public static List<Vector3d> getCoordinatesList() {
        return coordinatesList;
    }
}
