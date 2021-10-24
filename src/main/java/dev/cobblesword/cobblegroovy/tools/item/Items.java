package dev.cobblesword.cobblegroovy.tools.item;

import org.bukkit.Material;

public class Items
{
    public static CustomItem create(String nameId, Material material)
    {
        CustomItem customItem = new CustomItem(nameId, material);
        ItemManager.INSTANCE.registerItem(customItem);
        return customItem;
    }
}
