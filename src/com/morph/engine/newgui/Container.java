package com.morph.engine.newgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fernando on 10/12/2016.
 */
public abstract class Container extends Element {
    private List<Element> children;

    public Container() {
        children = new ArrayList<Element>();
    }

    public void addElement(Element e) {
        this.children.add(e);
    }

    public void addElement(Element... e) {
        children.addAll(Arrays.asList(e));
    }
}
