package com.morph.engine.util;

import com.morph.engine.core.Game;
import com.morph.engine.script.debug.Console;
import com.morph.engine.entities.Entity;
import com.morph.engine.script.ConsoleScript;
import com.morph.engine.script.EntityBehavior;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.script.ScriptContainer;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;
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

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created on 7/5/2017.
 */
public class ScriptUtils {
    private static HashMap<String, ScriptEngine> supportedScriptEngines = new HashMap<>();
    private static HashMap<String, List<Entity>> scriptedEntities = new HashMap<>();
    private static SimpleBindings bindings = new SimpleBindings();
    private static boolean isRunning;
    private static boolean initialized;
    private static Completable initTask;
    private static Flowable<GameBehavior> scriptUpdateTask;
    private static Disposable scriptUpdaterHandle;
    private static Game game;

    public static void init(Game game) {
        initTask = Single.just(game).observeOn(Schedulers.single()).flatMapCompletable(g -> Completable.fromCallable(() -> ScriptUtils.load(g)));
        initTask.subscribe(() -> scriptUpdaterHandle = Flowable.create(ScriptUtils::runReactive, BackpressureStrategy.BUFFER)
                .map(event -> Paths.get("scripts/").resolve(event.context()).getFileName().toString())
                .flatMapSingle(ScriptUtils::getScriptBehaviorAsync)
                .subscribe(behavior -> {
                    if (behavior instanceof EntityBehavior) scriptedEntities.get(behavior.getName()).forEach(entity -> entity.getComponent(ScriptContainer.class).replaceBehavior(behavior.getName(), (EntityBehavior)behavior));
                    else game.replaceBehavior(behavior.getName(), behavior);
                }));
    }

    public static boolean load(Game game) {
        ScriptUtils.game = game;

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

        initialized = true;

        return true;
    }

    public static void stopRx() {
        scriptUpdaterHandle.dispose();
    }

    private static void runReactive(FlowableEmitter<WatchEvent<Path>> subscriber) {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY);
            while (isRunning) {
                WatchKey key;
                if ((key = watchService.poll()) != null) {
                    for (WatchEvent e : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = e.kind();

                        if (kind == OVERFLOW) continue;

                        WatchEvent<Path> event = (WatchEvent<Path>)e;
                        subscriber.onNext(event);
                    }

                    key.reset();
                }
            }
        } catch (IOException e) {
            subscriber.onError(e);
        }
    }

    private static Flowable<GameBehavior> runRx(Game game) {
        return Flowable.create(emitter -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY);
                while (true) {
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

                            emitter.onNext(newBehavior);
                        }

                        key.reset();
                    }
                }
            } catch (IOException e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
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
    }

    private static void start(Game game) {
        if (isRunning) return;

        isRunning = true;
        run(game);
    }

    public static void stop() {
        if (!isRunning) return;

        isRunning = false;
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

    public static Single<Object> readScriptAsync(String script, String lang, Console console) {
        return getScriptEngine(lang).map(engine -> readScriptDI(script, engine, console));
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
        ScriptEngine engine = getScriptEngine(lang).blockingGet();

        Object result = readScriptDI(script, engine, console);

        bindings.clear();
        return result;
    }

    // TODO: When initialization is finished, return all requested script behaviors.
    private static Single<ScriptEngine> getScriptEngine(String lang) {
        SingleSubject<ScriptEngine> engineSingle = SingleSubject.create();
        initTask.doOnComplete(() -> engineSingle.onSuccess(supportedScriptEngines.get(lang)));
        return engineSingle;
    }

    public static Single<GameBehavior> getScriptBehaviorAsync(String filename) {
        return getScriptEngine(getFileExtension(filename)).flatMap(scriptEngine -> Single.just(getScriptBehaviorDI(filename, scriptEngine)));
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
            behavior.setName(filename);
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
        ScriptEngine engine = getScriptEngine(extension).blockingGet();

        T behavior = getScriptBehaviorDI(filename, engine);

        return behavior;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static Flowable<GameBehavior> getScriptUpdateTask() {
        return scriptUpdateTask;
    }

    public static Completable getInitializationTask() {
        return initTask;
    }
}
