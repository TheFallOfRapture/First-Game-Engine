package com.morph.engine.util;

import com.morph.engine.core.Game;
import com.morph.engine.debug.Console;
import com.morph.engine.entities.Entity;
import com.morph.engine.script.ConsoleScript;
import com.morph.engine.script.EntityBehavior;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.script.ScriptContainer;
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory;
import org.python.jsr223.PyScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created on 7/5/2017.
 */
public class ScriptUtils {
    private static HashMap<String, ScriptEngine> supportedScriptEngines = new HashMap<>();
    private static HashMap<String, List<Entity>> scriptedEntities = new HashMap<>();
    private static SimpleBindings bindings = new SimpleBindings();
    private static boolean isRunning;
    private static Thread scriptUpdateThread;
    private static boolean initialized;
    private static CompletableFuture<Void> initTask;

    public static boolean init(Game game) {
        Console.out.println("Morph Script Engine 0.5.15 initializing... Please wait...");
        KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory kotlinEngine = new KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory();
        PyScriptEngineFactory pythonEngine = new PyScriptEngineFactory();

        ScriptEngineManager manager = new ScriptEngineManager();
        manager.registerEngineExtension("kts", kotlinEngine);
        manager.registerEngineExtension("py", pythonEngine);

        supportedScriptEngines.put("kts", kotlinEngine.getScriptEngine());
        supportedScriptEngines.put("py", pythonEngine.getScriptEngine());

        System.out.println(Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/Test.kts").getFileName());
        System.out.println("Python support test: " + supportedScriptEngines.get("py").getFactory().getLanguageName());

        scriptUpdateThread = new Thread(() -> start(game), "Script Update Thread");
        scriptUpdateThread.start();

        initialized = true;

        return true;
    }

    private static void run(Game game) {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey key = Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY);
            while (isRunning) {
                pollEvents(watchService, game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void start(Game game) {
        if (isRunning) return;

        isRunning = true;
        run(game);
    }

    public static void stop() {
        if (!isRunning) return;

        isRunning = false;
    }

    public static synchronized void stopPolling() {
        try {
            scriptUpdateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pollEvents(WatchService watchService, Game game) {
        WatchKey key;
        if ((key = watchService.poll()) != null) {
            for (WatchEvent e : key.pollEvents()) {
                WatchEvent.Kind<?> kind = e.kind();

                if (kind == OVERFLOW) continue;

                WatchEvent<Path> event = (WatchEvent<Path>)e;
                Path filename = event.context();

                Path file = Paths.get("scripts/").resolve(filename);
                String simpleName = file.getFileName().toString();

                System.out.println(simpleName);
                GameBehavior newBehavior = getScriptBehavior(simpleName);

                if (newBehavior instanceof EntityBehavior) scriptedEntities.get(simpleName).forEach(entity -> entity.getComponent(ScriptContainer.class).replaceBehavior(simpleName, (EntityBehavior)newBehavior));
                else game.replaceBehavior(simpleName, newBehavior);
            }

            key.reset();
        }
    }

    public static void register(String scriptName, Entity e) {
        if (scriptedEntities.get(scriptName) == null) {
            List<Entity> eList = new ArrayList<>();
            eList.add(e);
            scriptedEntities.put(scriptName, eList);
        } else {
            scriptedEntities.get(scriptName).add(e);
        }
    }

    private static String genScript(String script) {
        return "import com.morph.engine.script.ConsoleScript\n" +
               "class CustomScript : ConsoleScript() {\n" +
               "    override fun run() = " + script + "\n" +
               "}\n" +
               "CustomScript()" ;
    }

    public static CompletableFuture<Object> readScriptAsync(String script, String lang, Console console) {
        return getScriptEngine(lang).thenApply(engine -> readScriptDI(script, engine, console));
    }

    private static Object readScriptDI(String script, ScriptEngine engine, Console console) {
        Object result = null;

        try {
            ConsoleScript behavior = (ConsoleScript) engine.eval(genScript(script), bindings);
            behavior.setConsole(console);
            behavior.run();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        bindings.clear();
        return result;
    }

    public static Object readScript(String script, String lang, Console console) {
        ScriptEngine engine = null;
        try {
            engine = getScriptEngine(lang).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Object result = readScriptDI(script, engine, console);

        bindings.clear();
        return result;
    }

    public static CompletableFuture<Void> launchInitializationTask(Game game) {
        return initTask = CompletableFuture.runAsync(() -> ScriptUtils.init(game));
    }


    // TODO: When initialization is finished, return all requested script behaviors.
    private static CompletableFuture<ScriptEngine> getScriptEngine(String lang) {
        return initTask.thenApply((a) -> supportedScriptEngines.get(lang));
    }

    public static <T extends GameBehavior> CompletableFuture<T> getScriptBehaviorAsync(String filename) {
        return getScriptEngine(getFileExtension(filename)).thenApply(engine -> getScriptBehaviorDI(filename, engine));
    }

    private static <T extends GameBehavior> T getScriptBehaviorDI(String filename, ScriptEngine engine) {
        String scriptSource = "";
        String fullFilename = System.getProperty("user.dir") + "/src/main/resources/scripts/" + filename;

        String extension = fullFilename.substring(fullFilename.indexOf(".") + 1);

        try {
            scriptSource = IOUtils.getFileAsStringAbsolute(fullFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        T behavior = null;

        try {
            behavior = (T) engine.eval(scriptSource, bindings);
            if (behavior == null) {
                System.out.println("No result from eval, getting script variable");
                behavior = (T) bindings.get("script");
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        bindings.clear();

        return behavior;
    }

    private static String getFileExtension(String filename) {
        String fullFilename = System.getProperty("user.dir") + "/src/main/resources/scripts/" + filename;
        return fullFilename.substring(fullFilename.indexOf(".") + 1);
    }

    public static <T extends GameBehavior> T getScriptBehavior(String filename) {
        String extension = getFileExtension(filename);
        ScriptEngine engine = null;
        try {
            engine = getScriptEngine(extension).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        T behavior = getScriptBehaviorDI(filename, engine);

        return behavior;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
