package com.fate.game.shooting.systems;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

import com.fate.engine.collision.Collision;
import com.fate.engine.collision.CollisionEngine;
import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.engine.physics.components.Velocity2D;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.controller.ComboDictionary;
import com.fate.game.shooting.controller.KeyCombo;
import com.fate.game.shooting.controller.KeyboardController;
import com.fate.game.shooting.entities.TestBullet;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;

public class ControllerSystem extends GameSystem {
	private ComboDictionary combos;
	
	private static final float groundTolerance = 0.05f;
	private static final float frictionFactor = 0.25f;
	
	public ControllerSystem(ShootingGame game) {
		super(game);
		combos = new ComboDictionary();
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(KeyboardController.class, Transform2D.class, Velocity2D.class);
	}
	
	protected void fixedUpdate(Entity e, float dt) {
		KeyboardController c = e.getComponent(KeyboardController.class);
		
		checkGrounded(c, dt);
		updateMovement(c.getParent(), dt);
	}
	
	private void updateMovement(Entity e, float dt) {
		Velocity2D vel = e.getComponent(Velocity2D.class);
		KeyboardController kc = e.getComponent(KeyboardController.class);
		Transform2D t = e.getComponent(Transform2D.class);
		
		Vector2f velocity = vel.getVelocity();
		Vector2f frameVelocity = new Vector2f();
		
		kc.setMoving(false);
		
		int dirX = (int) Math.signum(velocity.getX()); // troubling
//		int dirY = (int) Math.signum(velocity.getY());
		
		if (Keyboard.isKeyPressed(GLFW_KEY_ENTER)) {
			Vector2f pos = t.getPosition();
			TestBullet bullet = new TestBullet(pos.getX() + 2f, pos.getY(), new BasicTexturedShader());
			((ShootingGame)game).getWorld().addEntity(bullet);
		}
		
		if (Keyboard.isKeyDown(kc.getJumpKey())) {
			if (kc.isGrounded()) {
				Vector2f move = kc.getJumpCommand().addMovement(e);
				frameVelocity = frameVelocity.add(move);
			}
		}
		
		if (Keyboard.isKeyDown(kc.getDownKey())) {
			Vector2f move = kc.getDownCommand().addMovement(e);
			frameVelocity = frameVelocity.add(move);
		}
		
		if (Keyboard.isKeyDown(kc.getLeftKey())) {
			Vector2f move = kc.getLeftCommand().addMovement(e);
			
			if (dirX == 1)
				move = move.scale(1 + kc.getReactivity());
				
			frameVelocity = frameVelocity.add(move);
			kc.setMoving(true);
			kc.setStopped(false);
		}
		
		if (Keyboard.isKeyDown(kc.getRightKey())) {
			Vector2f move = kc.getRightCommand().addMovement(e);
			
			if (dirX == -1)
				move = move.scale(1 + kc.getReactivity());
			
			frameVelocity = frameVelocity.add(move);
			kc.setMoving(true);
			kc.setStopped(false);
		}
		
		// If was moving and is now still
		if (!kc.isMoving() && !kc.isStopped()) {
			float frictionModifier = kc.isGrounded() ? 1 : 0.5f;
			
			Vector2f friction = new Vector2f(-dirX * frictionFactor * frictionModifier * Math.abs(velocity.getX()), 0); // troubling
			frameVelocity = frameVelocity.add(friction);
			
			if (Math.abs(velocity.getX()) < 1e-6) // troubling
				kc.setStopped(true);
		}
		
		KeyCombo currentCombo = kc.getCurrentCombo();
		
		if (Keyboard.isKeyPressed(kc.getJumpKey()))
			currentCombo.pushKeyEvent(kc.getJumpKey());
		
		if (Keyboard.isKeyPressed(kc.getDownKey()))
			currentCombo.pushKeyEvent(kc.getDownKey());
		
		if (Keyboard.isKeyPressed(kc.getLeftKey()))
			currentCombo.pushKeyEvent(kc.getLeftKey());
		
		if (Keyboard.isKeyPressed(kc.getRightKey()))
			currentCombo.pushKeyEvent(kc.getRightKey());
		
		currentCombo.fixedUpdate(dt);
		
		String combo = combos.getComboName(currentCombo);
		
		if (combo != "")
			System.out.println(combo + " combo detected!");
		
		if (combo == "Left Dash") {
			frameVelocity = frameVelocity.add(new Vector2f(-80, 0));
			currentCombo.clearAllEvents();
		}
		
		if (combo == "Right Dash") {
			frameVelocity = frameVelocity.add(new Vector2f(80, 0));
			currentCombo.clearAllEvents();
		}
		
		if (combo == "Super Jump") {
			frameVelocity = frameVelocity.add(new Vector2f(0, kc.getJumpHeight() * 3));
			currentCombo.clearAllEvents();
		}
		
		velocity = velocity.add(frameVelocity); // critical 
		
		float sign = Math.signum(velocity.getX()); // troubling
		if (Math.abs(velocity.getX()) > kc.getSpeed()) {
			float velX = velocity.getX();
			velX *= 0.9f;
			
			velocity.setX(velX); // critical
		}
		
		if (Math.abs(velocity.getX()) < 1e-6) velocity.setX(0);
		if (Math.abs(velocity.getY()) < 1e-6) velocity.setY(0);
		
		vel.setVelocity(velocity);
	}

	private void checkGrounded(KeyboardController c, float dt) {
		Entity e = c.getParent();
		
		Vector2f position = e.getComponent(Transform2D.class).getPosition();
		Vector2f halfSize = e.getComponent(BoundingBox2D.class).getHalfSize();
		Vector2f velocity = e.getComponent(Velocity2D.class).getVelocity();
		
		BoundingBox2D sensor = new BoundingBox2D(position.sub(new Vector2f(0, halfSize.getY())), new Vector2f(halfSize.getX(), groundTolerance));
		List<Collision> sensorCollisions = CollisionEngine.checkAgainstWorldStatic(sensor, game.getEntities());
		
		boolean grounded = false;
		if (sensorCollisions.size() > 0) {
			for (Collision coll : sensorCollisions) {
				if (!coll.getHit().equals(e) && !coll.getHit().getComponent(BoundingBox2D.class).isTrigger()) {
					grounded = true;
					break;
				}
			}
		}
		
		c.setGrounded(grounded);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
