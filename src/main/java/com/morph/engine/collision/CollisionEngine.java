package com.morph.engine.collision;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.collision.components.CollisionComponent;
import com.morph.engine.collision.components.TriggerComponent;
import com.morph.engine.core.Game;
import com.morph.engine.core.GameSystem;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityRectangle;
import com.morph.engine.math.MathUtils;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.physics.components.Velocity2D;
import com.morph.engine.util.Feed;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

// Reference: noonat.github.io/intersect/

public class CollisionEngine extends GameSystem {
	private final Feed<Collision> collisionFeed = new Feed<>();
	private Observable<Collision> collisionEvents = Observable.create(collisionFeed::emit);

	public CollisionEngine(Game game) {
		super(game);
		game.getEvents().filter(e -> e == Game.GameAction.CLOSE).subscribe(e -> collisionFeed.onComplete());
	}
	
	protected void fixedUpdate(Entity e, float dt) {
		updateCollider(e, e.getComponent(BoundingBox2D.class), e.getComponent(Transform2D.class));
	}
	
	protected void systemFixedUpdate(float dt) {
		List<Entity> collidables = new ArrayList<>();
		for (int i = game.getWorld().getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getWorld().getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				collidables.add(e);
			}
		}
		
		checkCollision(collidables, dt);
	}

	private static void updateCollider(Entity e, BoundingBox2D collider, Transform2D transform) {
		if (e instanceof EntityRectangle) collider.setCenter(((EntityRectangle) e).getCenter());
		else collider.setCenter(e.getComponent(Transform2D.class).getPosition());
	}
	
	public static List<CollisionData> checkAgainstWorldStatic(BoundingBox2D boxA, List<Entity> entities) {
		List<CollisionData> result = new ArrayList<>();
		for (Entity other : entities) {
			BoundingBox2D boxB = other.getComponent(BoundingBox2D.class);
			CollisionData coll = checkDoubleStatic(boxA, boxB);
			if (coll != null)
				result.add(coll);
		}
		
		return result;
	}
	
	public static List<CollisionData> checkAgainstWorld(BoundingBox2D boxA, Vector2f velA, List<Entity> entities, float dt) {
		List<CollisionData> result = new ArrayList<>();

		for (Entity entity : entities) {
			BoundingBox2D boxB = entity.getComponent(BoundingBox2D.class);
			Velocity2D vel2D = entity.getComponent(Velocity2D.class);
			Vector2f velB = vel2D == null ? new Vector2f(0, 0) : vel2D.getVelocity();

			CollisionData coll;

			if (velA.equals(new Vector2f(0, 0)) && velB.equals(new Vector2f(0, 0)))
				coll = checkDoubleStatic(boxA, boxB);
			else if (velA.equals(new Vector2f(0, 0)) && !velB.equals(new Vector2f(0, 0)))
				coll = checkStaticDynamic(boxA, boxB, velB.scale(dt));
			else if (!velA.equals(new Vector2f(0, 0)) && velB.equals(new Vector2f(0, 0)))
				coll = checkStaticDynamic(boxB, boxA, velA.scale(dt));
			else
				coll = checkDoubleDynamic(boxA, boxB, velA, velB, dt);

			if (coll != null) {
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
				
				if (coll != null) { // Collision Solver
					Vector2f delta = coll.getCollisionData().getIntersection();
					float distance = delta.getX() == 0 ? delta.getX() : delta.getY();
					Vector3f normal = new Vector3f(coll.getCollisionData().getNormal(), 0);
					
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

					Collision collision = new Collision(a, b, coll.getCollisionData());
					collisionFeed.onNext(collision);
				}
			}
		}
	}
	
	private void solveCollision(SweepCollision coll) {
		Entity a = coll.getEntity();
		Entity b = coll.getHit();
		
		if (a.hasComponent(Velocity2D.class)) {
			Velocity2D v2D = a.getComponent(Velocity2D.class);
			
			Vector2f vel = v2D.getVelocity();
			Vector2f blockDir = coll.getCollisionData().getNormal().negate();
			Vector2f remove = blockDir.scale(blockDir.dot(vel)).scale(1.0f - coll.getCollisionData().getTime());
			
			Vector2f newVelocity = vel.sub(remove);
			
			v2D.setVelocity(newVelocity);
		}
		
		if (b.hasComponent(Velocity2D.class)) {
			Velocity2D v2D = b.getComponent(Velocity2D.class);
			
			Vector2f vel = v2D.getVelocity();
			Vector2f blockDir = coll.getCollisionData().getNormal();
			Vector2f remove = blockDir.scale(blockDir.dot(vel)).scale(1.0f - coll.getCollisionData().getTime());
			
			Vector2f newVelocity = vel.sub(remove);
			
			v2D.setVelocity(newVelocity);
		}
	}

	private static SweepCollision checkCollision(Entity a, Entity b, float dt) {
		Velocity2D velA = a.getComponent(Velocity2D.class);
		Velocity2D velB = b.getComponent(Velocity2D.class);
		
		BoundingBox2D boxA = a.getComponent(BoundingBox2D.class);
		BoundingBox2D boxB = b.getComponent(BoundingBox2D.class);
		
		Vector2f vA = velA == null ? new Vector2f(0, 0) : velA.getVelocity();
		Vector2f vB = velB == null ? new Vector2f(0, 0) : velB.getVelocity();
		
		if (vA.getX() == 0 && vA.getY() == 0) { // A STILL
			if (vB.getX() == 0 && vB.getY() == 0) { // A STILL | B STILL
				CollisionData c = checkDoubleStatic(boxA, boxB);
				float time = c != null ? 0 : 1;
				return new SweepCollision(a, b, c, boxB.getCenter(), time);
			}
			
			// A STILL | B MOVING
			Vector2f delta = vB.scale(dt);
			CollisionData c = checkStaticDynamic(boxA, boxB, delta);
			return getSweepCollision(a, b, boxB, delta, c);
		}
		
		if (vB.getX() == 0 && vB.getY() == 0) { // A MOVING | B STILL
			Vector2f delta = vA.scale(dt);
			CollisionData c = checkStaticDynamic(boxB, boxA, delta);
			return getSweepCollision(b, a, boxA, delta, c);
		}
		
		// A MOVING | B MOVING (CALCULATE AS IF A WAS STILL)
		Vector2f delta = vB.sub(vA).scale(dt);
		CollisionData c = checkDoubleDynamic(boxA, boxB, vA, vB, dt);
		return getSweepCollision(a, b, boxB, delta, c);
	}

	private static SweepCollision getSweepCollision(Entity a, Entity b, BoundingBox2D boxB, Vector2f delta, CollisionData c) {
		if (c != null) {
			float time = MathUtils.INSTANCE.clamp(c.getTime(), 0f, 1f);
			Vector2f pos = boxB.getCenter().add(delta).scale(time);
			Vector2f dir = delta.normalize();
			Vector2f hitPos = c.getPosition().add(boxB.getHalfSize().mul(dir));
			CollisionData newC = new CollisionData(hitPos, c.getIntersection(), c.getNormal(), time);
			return new SweepCollision(a, b, newC, pos, time);
		} else {
			return null;
		}
	}

	private static CollisionData checkDoubleStatic(BoundingBox2D a, BoundingBox2D b) {
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
		
		return new CollisionData(pos, inter, n, 0);
	}
	
	private static CollisionData checkDoubleDynamic(BoundingBox2D boxA, BoundingBox2D boxB, Vector2f velA, Vector2f velB, float dt) {
		Vector2f delta = velB.sub(velA).scale(dt);
		Vector2f padding = boxB.getHalfSize();
		
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
		
		float time = MathUtils.INSTANCE.clamp(nearTime, 0f, 1f);
		Vector2f n;
		if (nearTimeX > nearTimeY) {
			n = new Vector2f(-signX, 0);
		} else {
			n = new Vector2f(0, -signY);
		}
		
		Vector2f inter = delta.scale(time);
		Vector2f pos = position.add(delta);
		
		return new CollisionData(pos, inter, n, time);
	}
	
	private static CollisionData checkStaticDynamic(BoundingBox2D boxStill, BoundingBox2D boxMoving, Vector2f delta) {
		Vector2f position = boxMoving.getCenter();
		Vector2f padding = boxMoving.getHalfSize();
		
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
		
		float time = MathUtils.INSTANCE.clamp(nearTime, 0f, 1f);
		Vector2f n;
		if (nearTimeX > nearTimeY) {
			n = new Vector2f(-signX, 0);
		} else {
			n = new Vector2f(0, -signY);
		}
		
		Vector2f inter = delta.scale(time);
		Vector2f pos = position.add(delta);
		
		return new CollisionData(pos, inter, n, time);
	}
	
	public void update(List<Entity> entities, float dt) {
		for (Entity e : entities)
			if (e.hasComponent(CollisionComponent.class))
				e.clearComponents(CollisionComponent.class);
		
		updateAPriori(entities, dt);
	} 
	
	private void updateAPriori(List<Entity> entities, float dt) {
		List<BoundingBox2D> boundingBoxes = new ArrayList<>();
		for (Entity e: entities)
			if (e.hasComponent(BoundingBox2D.class))
				boundingBoxes.add(e.getComponent(BoundingBox2D.class));
		
		List<BoundingBox2DSweep> boundingBoxSweeps = new ArrayList<>();

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
						Entity e1 = a.getEntity();
						Entity e2 = b.getEntity();

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

				Entity e1 = boundingBoxSweeps.get(i).getEntity();
				Entity e2 = boundingBoxSweeps.get(j).getEntity();

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
	
	private float getAABBSweepCollision(BoundingBox2DSweep a, BoundingBox2D b) {
		Vector2f position = a.getBoundingBox().getCenter();
		Vector2f velocity = a.getVelocity();
		
		float nearTimeX, nearTimeY, farTimeX, farTimeY;
		
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
		
		return MathUtils.INSTANCE.clamp(nearTime, 0f, 1f);
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

	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(BoundingBox2D.class);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
