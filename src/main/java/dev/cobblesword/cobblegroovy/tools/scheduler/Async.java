package dev.cobblesword.cobblegroovy.tools.scheduler;

import me.lucko.helper.internal.LoaderUtils;

public class Async
{
    public static Async get()
    {
        Async newAsyncObj = new Async(TaskBuilder.builder().plugin(LoaderUtils.getPlugin()).runType(RunType.ASYNC));
        return newAsyncObj;
    }

    private TaskBuilder.TaskBuilderBuilder taskBuilderBuilder;

    protected Async(TaskBuilder.TaskBuilderBuilder taskBuilderBuilder)
    {
        this.taskBuilderBuilder = taskBuilderBuilder;
        this.taskBuilderBuilder.plugin(LoaderUtils.getPlugin());
    }

    public Async interval(int ticks)
    {
        this.taskBuilderBuilder.interval(ticks);
        return this;
    }

    public Async delay(int ticks)
    {
        this.taskBuilderBuilder.delay(ticks);
        return this;
    }

    /**
     * How many times the task should be repeated
     * @param times
     * @return
     */
    public Async repeats(int times)
    {
        this.taskBuilderBuilder.repeats(times);
        return this;
    }

    public Async run(Runnable runnable)
    {
        this.taskBuilderBuilder.run(runnable);
        return this;
    }

    public Async execute()
    {
        this.taskBuilderBuilder.build().execute();
        return this;
    }


}