package com.fate.game.shooting.controller;

import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Velocity2D;

import java.util.function.Consumer;

/**
 * Created by Fernando on 1/12/2017.
 */
public class GenericAction<T extends Actionable> {
    private T actionable;
    private String trigger;

    public GenericAction(String trigger, T actionable) {
        this.trigger = trigger;
        this.actionable = actionable;
    }

    public void executeIfMatch(String trigger, T t) {
        if (trigger.equals(this.trigger)) {
            actionable.execute(t);
        }
    }
}
