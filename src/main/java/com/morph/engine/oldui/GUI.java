package com.morph.engine.oldui;

import com.morph.engine.core.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 3/6/2017.
 */
public abstract class GUI {
    private List<Element> elements;
    private Game game;
    private boolean open = false;

    public GUI(Game game) {
        elements = new ArrayList<>();
        this.game = game;
    }

    public abstract void init();
    public abstract void load();
    public abstract void unload();

    public void addElement(Element e) {
        elements.add(e);
        if (e instanceof Container) {
            ((Container)e).getChildren(true).forEach(this::addElement);
        }
    }

    public void removeElement(Element e) {
        elements.remove(e);
        if (e instanceof Container) {
            ((Container)e).getChildren(true).forEach(this::removeElement);
        }
    }

    public Element getElementByName(String name) {
        return elements.stream().collect(Collectors.toMap(Element::getName, e -> e)).get(name);
    }

    public void fixedUpdate(float dt) {}

    public Game getGame() {
        return game;
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }
}
