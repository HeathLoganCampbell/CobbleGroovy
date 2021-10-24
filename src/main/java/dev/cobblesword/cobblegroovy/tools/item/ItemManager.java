package dev.cobblesword.cobblegroovy.tools.item;

import me.lucko.helper.internal.LoaderUtils;

import java.util.HashMap;

public class ItemManager
{
    public static ItemManager INSTANCE = new ItemManager();

    private HashMap<String, CustomItem> customItemByName = new HashMap<>();

    public void registerItem(CustomItem customItem)
    {
        this.customItemByName.put(customItem.getNameId(), customItem);
    }

    public CustomItem getItem(String name)
    {
        return this.customItemByName.get(name);
    }

    public void unregisterItem(String name)
    {
        this.customItemByName.remove(name);
    }
}
