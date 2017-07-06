package com.morph.engine.util;

import com.morph.engine.core.Game;
import com.morph.engine.entities.Entity;
import com.morph.engine.script.EntityBehavior;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.script.ScriptContainer;

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
    private static HashMap<String, ScriptEngine> supportedScriptEngines;
    private static HashMap<String, List<Entity>> scriptedEntities;
    private static SimpleBindings bindings;
    private static WatchService watchService;

    public static void init(Game game) {
        supportedScriptEngines = new HashMap<>();
        scriptedEntities = new HashMap<>();
        bindings = new SimpleBindings();
        supportedScriptEngines.put("kts", new ScriptEngineManager().getEngineByExtension("kts"));

        System.out.println(Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/Test.kts").getFileName());

        try {
            watchService = FileSystems.getDefault().newWatchService();
            WatchKey key = Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY);
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

    public static <T extends GameBehavior> T getScriptBehavior(String filename) {
        String scriptSource = "";
        String fullFilename = System.getProperty("user.dir") + "/src/main/resources/scripts/" + filename;

        String extension = fullFilename.substring(fullFilename.indexOf(".") + 1);
        ScriptEngine engine = supportedScriptEngines.get(extension);

        try {
            scriptSource = IOUtils.getFileAsStringAbsolute(fullFilename);
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
