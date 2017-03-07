package com.morph.engine.newgui;

import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.State;
import com.morph.engine.util.StateMachine;

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

    private Runnable onIdle;
    private Runnable onHover;
    private Runnable onClick;

    public StateMachine state;

    public Element(Transform2D transform, RenderData data, int depth) {
        this.transform = transform;
        this.data = data;
        this.depth = depth;

        this.onIdle = () -> System.out.println("Idle : " + this);
        this.onClick = () -> System.out.println("Click : " + this);
        this.onHover = () -> System.out.println("Hover : " + this);

        state = new StateMachine(new State("IDLE"));
        state.addPossibilities("IDLE", "HOVER", "CLICK");
        state.addTransition("*", "IDLE", onIdle);
        state.addTransition("*", "HOVER", onHover);
        state.addTransition("*", "CLICK", onClick);

        transform.init();
        data.init();
    }

    public Element(Transform2D transform, RenderData data) {
        this.transform = transform;
        this.data = data;

        this.onIdle = () -> System.out.println("Idle : " + this);
        this.onClick = () -> System.out.println("Click : " + this);
        this.onHover = () -> System.out.println("Hover : " + this);

        state = new StateMachine(new State("IDLE"));
        state.addPossibilities("IDLE", "HOVER", "CLICK");
        state.addTransition("*", "IDLE", onIdle);
        state.addTransition("*", "HOVER", onHover);
        state.addTransition("*", "CLICK", onClick);

        transform.init();
        data.init();
    }

    public Element(Transform2D transform) {
        this.transform = transform;

        this.onIdle = () -> System.out.println("Idle : " + this);
        this.onClick = () -> System.out.println("Click : " + this);
        this.onHover = () -> System.out.println("Hover : " + this);

        state = new StateMachine(new State("IDLE"));
        state.addPossibilities("IDLE", "HOVER", "CLICK");
        state.addTransition("*", "IDLE", onIdle);
        state.addTransition("*", "HOVER", onHover);
        state.addTransition("*", "CLICK", onClick);

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

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean contains(Vector2f point) {
        Vector2f p0 = transform.getPosition().sub(transform.getScale().scale(0.5f));
        Vector2f p1 = p0.add(transform.getScale());

        return p0.getX() < point.getX()
                && point.getX() < p1.getX()
                && p0.getY() < point.getY()
                && point.getY() < p1.getY();
    }

    public void setOnHover(Runnable onHover) {
        this.onHover = onHover;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setState(String stateName) {
        this.state.changeState(stateName);
    }

    public String getState() {
        return state.getCurrentStateName();
    }
}
