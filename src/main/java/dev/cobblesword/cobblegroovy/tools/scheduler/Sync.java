package dev.cobblesword.cobblegroovy.tools.scheduler;

import me.lucko.helper.internal.LoaderUtils;

public class Sync
{
    public static Sync get()
    {
        Sync newAsyncObj = new Sync(TaskBuilder.builder().runType(RunType.SYNC).plugin(LoaderUtils.getPlugin()));
        return newAsyncObj;
    }

    private TaskBuilder.TaskBuilderBuilder taskBuilderBuilder;

    protected Sync(TaskBuilder.TaskBuilderBuilder taskBuilderBuilder)
    {
        this.taskBuilderBuilder = taskBuilderBuilder;
    }

    public Sync interval(int ticks)
    {
        this.taskBuilderBuilder.interval(ticks);
        return this;
    }

    public Sync delay(int ticks)
    {
        this.taskBuilderBuilder.delay(ticks);
        return this;
    }

    /**
     * How many times the task should be repeated
     * @param times
     * @return
     */
    public Sync repeats(int times)
    {
        this.taskBuilderBuilder.repeats(times);
        return this;
    }

    public Sync run(Runnable runnable)
    {
        this.taskBuilderBuilder.run(runnable);
        return this;
    }

    public Sync finalRun(Runnable runnable)
    {
        this.taskBuilderBuilder.finalRun(runnable);
        return this;
    }

    public Sync execute()
    {
        this.taskBuilderBuilder.build().execute();
        return this;
    }


}