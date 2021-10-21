package dev.cobblesword.cobblegroovy;

import dev.cobblesword.cobblegroovy.watcher.FileWatcher;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.plugin.ap.Plugin;
import org.bukkit.Bukkit;

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
