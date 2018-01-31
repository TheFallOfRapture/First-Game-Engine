package com.morph.engine.newgui;

import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.physics.components.Transform2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Fernando on 10/12/2016.
 */
public class Container extends Element {
    private List<Element> children;

    public Container(String name, Transform2D transform, RenderData data, int depth) {
        super(name, transform, data, depth);
        children = new ArrayList<>();
    }

    public Container(String name, Transform2D transform, RenderData data) {
        super(name, transform, data);
        children = new ArrayList<>();
    }

    public Container(String name, Transform2D transform) {
        super(name, transform);
        children = new ArrayList<>();
    }

    public List<Element> getChildren(boolean deep) {
        if (deep) {
            List<Element> result = new ArrayList<>();
            children.forEach(c -> {
                result.add(c);
                if (c instanceof Container) {
                    Container container = (Container) c;
                    result.addAll(container.getChildren(true));
                }
            });

            return result;
        }

        return children;
    }

    public void addElement(Element e) {
        this.children.add(e);
    }

    public void addElement(Element... e) {
        children.addAll(Arrays.asList(e));
    }
}
