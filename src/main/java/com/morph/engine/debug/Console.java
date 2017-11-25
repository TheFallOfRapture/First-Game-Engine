package com.morph.engine.debug;

import com.morph.engine.events.ConsoleUpdateEvent;
import com.morph.engine.events.EventDispatcher;
import com.morph.engine.util.ScriptUtils;

import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created on 11/24/2017.
 */
public class Console {
    public enum ScriptType {
        KOTLIN, PYTHON, MULTI
    }

    private String text;
    private ScriptType type;
    private Logger logger;
    private String currentLine;

    public Console(Console.ScriptType type) {
        this.type = type;
    }

    public void readIn(String line) {
        this.currentLine = line;
        this.text += line;
        runLine(line);

        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleUpdateEvent(this));
    }

    public String getText() {
        return text;
    }

    public String getLastLine() {
        return currentLine;
    }

    private void runLine(String line) {
        switch (type) {
            case MULTI:
                String[] parts = line.split(":");
                parts[1] = parts[1].trim();
                ScriptUtils.readScript(parts[1], parts[0]);
                break;
            case KOTLIN:
                ScriptUtils.readScript(line, "kts");
                break;
            case PYTHON:
                ScriptUtils.readScript(line, "py");
        }
    }
}
