package com.fate.engine.physics.components;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.fate.engine.entities.Component;
import com.fate.engine.math.Vector2f;

public class StaticBody2D extends RigidBody2D {
	private Body body;
	
	public StaticBody2D(World world, Vector2f position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.getX(), position.getY());
		this.body = world.createBody(bodyDef);
	}
	
	public void addBoxShape(Vector2f halfSize) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(halfSize.getX(), halfSize.getY());
		body.createFixture(shape, 0);
	}
	
	public void addShape(PolygonShape shape) {
		body.createFixture(shape, 0);
	}

	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2f getPosition() {
		Vec2 pos = body.getPosition();
		return new Vector2f(pos.x, pos.y);
	}
}
