package com.morph.engine.util;

import com.morph.engine.script.GameBehavior;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created on 7/5/2017.
 */
public class ScriptUtils {
    private static HashMap<String, ScriptEngine> supportedScriptEngines;

    public static void init() {
        supportedScriptEngines = new HashMap<>();
        supportedScriptEngines.put("kts", new ScriptEngineManager().getEngineByExtension("kts"));
    }

    public static <T extends GameBehavior> T getScriptBehavior(String filename) {
        String scriptSource = "";
        String extension = filename.substring(filename.indexOf(".") + 1);
        ScriptEngine engine = supportedScriptEngines.get(extension);

        try {
            scriptSource = IOUtils.getFileAsString(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        T behavior = null;

        try {
            behavior = (T) engine.eval(scriptSource);
            System.out.println(behavior.getClass().getSimpleName());
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return behavior;
    }
}
