package dev.cobblesword.cobblegroovy.tools;

import org.bukkit.inventory.ItemStack;

public interface TripleConsumer<E, Itemstack, CustomItem>
{
    public void accept(E e, ItemStack itemstack, CustomItem customItem);
}
