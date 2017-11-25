package com.morph.engine.events;

import com.morph.engine.debug.Console;

/**
 * Created on 11/25/2017.
 */
public class ConsoleUpdateEvent extends Event {
    public ConsoleUpdateEvent(Console console) {
        super(console);
    }
}
