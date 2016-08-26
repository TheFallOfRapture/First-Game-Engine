package com.fate.engine.physics.components;

import com.fate.engine.entities.Component;
import com.fate.engine.math.Vector2f;

public class RigidBody extends Component implements Cloneable {
	private Vector2f velocity;
	private Vector2f acceleration;
	
	private float mass;
	
	private Vector2f netForce;

	public Vector2f getVelocity() {
		return velocity;
	}

	public Vector2f getAcceleration() {
		return acceleration;
	}

	public float getMass() {
		return mass;
	}
	
	public RigidBody(float mass) {
		this.mass = mass;
		
		velocity = new Vector2f();
		acceleration = new Vector2f();
		
		netForce = new Vector2f();
	}
	
	/**
	 * Cloning constructor
	 * @param mass
	 * @param velocity
	 * @param acceleration
	 * @param netForce
	 */
	private RigidBody(float mass, Vector2f velocity, Vector2f acceleration, Vector2f netForce) {
		this.mass = mass;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.netForce = netForce;
	}
	
	/*
	 * Leapfrog Integration
	 * x_new = x_current + v_current*dt + 0.5*a_current*dt^2
	 * v_new = v_current + 0.5(a_current + a_new)*dt
	 */
	
	/**
	 * Updates the rigid body using numerical integration (leapfrog method shown above).
	 * @param dt the time step
	 */
	public Vector2f update(float dt) {
		Vector2f newAcceleration = netForce.invScale(mass); // F = ma -> a = F / m
		
		Vector2f translation = velocity.scale(dt).add(acceleration.scale(0.5f * dt * dt));
		
//		this.getParent().setPosition(this.getPosition().add(velocity.scale(dt)).add(acceleration.scale(0.5f * dt * dt)));
		velocity = velocity.add(acceleration.add(newAcceleration).scale(0.5f * dt));
		
		acceleration.set(newAcceleration);
		netForce.set(0, 0);
		
		return translation;
	}
	
	/**
	 * Applies a force to the rigid body.
	 * @param force the force applied
	 */
	public void applyForce(Vector2f force) {
		// F = ma
		// a = F / m
		netForce = netForce.add(force);
	}
	
	/**
	 * Applies an impulse to the rigid body by converting it to force (unadvised!)
	 * @param impulse the impulse applied
	 * @param dt the time step
	 */
	public void applyImpulse(Vector2f impulse, float dt) {
		// J = integral F dt -> F * dt (finite dt)
		// F = J / dt
		netForce = netForce.add(impulse.invScale(dt));
	}

	/**
	 * Resets the rigid body system.
	 */
	public void clear() {
		velocity.set(0, 0);
		acceleration.set(0, 0);
	}
	
	@Override
	public RigidBody clone() {
		return new RigidBody(mass, velocity, acceleration, netForce);
	}
}
