package com.fate.engine.collision;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.collision.components.CollisionComponent;
import com.fate.engine.collision.components.TriggerComponent;
import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.MathUtils;
import com.fate.engine.math.Vector2f;
import com.fate.engine.math.Vector3f;
import com.fate.engine.physics.components.Velocity2D;

public class CollisionEngine extends GameSystem {
	public CollisionEngine(Game game) {
		super(game);
	}
	
	protected void fixedUpdate(Entity e, float dt) {
		e.getComponent(BoundingBox2D.class).update();
	}
	
	protected void systemFixedUpdate(float dt) {
		List<Entity> collidables = new ArrayList<Entity>();
		for (int i = game.getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				collidables.add(e);
			}
		}
		
		checkCollision(collidables, dt);
	}
	
	public static List<Collision> checkAgainstWorldStatic(BoundingBox2D boxA, List<Entity> entities) {
		List<Collision> result = new ArrayList<Collision>();
		for (int i = 0; i < entities.size(); i++) {
			Entity other = entities.get(i);
			
			BoundingBox2D boxB = other.getComponent(BoundingBox2D.class);
			Collision coll = checkDoubleStatic(boxA, boxB);
			if (coll != null)
				result.add(coll);
		}
		
		return result;
	}
	
	public static List<Collision> checkAgainstWorld(BoundingBox2D boxA, Vector2f velA, List<Entity> entities, float dt) {
		List<Collision> result = new ArrayList<Collision>();
		
		for (int i = 0; i < entities.size(); i++) {
			Entity a = boxA.getParent();
			Entity b = entities.get(i);
			
			BoundingBox2D boxB = b.getComponent(BoundingBox2D.class);
			Velocity2D vel2D = b.getComponent(Velocity2D.class);
			Vector2f velB = vel2D == null ? new Vector2f(0, 0) : vel2D.getVelocity();
			
			Collision coll;
			
			if (velA.equals(new Vector2f(0, 0)) && velB.equals(new Vector2f(0, 0)))
				coll = checkDoubleStatic(boxA, boxB);
			else if (velA.equals(new Vector2f(0, 0)) && !velB.equals(new Vector2f(0, 0)))
				coll = checkStaticDynamic(boxA, boxB, velB.scale(dt), boxB.getHalfSize());
			else if (!velA.equals(new Vector2f(0, 0)) && velB.equals(new Vector2f(0, 0)))
				coll = checkStaticDynamic(boxB, boxA, velA.scale(dt), boxA.getHalfSize());
			else
				coll = checkDoubleDynamic(boxA, boxB, velA, velB, boxB.getHalfSize(), dt);
			
			if (coll != null) {
				if (coll.getHit() == null) {
					coll = new Collision(coll.getHit(), coll.getEntity(), coll.getPosition(), coll.getIntersection(), coll.getNormal(), coll.getTime());
				}
				
				result.add(coll);
			}
		}
		
		return result;
	}
	
	private void checkCollision(List<Entity> entities, float dt) {
		for (int i = 0; i < entities.size(); i++) {
			for (int j = i + 1; j < entities.size(); j++) {
				Entity a = entities.get(i);
				Entity b = entities.get(j);
				
				BoundingBox2D boxA = a.getComponent(BoundingBox2D.class);
				BoundingBox2D boxB = b.getComponent(BoundingBox2D.class);
				
				SweepCollision coll = checkCollision(a, b, dt);
				
				if (coll.getCollision() != null) { // Collision Solver
					Vector2f delta = coll.getCollision().getIntersection();
					float distance = delta.getX() == 0 ? delta.getX() : delta.getY();
					Vector3f normal = new Vector3f(coll.getCollision().getNormal(), 0);
					
					if (!boxA.isTrigger() && !boxB.isTrigger())
						solveCollision(coll);
					
					if (boxA.isTrigger())
						b.addComponent(new TriggerComponent(a, normal));
					else
						b.addComponent(new CollisionComponent(a, normal, distance, coll.getTime()));
					
					if (boxB.isTrigger())
						a.addComponent(new TriggerComponent(b, normal));
					else
						a.addComponent(new CollisionComponent(b, normal, distance, coll.getTime()));
				}
			}
		}
	}
	
	private void solveCollision(SweepCollision coll) {
		Entity a = coll.getCollision().getEntity();
		Entity b = coll.getCollision().getHit();
		
		if (a.hasComponent(Velocity2D.class)) {
			Velocity2D v2D = a.getComponent(Velocity2D.class);
			
			Vector2f vel = v2D.getVelocity();
			Vector2f blockDir = coll.getCollision().getNormal().negate();
			Vector2f remove = blockDir.scale(blockDir.dot(vel)).scale(1.0f - coll.getCollision().getTime());
			
			Vector2f newVelocity = vel.sub(remove);
			
			v2D.setVelocity(newVelocity);
		}
		
		if (b.hasComponent(Velocity2D.class)) {
			Velocity2D v2D = b.getComponent(Velocity2D.class);
			
			Vector2f vel = v2D.getVelocity();
			Vector2f blockDir = coll.getCollision().getNormal();
			Vector2f remove = blockDir.scale(blockDir.dot(vel)).scale(1.0f - coll.getCollision().getTime());
			
			Vector2f newVelocity = vel.sub(remove);
			
			v2D.setVelocity(newVelocity);
		}
	}

	public static SweepCollision checkCollision(Entity a, Entity b, float dt) {
		Velocity2D velA = a.getComponent(Velocity2D.class);
		Velocity2D velB = b.getComponent(Velocity2D.class);
		
		BoundingBox2D boxA = a.getComponent(BoundingBox2D.class);
		BoundingBox2D boxB = b.getComponent(BoundingBox2D.class);
		
		Vector2f vA = velA == null ? new Vector2f(0, 0) : velA.getVelocity();
		Vector2f vB = velB == null ? new Vector2f(0, 0) : velB.getVelocity();
		
		if (vA.getX() == 0 && vA.getY() == 0) { // A STILL
			if (vB.getX() == 0 && vB.getY() == 0) { // A STILL | B STILL
				Collision c = checkDoubleStatic(boxA, boxB);
				float time = c != null ? 0 : 1;
				return new SweepCollision(c, boxB.getCenter(), time);
			}
			
			// A STILL | B MOVING
			Vector2f delta = vB.scale(dt);
			Collision c = checkStaticDynamic(boxA, boxB, delta, boxB.getHalfSize());
			if (c != null) {
				float time = MathUtils.clamp(c.getTime(), 0, 1);
				Vector2f pos = boxB.getCenter().add(delta).scale(time);
				Vector2f dir = delta.normalize();
				Vector2f hitPos = c.getPosition().add(boxB.getHalfSize().mul(dir));
				Collision newC = new Collision(a, b, hitPos, c.getIntersection(), c.getNormal(), time);
				return new SweepCollision(newC, pos, time);
			} else {
				Vector2f pos = boxB.getCenter().add(delta);
				float time = 1;
				return new SweepCollision(c, pos, time);
			}
		}
		
		if (vB.getX() == 0 && vB.getY() == 0) { // A MOVING | B STILL
			Vector2f delta = vA.scale(dt);
			Collision c = checkStaticDynamic(boxB, boxA, delta, boxA.getHalfSize());
			if (c != null) {
				float time = MathUtils.clamp(c.getTime(), 0, 1);
				Vector2f pos = boxA.getCenter().add(delta).scale(time);
				Vector2f dir = delta.normalize();
				Vector2f hitPos = c.getPosition().add(boxA.getHalfSize().mul(dir));
				Collision newC = new Collision(b, a, hitPos, c.getIntersection(), c.getNormal(), time);
				return new SweepCollision(newC, pos, time);
			} else {
				Vector2f pos = boxA.getCenter().add(delta);
				float time = 1;
				return new SweepCollision(c, pos, time);
			}
		}
		
		// A MOVING | B MOVING (CALCULATE AS IF A WAS STILL)
		Vector2f delta = vB.sub(vA).scale(dt);
		Collision c = checkDoubleDynamic(boxA, boxB, vA, vB, boxB.getHalfSize(), dt);
		if (c != null) {
			float time = MathUtils.clamp(c.getTime(), 0, 1);
			Vector2f pos = boxB.getCenter().add(delta).scale(time);
			Vector2f dir = delta.normalize();
			Vector2f hitPos = c.getPosition().add(boxB.getHalfSize().mul(dir));
			Collision newC = new Collision(a, b, hitPos, c.getIntersection(), c.getNormal(), time);
			return new SweepCollision(newC, pos, time);
		} else {
			Vector2f pos = boxB.getCenter().add(delta);
			float time = 1;
			return new SweepCollision(c, pos, time);
		}
	}
	
	public static Collision checkDoubleStatic(BoundingBox2D a, BoundingBox2D b) {
		Vector2f delta = b.getCenter().sub(a.getCenter());
		Vector2f proj = b.getHalfSize().add(a.getHalfSize()).sub(delta.abs());
		
		if (proj.getX() <= 0 || proj.getY() <= 0)
			return null;
		
		Vector2f pos;
		Vector2f inter;
		Vector2f n;
		
		if (proj.getX() < proj.getY()) {
			float signX = Math.signum(proj.getX());
			inter = new Vector2f(proj.getX() * signX, 0);
			n = new Vector2f(signX, 0);
			pos = new Vector2f(a.getCenter().getX() + (a.getHalfSize().getX() * signX), b.getCenter().getY());
		} else {
			float signY = Math.signum(proj.getY());
			inter = new Vector2f(0, proj.getY() * signY);
			n = new Vector2f(0, signY);
			pos = new Vector2f(b.getCenter().getX(), a.getCenter().getY() + (a.getHalfSize().getY() * signY));
		}
		
		return new Collision(a.getParent(), b.getParent(), pos, inter, n, 0);
	}
	
	public static Collision checkDoubleDynamic(BoundingBox2D boxA, BoundingBox2D boxB, Vector2f velA, Vector2f velB, Vector2f padding, float dt) {
		Vector2f delta = velB.sub(velA).scale(dt);
		
		Vector2f position = boxB.getCenter();
		
		float scaleX = 1.0f / delta.getX();
		float scaleY = 1.0f / delta.getY();
		
		float signX = Math.signum(scaleX);
		float signY = Math.signum(scaleY);
		
		if (delta.getX() == 0.0f) scaleX = signX * Float.MAX_VALUE;
		if (delta.getY() == 0.0f) scaleY = signY * Float.MAX_VALUE;
		
		Vector2f sign = new Vector2f(signX, signY);
		Vector2f scale = new Vector2f(scaleX, scaleY);
		
		float nearTimeX = (boxA.getCenter().getX() - signX * (boxA.getHalfSize().getX() + padding.getX()) - position.getX()) * scaleX;
		float nearTimeY = (boxA.getCenter().getY() - signY * (boxA.getHalfSize().getY() + padding.getY()) - position.getY()) * scaleY;
		float farTimeX = (boxA.getCenter().getX() + signX * (boxA.getHalfSize().getX() + padding.getX()) - position.getX()) * scaleX;
		float farTimeY = (boxA.getCenter().getY() + signY * (boxA.getHalfSize().getY() + padding.getY()) - position.getY()) * scaleY;
		
		if (nearTimeX > farTimeY || nearTimeY > farTimeX)
			return null;
		
		float nearTime = nearTimeX > nearTimeY ? nearTimeX : nearTimeY;
		float farTime = farTimeX < farTimeY ? farTimeX : farTimeY;
		
		if (nearTime >= 1 || farTime <= 0)
			return null;
		
		float time = MathUtils.clamp(nearTime, 0, 1);
		Vector2f n;
		if (nearTimeX > nearTimeY) {
			n = new Vector2f(-signX, 0);
		} else {
			n = new Vector2f(0, -signY);
		}
		
		Vector2f inter = delta.scale(time);
		Vector2f pos = position.add(delta);
		
		return new Collision(boxA.getParent(), boxB.getParent(), pos, inter, n, time);
	}
	
	public static Collision checkStaticDynamic(BoundingBox2D boxStill, BoundingBox2D boxMoving, Vector2f delta, Vector2f padding) {
		Vector2f position = boxMoving.getCenter();
		
		float scaleX = 1.0f / delta.getX();
		float scaleY = 1.0f / delta.getY();
		
		float signX = Math.signum(scaleX);
		float signY = Math.signum(scaleY);
		
		if (delta.getX() == 0.0f) scaleX = signX * Float.MAX_VALUE;
		if (delta.getY() == 0.0f) scaleY = signY * Float.MAX_VALUE;
		
		Vector2f sign = new Vector2f(signX, signY);
		Vector2f scale = new Vector2f(scaleX, scaleY);
		
		float nearTimeX = (boxStill.getCenter().getX() - signX * (boxStill.getHalfSize().getX() + padding.getX()) - position.getX()) * scaleX;
		float nearTimeY = (boxStill.getCenter().getY() - signY * (boxStill.getHalfSize().getY() + padding.getY()) - position.getY()) * scaleY;
		float farTimeX = (boxStill.getCenter().getX() + signX * (boxStill.getHalfSize().getX() + padding.getX()) - position.getX()) * scaleX;
		float farTimeY = (boxStill.getCenter().getY() + signY * (boxStill.getHalfSize().getY() + padding.getY()) - position.getY()) * scaleY;
		
		if (nearTimeX > farTimeY || nearTimeY > farTimeX)
			return null;
		
		float nearTime = nearTimeX > nearTimeY ? nearTimeX : nearTimeY;
		float farTime = farTimeX < farTimeY ? farTimeX : farTimeY;
		
		if (nearTime >= 1 || farTime <= 0)
			return null;
		
		float time = MathUtils.clamp(nearTime, 0, 1);
		Vector2f n;
		if (nearTimeX > nearTimeY) {
			n = new Vector2f(-signX, 0);
		} else {
			n = new Vector2f(0, -signY);
		}
		
		Vector2f inter = delta.scale(time);
		Vector2f pos = position.add(delta);
		
		return new Collision(boxStill.getParent(), boxMoving.getParent(), pos, inter, n, time);
	}
	
	public void update(List<Entity> entities, float dt) {
		for (Entity e : entities)
			if (e.hasComponent(CollisionComponent.class))
				e.clearComponents(CollisionComponent.class);
		
		List<BoundingBox2D> boundingBoxes = new ArrayList<BoundingBox2D>();
		for (Entity e: entities)
			if (e.hasComponent(BoundingBox2D.class))
				boundingBoxes.add(e.getComponent(BoundingBox2D.class));
				
		updateAPriori(entities, dt);
	} 
	
	public void updateAPriori(List<Entity> entities, float dt) {
		List<BoundingBox2D> boundingBoxes = new ArrayList<BoundingBox2D>();
		for (Entity e: entities)
			if (e.hasComponent(BoundingBox2D.class))
				boundingBoxes.add(e.getComponent(BoundingBox2D.class));
		
		List<BoundingBox2DSweep> boundingBoxSweeps = new ArrayList<BoundingBox2DSweep>();
		
		for (Entity e : entities) {
			if (e.hasComponent(BoundingBox2D.class)) {
				BoundingBox2D start = e.getComponent(BoundingBox2D.class);
				Velocity2D vel2D = e.getComponent(Velocity2D.class);
				Vector2f velocity = vel2D == null ? new Vector2f(0, 0) : vel2D.getVelocity().scale(dt);
				
				boolean moving = !velocity.equals(new Vector2f(0, 0)); 
				
				boundingBoxSweeps.add(new BoundingBox2DSweep(e, velocity, moving));
			}
		}
		
		for (int i = 0; i < boundingBoxSweeps.size(); i++) {
			for (int j = i + 1; j < boundingBoxSweeps.size(); j++) {
				BoundingBox2DSweep a = boundingBoxSweeps.get(i); // use as moving object
				BoundingBox2DSweep b = boundingBoxSweeps.get(j); // use as static object
				float collisionTime = 1;
				
				if (!a.isMoving() && b.isMoving()) {
					// Inflate A by B's size
//					System.out.println("A STILL");
					BoundingBox2D still = new BoundingBox2D(a.getBoundingBox().getCenter(), a.getBoundingBox().getHalfSize().add(b.getBoundingBox().getHalfSize()));
					collisionTime = getAABBSweepCollision(b, still);
				} else if (!b.isMoving() && a.isMoving()) {
					// Inflate B by A's size
//					System.out.println("B STILL");
					BoundingBox2D still = new BoundingBox2D(b.getBoundingBox().getCenter(), b.getBoundingBox().getHalfSize().add(a.getBoundingBox().getHalfSize()));
					collisionTime = getAABBSweepCollision(a, still);
				} else if (!a.isMoving() && !b.isMoving()) {
//					System.out.println("BOTH STILL");
					boolean colliding = a.getBoundingBox().intersects(b.getBoundingBox());
//					System.out.println(a.getBoundingBox().getCenter() + " : " + b.getBoundingBox().getCenter() + " : " + (colliding ? "YES" : "NO"));
					
					if (colliding) {
						Entity e1 = boundingBoxes.get(i).getParent();
						Entity e2 = boundingBoxes.get(j).getParent();
						
						Vector2f e1Center = boundingBoxes.get(i).getCenter();
						Vector2f e2Center = boundingBoxes.get(j).getCenter();
						
						Vector2f e1Min = e1Center.sub(boundingBoxes.get(i).getHalfSize());
						Vector2f e1Max = e1Center.add(boundingBoxes.get(i).getHalfSize());
						Vector2f e2Min = e2Center.sub(boundingBoxes.get(j).getHalfSize());
						Vector2f e2Max = e2Center.add(boundingBoxes.get(j).getHalfSize());
						
						float dX = Math.min(e1Max.getX() - e2Min.getX(), e2Max.getX() - e1Min.getX());
						float dY = Math.min(e1Max.getY() - e2Min.getY(), e2Max.getY() - e1Min.getY());
						
						Vector3f normal;
						float distance;
						
//						System.out.println(boundingBoxes.get(i).getCenter() + " : " + boundingBoxes.get(j).getCenter());
						
						if (dX < dY) {
							normal = new Vector3f(Math.signum(e1Center.getX() - e2Center.getX()), 0, 0);
							distance = dX;
						} else {
							normal = new Vector3f(0, Math.signum(e1Center.getY() - e2Center.getY()), 0);
							distance = dY;
						}
						
//						System.out.println("X: " + dX + ", Y: " + dY);
						
						if (boundingBoxes.get(i).isTrigger())
							e2.addComponent(new TriggerComponent(e1, normal.negate()));
						else
							e2.addComponent(new CollisionComponent(e1, normal.negate(), distance, collisionTime));
						
						if (boundingBoxes.get(j).isTrigger())
							e1.addComponent(new TriggerComponent(e2, normal));
						else
							e1.addComponent(new CollisionComponent(e2, normal, distance, collisionTime));
						
						continue;
					}
				} else {
					// Inflate B by A's size
					// Create new sweep
					Vector2f aVel = a.getVelocity();
					Vector2f bVel = b.getVelocity();
					BoundingBox2D bStart = b.getBoundingBox();
					
					Vector2f newVel = aVel.sub(bVel);
					
					Vector2f newHalfSize = bStart.getHalfSize().add(a.getBoundingBox().getHalfSize());
					BoundingBox2D still = new BoundingBox2D(bStart.getCenter(), newHalfSize);
					
					BoundingBox2DSweep moving = new BoundingBox2DSweep(a.getEntity(), newVel, true);
					
					collisionTime = getAABBSweepCollision(moving, still);
				}
				
				if (collisionTime <= 0.0f)
					continue;
				
				Entity e1 = boundingBoxSweeps.get(i).getBoundingBox().getParent();
				Entity e2 = boundingBoxSweeps.get(j).getBoundingBox().getParent();
				
				Vector2f e1Center = boundingBoxes.get(i).getCenter();
				Vector2f e2Center = boundingBoxes.get(j).getCenter();
				
				Vector2f e1Min = e1Center.sub(boundingBoxes.get(i).getHalfSize());
				Vector2f e1Max = e1Center.add(boundingBoxes.get(i).getHalfSize());
				Vector2f e2Min = e2Center.sub(boundingBoxes.get(j).getHalfSize());
				Vector2f e2Max = e2Center.add(boundingBoxes.get(j).getHalfSize());
				
				float dX = Math.min(e1Max.getX() - e2Min.getX(), e2Max.getX() - e1Min.getX());
				float dY = Math.min(e1Max.getY() - e2Min.getY(), e2Max.getY() - e1Min.getY());
				
				Vector3f normal;
				float distance;
				
				if (dX < dY) {
					normal = new Vector3f(Math.signum(e1Center.getX() - e2Center.getX()), 0, 0);
					distance = dX;
				} else {
					normal = new Vector3f(0, Math.signum(e1Center.getY() - e2Center.getY()), 0);
					distance = dY;
				}
				
				if (boundingBoxes.get(i).isTrigger())
					e2.addComponent(new TriggerComponent(e1, normal.negate()));
				else
					e2.addComponent(new CollisionComponent(e1, normal.negate(), distance, 0));
				
				if (boundingBoxes.get(j).isTrigger())
					e1.addComponent(new TriggerComponent(e2, normal));
				else
					e1.addComponent(new CollisionComponent(e2, normal, distance, 0));
			}
		}
	}
	
	public float getAABBSweepCollision(BoundingBox2DSweep a, BoundingBox2D b) {
		Vector2f position = a.getBoundingBox().getCenter();
		Vector2f velocity = a.getVelocity();
		
		float nearTimeX = 0, nearTimeY = 0, farTimeX = 0, farTimeY = 0;
		
		float scaleX = 1f / velocity.getX();
		float scaleY = 1f / velocity.getY();
		
		float signX = Math.signum(scaleX);
		float signY = Math.signum(scaleY);
			
		nearTimeX = (b.getCenter().getX() - signX * b.getHalfSize().getX() - position.getX()) * scaleX;
		nearTimeY = (b.getCenter().getY() - signY * b.getHalfSize().getY() - position.getY()) * scaleY;
		farTimeX = (b.getCenter().getX() + signX * b.getHalfSize().getX() - position.getX()) * scaleX;
		farTimeY = (b.getCenter().getY() + signY * b.getHalfSize().getY() - position.getY()) * scaleY;
		
		if (nearTimeX > farTimeY || nearTimeY > farTimeX)
			return -1;
		
		float nearTime = nearTimeX > nearTimeY ? nearTimeX : nearTimeY;
		float farTime = farTimeX < farTimeY ? farTimeX : farTimeY;
		
		if (nearTime >= 1 || farTime <= 0)
			return -1;
		
		return MathUtils.clamp(nearTime, 0, 1);
	}
	
	public static boolean checkAgainstWorld(Vector2f point, List<Entity> entities) {
		for (Entity e : entities) {
			BoundingBox2D b = e.getComponent(BoundingBox2D.class);
			
			if (b.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void updateCollision(List<BoundingBox2D> boundingBoxes) {
		for (BoundingBox2D b: boundingBoxes)
			b.update();
		
		for (int i = 0; i < boundingBoxes.size(); i++) {
			for (int j = i + 1; j < boundingBoxes.size(); j++) {
				if (boundingBoxes.get(i).intersects(boundingBoxes.get(j))) {
					Entity e1 = boundingBoxes.get(i).getParent();
					Entity e2 = boundingBoxes.get(j).getParent();
					
					Vector2f e1Center = boundingBoxes.get(i).getCenter();
					Vector2f e2Center = boundingBoxes.get(j).getCenter();
					
					Vector2f e1Min = e1Center.sub(boundingBoxes.get(i).getHalfSize());
					Vector2f e1Max = e1Center.add(boundingBoxes.get(i).getHalfSize());
					Vector2f e2Min = e2Center.sub(boundingBoxes.get(j).getHalfSize());
					Vector2f e2Max = e2Center.add(boundingBoxes.get(j).getHalfSize());
					
					float dX = Math.min(e1Max.getX() - e2Min.getX(), e2Max.getX() - e1Min.getX());
					float dY = Math.min(e1Max.getY() - e2Min.getY(), e2Max.getY() - e1Min.getY());
					
					Vector3f normal;
					float distance;
					
					if (dX < dY) {
						normal = new Vector3f(Math.signum(e1Center.getX() - e2Center.getX()), 0, 0);
						distance = dX;
					} else {
						normal = new Vector3f(0, Math.signum(e1Center.getY() - e2Center.getY()), 0);
						distance = dY;
					}
					
					if (boundingBoxes.get(i).isTrigger())
						e2.addComponent(new TriggerComponent(e1, normal.negate()));
					else
						e2.addComponent(new CollisionComponent(e1, normal.negate(), distance, 0));
					
					if (boundingBoxes.get(j).isTrigger())
						e1.addComponent(new TriggerComponent(e2, normal));
					else
						e1.addComponent(new CollisionComponent(e2, normal, distance, 0));
				}
			}
		}
	}

	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(BoundingBox2D.class);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
