package dev.cobblesword.cobblegroovy;

import dev.cobblesword.cobblegroovy.tools.CC;
import dev.cobblesword.cobblegroovy.tools.item.CustomItem;
import dev.cobblesword.cobblegroovy.tools.item.Items;
import dev.cobblesword.cobblegroovy.watcher.FileWatcher;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.plugin.ap.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;

@Plugin(name = "CobbleGroovy",
        version = "1.0",
        description = "CobbleGroovy")
public class CobbleGroovyPlugin extends ExtendedJavaPlugin
{
    @Override
    public void enable()
    {
        try {
            FileWatcher fileWatcher = new FileWatcher();
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, fileWatcher, 20, 60);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable()
    {

    }
}
