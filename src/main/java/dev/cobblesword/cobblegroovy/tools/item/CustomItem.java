package dev.cobblesword.cobblegroovy.tools.item;

import dev.cobblesword.cobblegroovy.tools.TripleConsumer;
import dev.cobblesword.cobblegroovy.tools.ItemStackBuilder;
import dev.cobblesword.cobblegroovy.tools.Keys;
import lombok.Getter;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.Terminable;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomItem implements Terminable
{
    protected final CompositeTerminable compositeTerminable = CompositeTerminable.create();

    @Getter
    private String nameId;
    private ItemStackBuilder itemStackBuilder;

    public CustomItem(String nameId, Material material)
    {
        this.nameId = nameId;
        this.itemStackBuilder = new ItemStackBuilder(material);
        this.itemStackBuilder.withNBT(Keys.NAME_ID, nameId);

        ItemManager.INSTANCE.registerItem(this);
    }

    public CustomItem displayName(String name)
    {
        this.itemStackBuilder.displayName(name);
        return this;
    }

    public <T extends Event> CustomItem subscribe(Class<T> eventClass, TripleConsumer<T, ItemStack, CustomItem> eventHandler)
    {
        Events.subscribe(eventClass)
                .handler(e -> {
                    Player player = null;
                    ItemStack item = null;

                    if(e instanceof PlayerEvent)
                    {
                        player = ((PlayerEvent) e).getPlayer();
                        item = player.getInventory().getItemInMainHand();
                        if(item == null)
                        {
                            return;
                        }

                        if(!item.hasItemMeta())
                        {
                            return;
                        }
                    }

                    if(e instanceof EntityDamageByEntityEvent)
                    {
                        EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e;
                        Entity attacker = edbee.getDamager();

                        if(attacker instanceof Player)
                        {
                            player = ((Player) attacker);
                            item = player.getInventory().getItemInMainHand();
                            if(item == null)
                            {
                                return;
                            }

                            if(!item.hasItemMeta())
                            {
                                return;
                            }
                        }
                    }
                    else if(e instanceof EntityDamageEvent)
                    {
                        EntityDamageEvent ede = (EntityDamageEvent) e;
                        Entity entity = ede.getEntity();
                        if(entity instanceof Player)
                        {
                            player = ((Player) entity);
                            item = player.getInventory().getItemInMainHand();
                            if(item == null)
                            {
                                return;
                            }

                            if(!item.hasItemMeta())
                            {
                                return;
                            }
                        }
                    }

                    ItemMeta itemMeta = item.getItemMeta();

                    if(itemMeta == null)
                    {
                        return;
                    }

                    if(!item.hasItemMeta())
                    {
                        return;
                    }

                    if(!itemMeta.getPersistentDataContainer().has(Keys.NAME_ID, PersistentDataType.STRING))
                    {
                        return;
                    }

                    String nameId = itemMeta.getPersistentDataContainer().get(Keys.NAME_ID, PersistentDataType.STRING);
                    CustomItem customItem = ItemManager.INSTANCE.getItem(nameId);

                    if(customItem != null)
                    {
                        eventHandler.accept(e, item, customItem);
                    }
                }).bindWith(compositeTerminable);

        return this;
    }

    public ItemStack getItemStack()
    {
        return this.itemStackBuilder.build();
    }

    @Override
    public void close() throws Exception
    {
        ItemManager.INSTANCE.unregisterItem(this.getNameId());
        this.compositeTerminable.closeAndReportException();
    }
}
