package dev.cobblesword.cobblegroovy;

import me.lucko.helper.internal.LoaderUtils;

import java.io.File;

public class CobbleGroovy
{
    public static File getScriptFolder()
    {
        return new File(LoaderUtils.getPlugin().getDataFolder(), "scripts");
    }
}
