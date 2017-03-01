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
    private RenderData renderData;

    public RenderData getRenderData() {
        return renderData;
    }

    public Transform2D getTransform() {
        return transform;
    }
    // Render, Behavior
}
