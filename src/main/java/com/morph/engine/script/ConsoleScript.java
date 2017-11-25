package com.morph.engine.script;

import com.morph.engine.debug.Console;
import com.morph.engine.events.ConsoleEvent;
import com.morph.engine.events.EventDispatcher;

public class ConsoleScript {
    private Console console;

    public ConsoleScript(Console console) {
        this.console = console;
    }

    protected void echo(String message) {
        EventDispatcher.INSTANCE.dispatchEvent(new ConsoleEvent(console, ConsoleEvent.EventType.PRINT, message));
    }
}
