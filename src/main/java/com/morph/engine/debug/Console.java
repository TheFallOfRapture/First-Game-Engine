package com.morph.engine.debug;

import com.morph.engine.core.Game;
import com.morph.engine.events.ConsoleEvent;
import com.morph.engine.events.EventDispatcher;
import com.morph.engine.events.EventListener;
import com.morph.engine.util.ScriptUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Created on 11/24/2017.
 */
public class Console {
    private static class ConsoleOutputStream extends OutputStream {
        @Override
        public void write(@NotNull byte[] buffer, int offset, int length) {
            String text = new String(buffer, offset, length);
            Console.print(text);
        }

        @Override
        public void write(int b) {
            write(new byte[]{(byte)b}, 0, 1);
        }
    }

    public enum ScriptType {
        KOTLIN, PYTHON, MULTI
    }

    private static String text = "";
    private ScriptType type;
    private static String currentLine = "";
    private Game game;

    private static ConsoleOutputStream outBytes = new ConsoleOutputStream();
    private static ConsoleOutputStream errBytes = new ConsoleOutputStream();

    public static PrintStream out = new PrintStream(outBytes);
    public static PrintStream err = new PrintStream(errBytes);

    public Console(Console.ScriptType type, Game game) {
        this.type = type;
        this.game = game;
        EventDispatcher.INSTANCE.addEventHandler(this);
    }

    public void readIn(String line) {
        runLine(line);
    }

    private static void print(String line) {
        Console.currentLine = line;
        Console.text += line;

        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(null, ConsoleEvent.EventType.UPDATE, ""));
    }

    private static void newLine() {
        Console.currentLine = "\n";
        Console.text += "\n";

        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(null, ConsoleEvent.EventType.UPDATE, ""));
    }

    public String getText() {
        return Console.text;
    }

    public String getLastLine() {
        return currentLine;
    }

    private void runLine(String line) {
        switch (type) {
            case MULTI:
                String[] parts = line.split(":");
                parts[1] = parts[1].trim();
                ScriptUtils.readScript(parts[1], parts[0], this);
                break;
            case KOTLIN:
                ScriptUtils.readScript(line, "kts", this);
                break;
            case PYTHON:
                ScriptUtils.readScript(line, "py", this);
        }
    }

    public Game getGame() {
        return game;
    }

    public void clear() {
        Console.text = "";
        Console.currentLine = "";

        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(null, ConsoleEvent.EventType.CLEAR, ""));
    }
}
