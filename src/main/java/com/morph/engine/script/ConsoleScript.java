package com.morph.engine.script;

import com.morph.engine.core.Game;
import com.morph.engine.debug.Console;
import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.math.*;
import com.morph.engine.util.EntityGenUtils;

public abstract class ConsoleScript implements Runnable {
    private Console console;

    public void setConsole(Console console) {
        this.console = console;
    }

    protected void echo(String message) {
        Console.out.println(message);
    }

    protected void echo(Object message) {
        echo(message.toString());
    }

    protected void getVersion() {
        echo("Morph " + Game.VERSION_MAJOR + "." + Game.VERSION_MINOR + "." + Game.VERSION_PATCH);
    }

    protected void addEntity(String name, Component... components) {
        Entity e = EntityGenUtils.createEntity(name, components);
        console.getGame().addEntity(e);
        echo("Created new entity " + name + " (ID #" + e.getID() + ")");
    }

    protected void addEntityRectangle(String name, float width, float height, boolean isTrigger, Component... components) {
        Entity e = EntityGenUtils.createEntityRectangle(name, width, height, isTrigger, components);
        console.getGame().addEntity(e);
        echo("Created new entity " + name + " (ID #" + e.getID() + ")");
    }

    protected Entity getEntity(String name) {
        return console.getGame().getWorld().getEntityByName(name);
    }

    protected void clear() {
        console.clear();
    }

    public abstract void run();
}
