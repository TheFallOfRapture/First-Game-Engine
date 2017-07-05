package com.morph.engine.script;

import com.morph.engine.entities.Component;
import com.morph.engine.util.ScriptUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 7/5/2017.
 */
public class ScriptContainer extends Component {
    private HashMap<String, EntityBehavior> behaviors;

    public void addBehavior(String filename) {
        EntityBehavior behavior = ScriptUtils.getScriptBehavior(filename);
        behaviors.put(filename, behavior);
        behavior.init();
    }

    public void removeBehavior(String filename) {
        EntityBehavior behavior = behaviors.get(filename);
        behaviors.remove(filename);
        behavior.destroy();
    }

    public List<EntityBehavior> getBehaviors() {
        return behaviors.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Component clone() {
        return null;
    }
}
