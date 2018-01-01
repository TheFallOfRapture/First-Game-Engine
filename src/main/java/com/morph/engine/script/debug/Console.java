package com.morph.engine.script.debug;

import com.morph.engine.core.Game;
import com.morph.engine.util.Feed;
import com.morph.engine.util.Listener;
import com.morph.engine.util.ScriptUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created on 11/24/2017.
 */
public class Console {
    public enum EventType {
        UPDATE, CLEAR
    }

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

    public static class ConsolePrintStream extends PrintStream {
        private ConsolePrintStream(@NotNull ConsoleOutputStream out) {
            super(out);
        }

        @Override
        public void println() {
            super.println();
            Console.newLine();
        }

        @Override
        public void println(boolean x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(char x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(int x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(long x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(float x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(double x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(@NotNull char[] x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(String x) {
            super.println(x);
            Console.newLine();
        }

        @Override
        public void println(Object x) {
            super.println(x);
            Console.newLine();
        }
    }

    private static Feed<EventType> eventFeed = new Feed<>();

    private static Flowable<EventType> events = Flowable.create(emitter -> {
        Listener<EventType> eventListener = new Listener<EventType>() {
            @Override
            public void onNext(EventType eventType) {
                emitter.onNext(eventType);
            }

            @Override
            public void onError(Throwable t) {
                emitter.onError(t);
            }

            @Override
            public void onComplete() {
                emitter.onComplete();
            }
        };
        eventFeed.register(eventListener);
    }, BackpressureStrategy.BUFFER);

    public enum ScriptType {
        KOTLIN, PYTHON, MULTI
    }

    private static String text = "";
    private ScriptType type;
    private static String currentLine = "";
    private static Game game;

    private static ConsoleOutputStream outBytes = new ConsoleOutputStream();
    private static ConsoleOutputStream errBytes = new ConsoleOutputStream();

    public static PrintStream out = new PrintStream(outBytes);
    public static PrintStream err = new PrintStream(errBytes);

    public Console(Console.ScriptType type, Game game) {
        this.type = type;
        Console.game = game;

        game.getEvents().filter(e -> e == Game.GameAction.CLOSE).subscribe(e -> eventFeed.onComplete());
    }

    public void readIn(String line) {
        runLine(line);
    }

    private static void print(String line) {
        Console.currentLine = line;
        Console.text += line;

        eventFeed.onNext(EventType.UPDATE);
    }

    private static void newLine() {
        Console.currentLine = "\n";
        Console.text += "\n";

        eventFeed.onNext(EventType.UPDATE);
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
                ScriptUtils.readScriptBlocking(parts[1], parts[0], this);
                break;
            case KOTLIN:
                ScriptUtils.readScriptBlocking(line, "kts", this);
                break;
            case PYTHON:
                ScriptUtils.readScriptBlocking(line, "py", this);
        }
    }

    public Game getGame() {
        return game;
    }

    public void clear() {
        Console.text = "";
        Console.currentLine = "";

        eventFeed.onNext(EventType.CLEAR);
    }

    @Contract(pure = true)
    public static Flowable<EventType> events() {
        return events;
    }
}
