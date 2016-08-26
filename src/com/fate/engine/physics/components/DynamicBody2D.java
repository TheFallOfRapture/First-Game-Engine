package com.fate.engine.physics.components;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.fate.engine.entities.Component;
import com.fate.engine.math.Vector2f;

public class DynamicBody2D extends RigidBody2D {
	private Body body;
	
	public DynamicBody2D(World world, Vector2f position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(position.getX(), position.getY());
		body = world.createBody(bodyDef);
	}
	
	public void addBoxShape(Vector2f halfSize, float density, float friction) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(halfSize.getX(), halfSize.getY());
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		body.createFixture(fixtureDef);
	}
	
	public void applyLinearImpulse(Vector2f impulse) {
		body.applyLinearImpulse(new Vec2(impulse.getX(), impulse.getY()), body.getWorldCenter());
	}
	
	public void setLinearVelocity(Vector2f velocity) {
		body.setLinearVelocity(new Vec2(velocity.getX(), velocity.getY()));
	}
	
	public void applyForce(Vector2f force) {
		body.applyForceToCenter(new Vec2(force.getX(), force.getY()));
	}

	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2f getPosition() {
		return new Vector2f(body.getPosition().x, body.getPosition().y);
	}

	public Vector2f getLinearVelocity() {
		return new Vector2f(body.getLinearVelocity().x, body.getLinearVelocity().y);
	}
}
