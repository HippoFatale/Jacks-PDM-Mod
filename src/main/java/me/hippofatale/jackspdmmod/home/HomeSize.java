package me.hippofatale.jackspdmmod.home;

import net.minecraft.util.math.vector.Vector3i;

import java.math.BigDecimal;

public enum HomeSize {
    SMALL(new Vector3i(20, 20, 20), new BigDecimal("100000.0")),
    MEDIUM(new Vector3i(35, 25, 35), new BigDecimal("500000.0")),
    LARGE(new Vector3i(50, 35, 50), new BigDecimal("1000000.0"))
    ;

    private final Vector3i size;
    private final BigDecimal price;

    HomeSize(Vector3i size, BigDecimal price) {
        this.size = size;
        this.price = price;
    }

    public Vector3i getSize() {
        return size;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
