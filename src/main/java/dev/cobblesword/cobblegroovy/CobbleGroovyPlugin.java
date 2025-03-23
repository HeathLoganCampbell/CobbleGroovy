package dev.cobblesword.cobblegroovy;

import dev.cobblesword.cobblegroovy.watcher.FileWatcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class CobbleGroovyPlugin extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        try {
            FileWatcher fileWatcher = new FileWatcher();
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, fileWatcher, 20, 60);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {

    }
}
