package me.hippofatale.jackspdmmod.market;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class MarketItem {
    private final String registryName;

    private transient Item item;
    private int price;
    private final int defaultPrice;
    private int quantity;

    public MarketItem(Item item, int price, int quantity) {
        this.item = item;
        this.registryName = item.getRegistryName() != null ? item.getRegistryName().toString() : "minecraft:air";
        this.price = price;
        this.defaultPrice = price;
        this.quantity = quantity;
    }

    public void restoreItemObject() {
        this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.registryName));
    }

    public Item getItem() { return this.item; }
    public String getRegistryName() { return this.registryName; }

    public int getPrice() { return this.price; }
    public void setPrice(int price) { this.price = price; }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public int getQuantity() { return this.quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
