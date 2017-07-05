package com.morph.engine.script;

import com.morph.engine.core.Game;
import com.morph.engine.core.GameSystem;
import com.morph.engine.entities.Entity;

/**
 * Created on 7/5/2017.
 */
public class ScriptSystem extends GameSystem {
    public ScriptSystem(Game game) {
        super(game);
    }

    @Override
    protected boolean acceptEntity(Entity e) {
        return e.hasComponent(ScriptContainer.class);
    }

    @Override
    public void initSystem() {

    }

    @Override
    protected void preUpdate(Entity e) {
        super.preUpdate(e);

        e.getComponent(ScriptContainer.class).getBehaviors().forEach(EntityBehavior::preUpdate);
    }

    @Override
    protected void update(Entity e) {
        super.update(e);

        e.getComponent(ScriptContainer.class).getBehaviors().forEach(EntityBehavior::update);
    }

    @Override
    protected void fixedUpdate(Entity e, float dt) {
        super.fixedUpdate(e, dt);

        e.getComponent(ScriptContainer.class).getBehaviors().forEach(s -> s.fixedUpdate(dt));
    }

    @Override
    protected void postUpdate(Entity e) {
        super.postUpdate(e);

        e.getComponent(ScriptContainer.class).getBehaviors().forEach(EntityBehavior::postUpdate);
    }
}
