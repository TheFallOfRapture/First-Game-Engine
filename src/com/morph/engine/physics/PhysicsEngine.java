package com.fate.engine.physics;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.core.Game;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.RigidBody;
import com.fate.engine.physics.components.Transform;

// TODO: This class cannot be present in the Engine package with an immutable gravity factor!
public class PhysicsEngine {
	private static Vector2f gravity;
	public PhysicsEngine() {
		gravity = new Vector2f(0, -1000);
	}
	
	public void update(Game g, List<Entity> entities, float dt) {
		List<RigidBody> rigidBodies = new ArrayList<RigidBody>();
		for (Entity e: entities)
			if (e.hasComponent(RigidBody.class))
				rigidBodies.add(e.getComponent(RigidBody.class));
				
		updateRigidBodies(rigidBodies, dt);
	}
	
	public void updateRigidBodies(List<RigidBody> rigidBodies, float dt) {
		for (RigidBody rb: rigidBodies) {
			Transform t = rb.getParent().getComponent(Transform.class);
			updateRigidBody(rb, t, dt);
		}
	}
	
	public void updateRigidBody(RigidBody rb, Transform t, float dt) {
		rb.applyForce(gravity.scale(rb.getMass()));
		Vector2f translation = rb.update(dt);
		t.translate(translation.asTranslationMatrix());
	}
}
