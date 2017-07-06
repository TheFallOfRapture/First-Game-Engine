package com.morph.engine.util;

import com.morph.engine.core.Game;
import com.morph.engine.script.GameBehavior;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created on 7/5/2017.
 */
public class ScriptUtils {
    private static HashMap<String, ScriptEngine> supportedScriptEngines;
    private static SimpleBindings bindings;
    private static WatchService watchService;

    public static void init(Game game) {
        supportedScriptEngines = new HashMap<>();
        bindings = new SimpleBindings();
        supportedScriptEngines.put("kts", new ScriptEngineManager().getEngineByExtension("kts"));

        System.out.println(Paths.get(ScriptUtils.class.getClassLoader()
                .getResource("scripts/Test.kts")
                .getPath().replaceFirst("^/(.:/)", "$1")));

        try {
            watchService = FileSystems.getDefault().newWatchService();
            WatchKey key = Paths.get(ScriptUtils.class.getClassLoader().getResource("scripts/").getPath().replaceFirst("^/(.:/)", "$1")).register(watchService, ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pollEvents(Game game) {
        WatchKey key;
        if ((key = watchService.poll()) != null) {
            for (WatchEvent e : key.pollEvents()) {
                WatchEvent.Kind<?> kind = e.kind();

                if (kind == OVERFLOW) continue;

                WatchEvent<Path> event = (WatchEvent<Path>)e;
                Path filename = event.context();

                Path file = Paths.get("scripts/").resolve(filename);
                String simpleName = file.toString().substring(8);

                System.out.println(simpleName);
                GameBehavior newBehavior = getScriptBehavior(simpleName);

                game.replaceBehavior(simpleName, newBehavior);
            }

            key.reset();
        }
    }

    public static <T extends GameBehavior> T getScriptBehavior(String filename) {
        String scriptSource = "";
        String fullFilename = "scripts/" + filename;

        String extension = fullFilename.substring(fullFilename.indexOf(".") + 1);
        ScriptEngine engine = supportedScriptEngines.get(extension);

        try {
            scriptSource = IOUtils.getFileAsString(fullFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        T behavior = null;

        try {
            behavior = (T) engine.eval(scriptSource, bindings);
            System.out.println(behavior.getClass().getSimpleName());
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        bindings.clear();

        return behavior;
    }
}
