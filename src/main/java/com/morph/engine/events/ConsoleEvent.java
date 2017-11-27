package com.morph.engine.events;

public class ConsoleEvent extends Event {
    public enum EventType {
        UPDATE, CLEAR
    }

    private EventType type;
    private String message;

    public ConsoleEvent(Object source, EventType type, String message) {
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
