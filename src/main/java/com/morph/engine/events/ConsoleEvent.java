package com.morph.engine.events;

import com.morph.engine.debug.Console;

public class ConsoleEvent extends Event {
    public enum EventType {
        UPDATE, PRINT
    }

    private EventType type;
    private String message;

    public ConsoleEvent(Console source, EventType type, String message) {
        super(source);
        this.type = type;
        this.message = message;
    }

    public EventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
