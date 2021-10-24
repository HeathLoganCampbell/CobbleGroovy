package dev.cobblesword.cobblegroovy.tools;

import dev.cobblesword.cobblegroovy.tools.item.CustomItem;
import org.bukkit.inventory.ItemStack;

public interface TripleConsumer<E, Itemstack, CustomItem>
{
    public void accept(E e, ItemStack itemstack, CustomItem customItem);
}
