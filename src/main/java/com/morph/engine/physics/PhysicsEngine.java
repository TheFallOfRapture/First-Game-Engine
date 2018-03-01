package com.morph.engine.physics;

import com.morph.engine.core.Game;
import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.RigidBody;
import com.morph.engine.physics.components.Transform;

import java.util.ArrayList;
import java.util.List;

// TODO: This class cannot be present in the Engine package with an immutable gravity factor!
public class PhysicsEngine {
	private static Vector2f gravity;
	public PhysicsEngine() {
		gravity = new Vector2f(0, -1000);
	}
	
	public void update(Game g, List<Entity> entities, float dt) {
		List<RigidBody> rigidBodies = new ArrayList<>();
		for (Entity e: entities)
			if (e.hasComponent(RigidBody.class))
				rigidBodies.add(e.getComponent(RigidBody.class));
				
		updateRigidBodies(entities, dt);
	}
	
	public void updateRigidBodies(List<Entity> entities, float dt) {
		for (Entity e : entities) {
			RigidBody rb = e.getComponent(RigidBody.class);
			Transform t = e.getComponent(Transform.class);
			updateRigidBody(rb, t, dt);
		}
	}
	
	public void updateRigidBody(RigidBody rb, Transform t, float dt) {
		rb.applyForce(gravity.scale(rb.getMass()));
		Vector2f translation = rb.update(dt);
		t.translate(translation.asTranslationMatrix());
	}
}
