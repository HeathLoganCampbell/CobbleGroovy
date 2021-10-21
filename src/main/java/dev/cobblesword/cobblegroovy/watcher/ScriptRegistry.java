package dev.cobblesword.cobblegroovy.watcher;

import dev.cobblesword.cobblegroovy.enviroment.GroovyScript;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScriptRegistry
{
    private Map<Path, GroovyScript> scripts = new HashMap<>();

    public void register(GroovyScript script)
    {
        this.scripts.put(script.getPath(), script);
    }

    public void unregister(GroovyScript script)
    {
        this.scripts.remove(script.getPath());
    }

    public GroovyScript getScript(Path path)
    {
        return this.scripts.get(path);
    }

    public Map<Path, GroovyScript> getAll()
    {
        return Collections.unmodifiableMap(this.scripts);
    }

    public void close()
    {
        for (GroovyScript script : this.scripts.values())
        {
            script.close();
        }
    }
}
