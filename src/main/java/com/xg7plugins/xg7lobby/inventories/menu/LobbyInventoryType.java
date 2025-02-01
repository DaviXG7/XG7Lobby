package com.xg7plugins.xg7lobby.inventories.menu;

import org.bukkit.event.inventory.InventoryType;

public enum LobbyInventoryType {

    NORMAL,
    SELECTOR,

    CHEST,
    DISPENSER,
    DROPPER,
    FURNACE,
    WORKBENCH,
    CRAFTING,
    ENCHANTING,
    BREWING,
    PLAYER,
    CREATIVE,
    MERCHANT,
    ENDER_CHEST,
    ANVIL,
    SMITHING,
    BEACON,
    HOPPER,
    SHULKER_BOX,
    BARREL,
    BLAST_FURNACE,
    LECTERN,
    SMOKER,
    LOOM,
    CARTOGRAPHY,
    GRINDSTONE,
    STONECUTTER;

    public InventoryType toBukkitInventoryType() {
        if (this == NORMAL || this == SELECTOR) return InventoryType.CHEST;
        return InventoryType.valueOf(name());
    }

}
