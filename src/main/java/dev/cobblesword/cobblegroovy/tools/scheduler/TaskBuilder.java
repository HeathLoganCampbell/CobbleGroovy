package dev.cobblesword.cobblegroovy.tools.scheduler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;

@Builder
@Getter
public class TaskBuilder implements Runnable
{
    private Plugin plugin;
    @Setter
    private RunType runType;
    private Runnable run;
    @Setter
    private Runnable finalRun;//used on count

    private BukkitTask bukkitTask;//only set when running

    private long delay = 0l;
    private long interval = 0l;
    private long repeats = 0l;//repating x number of times before stoping

    public TaskBuilder execute()
    {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask bukkitTask = null;

        if(this.interval == 0 && this.repeats == 0)
        {
            //don't repeat
            if(this.runType == RunType.ASYNC)
                bukkitTask = scheduler.runTaskLaterAsynchronously(this.getPlugin(), this, this.delay);
            else
                bukkitTask = scheduler.runTaskLater(this.getPlugin(), this, this.delay);
        }
        else
        {
            //repeat multiple times
            if(this.runType == RunType.ASYNC)
                bukkitTask = scheduler.runTaskTimerAsynchronously(this.getPlugin(), this, this.delay, this.interval);
            else
                bukkitTask = scheduler.runTaskTimer(this.getPlugin(), this, this.delay, this.interval);
        }

        this.bukkitTask = bukkitTask;
        return this;
    }

    /**
     * cancels the task from running
     */
    public void cancel()
    {
        if(this.bukkitTask != null)
            this.bukkitTask.cancel();
    }

    protected int counts = 0;
    @Override
    public void run()
    {
        this.run.run();

        counts++;
        if(this.repeats > 0 && this.counts >= this.repeats)
        {
            if(this.finalRun != null) this.finalRun.run();
            this.cancel();
            return;
        }
    }
}