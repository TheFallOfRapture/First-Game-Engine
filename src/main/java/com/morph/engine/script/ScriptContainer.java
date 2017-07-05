package com.morph.engine.script;

import com.morph.engine.entities.Component;

import java.util.List;

/**
 * Created on 7/5/2017.
 */
public class ScriptContainer extends Component {
    private List<EntityBehavior> behaviors;

    public void addBehavior(EntityBehavior b) {
        behaviors.add(b);
        b.init();
    }

    public void removeBehavior(EntityBehavior b) {
        behaviors.remove(b);
        b.destroy();
    }

    public List<EntityBehavior> getBehaviors() {
        return behaviors;
    }

    @Override
    public Component clone() {
        return null;
    }
}
