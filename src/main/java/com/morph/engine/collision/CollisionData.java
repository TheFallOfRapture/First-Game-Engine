package com.morph.engine.collision;

import com.morph.engine.math.Vector2f;

public class CollisionData {
    private final Vector2f position;
    private final Vector2f intersection;
    private final Vector2f normal;
    private final float time;

    public CollisionData(Vector2f position, Vector2f intersection, Vector2f normal, float time) {
        this.position = position;
        this.intersection = intersection;
        this.normal = normal;
        this.time = time;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getIntersection() {
        return intersection;
    }

    public Vector2f getNormal() {
        return normal;
    }

    public float getTime() {
        return time;
    }
}
