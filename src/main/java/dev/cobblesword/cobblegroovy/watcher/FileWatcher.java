package dev.cobblesword.cobblegroovy.watcher;

import dev.cobblesword.cobblegroovy.CobbleGroovy;
import dev.cobblesword.cobblegroovy.enviroment.GroovyScript;
import dev.cobblesword.cobblegroovy.tools.CC;
import me.lucko.helper.Schedulers;
import me.lucko.helper.internal.LoaderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWatcher implements Runnable
{
    private static final WatchEvent.Kind<?>[] EVENTS = new WatchEvent.Kind[]{
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
    };

    private final WatchService watchService;
    private final List<WatchKey> watchKeys = new CopyOnWriteArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private List<Path> files = new ArrayList<>();

    private ScriptRegistry scriptRegistry;

    public FileWatcher() throws IOException {
        Path path = CobbleGroovy.getScriptFolder().toPath();

        this.scriptRegistry = new ScriptRegistry();

        this.watchService = path.getFileSystem().newWatchService();
        try (Stream<Path> dirs = Files.walk(path))
        {
            List<Path> directories = dirs.filter(Files::isDirectory).collect(Collectors.toList());
            for (Path dir : directories)
            {
                this.watchKeys.add(dir.register(this.watchService, EVENTS));
            }
        }

        // Load files in directory
        for (Path scriptPath : getScripts())
        {
            GroovyScript oldScript = this.scriptRegistry.getScript(scriptPath);
            if (Files.exists(path))
            {
                if (oldScript == null)
                {
                    GroovyScript script = new GroovyScript(scriptPath);
                    this.scriptRegistry.register(script);

                    try {
                        script.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<Path> getScripts()
    {
        File scriptsFolder = CobbleGroovy.getScriptFolder();
        try {
            return Files.walk(scriptsFolder.toPath())
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        this.lock.lock();
        try {
            reload(false);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            this.lock.unlock();
        }
    }

    private void checkWatched()
    {

    }

    public void reload(boolean runImmediately)
    {
        Set<GroovyScript> toLoad = new HashSet<>();
        Set<GroovyScript> toUnload = new HashSet<>();

        // Check for deleted files
        ScriptRegistry registry = this.scriptRegistry;

        // Check watched files

        Iterator<WatchKey> keys = this.watchKeys.iterator();
        while (keys.hasNext())
        {
            WatchKey key = keys.next();
            for (WatchEvent<?> event : key.pollEvents())
            {
                Path context = (Path) event.context();
                if (context == null) {
                    continue;
                }

                Path keyPath = (Path) key.watchable();
                Path fullPath = keyPath.resolve(context);

                if (event.context().toString().endsWith("~"))
                {
                    continue;
                }

                if (Files.isDirectory(fullPath) && !fullPath.getFileName().toString().equals("New folder"))
                {
                    System.out.println("[LOADER] New directory detected at: " + fullPath.toString());
                    continue;
                }

                GroovyScript oldScript = this.scriptRegistry.getScript(fullPath);
                if(oldScript != null)
                {
                    toUnload.add(oldScript);
                }

                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
                {
                    System.out.println("File was removed " + event.context());
                    continue;
                }

                GroovyScript script = new GroovyScript(fullPath);
                this.scriptRegistry.register(script);
                toLoad.add(script);
            }

            boolean valid = key.reset();
            if (!valid)
            {
                System.out.println("[LOADER] Watch key is no longer valid: " + key.watchable().toString());
                keys.remove();
            }
        }

        if(toUnload.size() + toUnload.size() != 0)
        {
            sendMessageToStaff(CC.info("CobbleGroovy", "Updating scripts"));
        }

        if(toUnload.size() + toUnload.size() != 0)
        {
            Schedulers.sync().run(() -> {
                // then handle unloads
                long start = System.currentTimeMillis();
                for (GroovyScript s : toUnload)
                {
                    s.close();
                    System.out.println("[LOADER] Unloaded script: " + s.getPath().toString());
                }

                if(toUnload.size() != 0)
                {
                    double millsTaken = (System.currentTimeMillis() - start);
                    sendMessageToStaff(CC.info("CobbleGroovy", millsTaken + "ms to close scripts"));
                }

                for (GroovyScript script : toLoad) {
                    try {
                        script.run();
                    } catch (Exception e) {
                        sendMessageToStaff(CC.error("CobbleGroovy", "Failure to compile " + script.getPath().getFileName()));
                        sendMessageToStaff((CC.error("CobbleGroovy", prettifyErrorMessage(e))));

                        e.printStackTrace();
                    }
                }

                double millsTaken = (System.currentTimeMillis() - start);
                sendMessageToStaff(CC.info("CobbleGroovy", millsTaken + "ms to reload"));
            });
        }
    }

    private void sendMessageToStaff(String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if(player.hasPermission("cobblegroovy.message.error"))
            {
                player.sendMessage(message);
            }
        }
    }

    private String prettifyErrorMessage(Exception e)
    {
        if(e instanceof MultipleCompilationErrorsException)
        {
            MultipleCompilationErrorsException compileEx = (MultipleCompilationErrorsException) e;
            String message = compileEx.getMessage();
            if(message.contains("Unexpected input:"))
            {
                String[] split = message.split("Unexpected input:");
                return split[0].replaceAll("\r", "");
            }
        }

        return e.getMessage();
    }
}
