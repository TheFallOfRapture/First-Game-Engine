package com.fate.game.shooting.events;

import com.fate.engine.entities.Entity;
import com.fate.engine.events.EntityEvent;
import com.fate.engine.events.Event;
import com.fate.engine.math.Vector2f;

/**
 * Created by Fernando on 1/9/2017.
 */
public class AttackEvent extends EntityEvent {
    public enum AttackSelection {
        ONE, TWO, THREE, FOUR;
    }

    private AttackSelection selection;
    private Vector2f mousePos;

    public AttackEvent(Entity source, AttackSelection selection, Vector2f mousePos) {
        super(source);
        this.selection = selection;
        this.mousePos = mousePos;
    }

    public AttackSelection getSelection() {
        return selection;
    }

    public Vector2f getMousePosition() {
        return mousePos;
    }
}
