package hippofatale.jackspdmmod.home;

import net.minecraft.util.math.vector.Vector3i;

public enum HomeSize {
    SMALL(new Vector3i(20, 20, 20)),
    MEDIUM(new Vector3i(35, 25, 35)),
    LARGE(new Vector3i(50, 35, 50))
    ;

    private final Vector3i size;

    HomeSize(Vector3i size) {
        this.size = size;
    }

    public Vector3i size() {
        return size;
    }

}
