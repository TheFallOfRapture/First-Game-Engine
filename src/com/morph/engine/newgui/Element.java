package com.morph.engine.newgui;

import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 10/12/2016.
 */
public abstract class Element {
    private boolean enabled;
    private String ID;
    private String guiClass;
    private Transform2D transform;
    private RenderData data;
    private int depth;

    public Element(Transform2D transform, RenderData data, int depth) {
        this.transform = transform;
        this.data = data;
        this.depth = depth;

        transform.init();
        data.init();
    }

    public Element(Transform2D transform, RenderData data) {
        this.transform = transform;
        this.data = data;

        transform.init();
        data.init();
    }

    public Element(Transform2D transform) {
        this.transform = transform;

        transform.init();
    }

    public RenderData getRenderData() {
        return data;
    }

    public Transform2D getTransform() {
        return transform;
    }

    public int getDepth() {
        return depth;
    }
    // Render, Behavior
}
