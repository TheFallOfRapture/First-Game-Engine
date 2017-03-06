package com.morph.engine.newgui;

import com.morph.engine.core.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 3/6/2017.
 */
public abstract class GUI {
    private List<Element> elements;
    private Game game;

    public GUI(Game game) {
        elements = new ArrayList<>();
        this.game = game;
    }

    public abstract void load();
    public abstract void unload();

    public void addElement(Element e) {
        elements.add(e);
        if (e instanceof Container) {
            ((Container)e).getChildren(true).forEach(this::addElement);
        }
        game.addElement(e);
    }

    public void removeElement(Element e) {
        elements.remove(e);
        if (e instanceof Container) {
            ((Container)e).getChildren(true).forEach(this::removeElement);
        }
        game.removeElement(e);
    }

    public List<Element> getElements() {
        return elements;
    }
}
