package dev.cobblesword.cobblegroovy;

import java.io.File;

public class CobbleGroovy
{
    public static File getScriptFolder()
    {
        return new File(CobbleGroovyPlugin.getPlugin(CobbleGroovyPlugin.class).getDataFolder(), "scripts");
    }
}
