package com.morph.engine.util;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.shaders.BasicTexturedShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;

import java.util.Arrays;

/**
 * Created by Fernando on 1/16/2017.
 */
public class EntityGenUtils {
    public static Entity createEntity(Component... components) {
        Entity e = new Entity();
        Arrays.stream(components).forEach(e::addComponent);
        return e;
    }

    public static Entity createEntityRectangle(float width, float height, boolean isTrigger, Component... additionalComponents) {
        Entity e = createEntity();
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);

        e.addComponent(new Transform2D(new Vector2f(0, 0), new Vector2f(width, height)));
        e.addComponent(new BoundingBox2D(new Vector2f(0, 0), halfSize, isTrigger));
        e.addComponent(RenderDataUtils.createSquare(new Color(1, 1, 1), new BasicTexturedShader(), new Texture(null)));

        return e;
    }
}
