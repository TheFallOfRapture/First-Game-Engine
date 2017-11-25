package com.morph.engine.events;

import com.morph.engine.debug.Console;

public class ConsoleEvent extends Event {
    public enum EventType {
        UPDATE, PRINT
    }

    private EventType type;

    public ConsoleEvent(Console source, EventType type) {
        super(source);
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
