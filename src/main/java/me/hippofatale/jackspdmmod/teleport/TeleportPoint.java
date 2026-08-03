package me.hippofatale.jackspdmmod.teleport;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

public class TeleportPoint {
    private ITextComponent teleportName;
    private Vector3d teleportPos;
    private boolean isOpenByDefault;

    public TeleportPoint(ITextComponent teleportName, Vector3d teleportPos, boolean isOpenByDefault) {
        this.teleportName = teleportName;
        this.teleportPos = teleportPos;
        this.isOpenByDefault = isOpenByDefault;
    }

    public ITextComponent getTeleportName() {
        return teleportName;
    }

    public Vector3d getTeleportPos() {
        return teleportPos;
    }

    public boolean isOpenByDefault() {
        return isOpenByDefault;
    }
}
