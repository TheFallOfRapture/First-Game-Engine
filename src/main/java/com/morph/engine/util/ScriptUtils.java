package com.morph.engine.util;

import com.morph.engine.core.Game;
import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.LoadedFont;
import com.morph.engine.script.ConsoleScript;
import com.morph.engine.script.EntityBehavior;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.script.ScriptContainer;
import com.morph.engine.script.debug.Console;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

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
    private static PublishSubject<Boolean> closeRequests = PublishSubject.create();

    private static final List<String> SCRIPT_ENGINE_EXTENSIONS = Collections.singletonList("kts");

    public static void init(Game game) {
        initTask = Single.just(game).flatMapCompletable(g -> Completable.fromCallable(() -> ScriptUtils.load(g))).cache();

        Observable<GameBehavior> loadedScripts = initTask.subscribeOn(Schedulers.io())
                .andThen(Observable.create(ScriptUtils::runReactive))
                .map(event -> Paths.get("scripts/").resolve(event.context()).getFileName().toString())
                .flatMapMaybe(ScriptUtils::getScriptBehaviorAsync);

        loadedScripts
                .filter(EntityBehavior.class::isInstance)
                .subscribe(behavior ->
                        scriptedEntities.get(behavior.getName()).forEach(entity ->
                                entity.getComponent(ScriptContainer.class).replaceBehavior(behavior.getName(), (EntityBehavior)behavior)));

        loadedScripts
                .filter(behavior -> !EntityBehavior.class.isInstance(behavior))
                .subscribe(behavior -> game.replaceBehavior(behavior.getName(), behavior));

//                .subscribe(behavior -> {
//                    if (behavior instanceof EntityBehavior) scriptedEntities.get(behavior.getName()).forEach(entity -> entity.getComponent(ScriptContainer.class).replaceBehavior(behavior.getName(), (EntityBehavior)behavior));
//                    else game.replaceBehavior(behavior.getName(), behavior);
//                });
    }

    private static boolean load(Game game) {
        Console.out.println("Morph Script Engine " + Game.VERSION_STRING + " initializing... Please wait...");
        KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory kotlinEngine = new KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory();
//        PyScriptEngineFactory pythonEngine = new PyScriptEngineFactory();

        ScriptEngineManager manager = new ScriptEngineManager();
        manager.registerEngineExtension("kts", kotlinEngine);
//        manager.registerEngineExtension("py", pythonEngine);

        supportedScriptEngines.put("kts", kotlinEngine.getScriptEngine());
//        supportedScriptEngines.put("py", pythonEngine.getScriptEngine());

        System.out.println(Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/Test.kts").getFileName());
//        System.out.println("Python support test: " + supportedScriptEngines.get("py").getFactory().getLanguageName());

        initialized = true;

        Console.out.println("[Morph Script Engine " + Game.VERSION_STRING + "] Engine initialized.");

        return true;
    }

    private static void runReactive(Emitter<WatchEvent<Path>> subscriber) {
        closeRequests.subscribe(val -> {
            if (val) isRunning = false;
        });

        isRunning = true;

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
            subscriber.onComplete();
        } catch (IOException e) {
            subscriber.onError(e);
        }
    }

    @Deprecated
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

    @Deprecated
    private static void start(Game game) {
        if (isRunning) return;

        isRunning = true;
        run(game);
    }

    public static void stop() {
        if (!isRunning) return;

        closeRequests.onNext(true);
    }

    @Deprecated
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

    public static Completable readScriptAsync(String script, String lang, Console console) {
        return getScriptEngine(lang).flatMapCompletable(engine -> Completable.fromAction(() -> readScriptDI(script, engine, console)));
    }

    private static void readScriptDI(String script, ScriptEngine engine, Console console) {
        try {
            String sanitizedScript = script.chars().mapToObj(c -> (char) c).filter(LoadedFont::isValidChar).map(String::valueOf).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append).toString();
            ConsoleScript behavior = (ConsoleScript) engine.eval(genScript(sanitizedScript), bindings);
            behavior.setConsole(console);
            behavior.run();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        bindings.clear();
    }

    public static void readScriptBlocking(String script, String lang, Console console) {
        ScriptEngine engine = getScriptEngine(lang).blockingGet();
        readScriptDI(script, engine, console);
    }

    // TODO: When initialization is finished, return all requested script behaviors.
    private static Maybe<ScriptEngine> getScriptEngine(String lang) {
        return initTask.andThen(resolveScriptEngine(lang));
    }

    private static Maybe<ScriptEngine> resolveScriptEngine(String lang) {
        if (supportedScriptEngines.containsKey(lang)) {
            return Maybe.fromCallable(() -> supportedScriptEngines.get(lang));
        } else {
            return Maybe.empty();
        }
    }

    public static Maybe<GameBehavior> getScriptBehaviorAsync(String filename) {
        return getScriptEngine(getFileExtension(filename)).observeOn(Schedulers.single()).flatMap(scriptEngine -> Maybe.just(getScriptBehaviorDI(filename, scriptEngine)));
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

    @Deprecated
    public static <T extends GameBehavior> T getScriptBehavior(String filename) {
        String extension = getFileExtension(filename);
        ScriptEngine engine = getScriptEngine(extension).blockingGet();

        return getScriptBehaviorDI(filename, engine);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static Completable getInitializationTask() {
        return initTask;
    }

    public static boolean isSupported(String lang) {
        return SCRIPT_ENGINE_EXTENSIONS.contains(lang);
    }
}
