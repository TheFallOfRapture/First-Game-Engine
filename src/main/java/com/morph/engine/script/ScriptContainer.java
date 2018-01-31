package com.morph.engine.script;

import com.morph.engine.core.Game;
import com.morph.engine.entities.Component;
import com.morph.engine.util.ScriptUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 7/5/2017.
 */
public class ScriptContainer extends Component {
    private HashMap<String, EntityBehavior> behaviors;
    private Game game;

    public ScriptContainer(Game game) {
        behaviors = new HashMap<>();
        this.game = game;
    }

    public void addBehavior(String filename) {
        addBehaviorStrict(filename);
    }

    public void addBehaviorAsync(String filename) {
        ScriptUtils.getScriptBehaviorAsync(filename).subscribe(behavior -> {
            EntityBehavior eBehavior = (EntityBehavior) behavior;
            eBehavior.setGame(game);
            eBehavior.setSelf(parent);
            behaviors.put(filename, eBehavior);
            eBehavior.init();
            eBehavior.start();
        });
    }

    private void addBehaviorStrict(String filename) {
        EntityBehavior behavior = ScriptUtils.getScriptBehavior(filename);
        behavior.setGame(game);
        behavior.setSelf(parent);
        behaviors.put(filename, behavior);
        behavior.init();
        behavior.start();

        ScriptUtils.register(filename, parent);
    }

    public void replaceBehavior(String filename, EntityBehavior newBehavior) {
        newBehavior.setGame(game);
        newBehavior.setSelf(parent);
        behaviors.replace(filename, newBehavior);
        newBehavior.start();
    }

    public void removeBehavior(String filename) {
        EntityBehavior behavior = behaviors.get(filename);
        behaviors.remove(filename);
        behavior.destroy();
    }

    public List<EntityBehavior> getBehaviors() {
        return new ArrayList<>(behaviors.values());
    }

    @Override
    public Component clone() {
        return null;
    }
}
