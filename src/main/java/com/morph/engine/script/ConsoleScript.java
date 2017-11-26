package com.morph.engine.script;

import com.morph.engine.core.Game;
import com.morph.engine.debug.Console;
import com.morph.engine.events.ConsoleEvent;
import com.morph.engine.events.EventDispatcher;

public abstract class ConsoleScript implements Runnable {
    private Console console;

    public void setConsole(Console console) {
        this.console = console;
    }

    public Console getConsole() {
        return console;
    }

    protected void echo(String message) {
        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(console, ConsoleEvent.EventType.PRINT, message));
    }

    protected void echo(Object message) {
        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(console, ConsoleEvent.EventType.PRINT, message.toString()));
    }

    protected void getVersion() {
        echo("Morph " + Game.VERSION_MAJOR + "." + Game.VERSION_MINOR + "." + Game.VERSION_PATCH);
    }

    public abstract void run();
}
