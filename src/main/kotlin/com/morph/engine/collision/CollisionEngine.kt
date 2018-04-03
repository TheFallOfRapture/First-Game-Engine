package com.morph.engine.collision

import com.morph.engine.collision.components.BoundingBox2D
import com.morph.engine.collision.components.CollisionComponent
import com.morph.engine.collision.components.TriggerComponent
import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.entities.given
import com.morph.engine.math.MathUtils
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.Transform2D
import com.morph.engine.physics.components.Velocity2D
import com.morph.engine.util.Feed
import io.reactivex.Observable
import java.util.*

// Reference: noonat.github.io/intersect/

class CollisionEngine(game: Game) : GameSystem(game) {
    private val collisionFeed = Feed<Collision>()
    private val collisionEvents = Observable.create<Collision> { collisionFeed.emit(it) }

    init {
        game.events.filter { e -> e == Game.GameAction.CLOSE }.subscribe { e -> collisionFeed.onComplete() }
    }

    override fun fixedUpdate(e: Entity, dt: Float) {
        updateCollider(e, e.getComponent(BoundingBox2D::class.java)!!, e.getComponent(Transform2D::class.java))
    }

    override fun systemFixedUpdate(dt: Float) {
        val collidables = ArrayList<Entity>()
        for (i in game.world.entities.size - 1 downTo 0) {
            val e = game.world.entities[i]
            if (e != null && acceptEntity(e)) {
                collidables.add(e)
            }
        }

        checkCollision(collidables, dt)
    }

    private fun checkCollision(entities: List<Entity>, dt: Float) {
        for (i in entities.indices) {
            for (j in i + 1 until entities.size) {
                val a = entities[i]
                val b = entities[j]

                given<BoundingBox2D>(a) { boxA ->
                    given<BoundingBox2D>(b) { boxB ->
                        val coll = checkCollision(a, b, dt)

                        if (coll is SweepCollision.Hit) { // Collision Solver
                            val (x, y) = coll.collisionData.intersection
                            val distance = if (x == 0f) x else y
                            val normal = Vector3f(coll.collisionData.normal, 0f)

                            if (!boxA.isTrigger && !boxB.isTrigger)
                                solveCollision(coll)

                            if (boxA.isTrigger)
                                b.addComponent(TriggerComponent(a, normal))
                            else
                                b.addComponent(CollisionComponent(a, normal, distance, coll.time))

                            if (boxB.isTrigger)
                                a.addComponent(TriggerComponent(b, normal))
                            else
                                a.addComponent(CollisionComponent(b, normal, distance, coll.time))

                            val collision = Collision.Hit(a, b, coll.collisionData)
                            collisionFeed.onNext(collision)
                        }
                    }
                }
            }
        }
    }

    private fun solveCollision(coll: SweepCollision) {
        if (coll !is SweepCollision.Hit) return

        val a = coll.entity
        val b = coll.hit

        given<Velocity2D>(a) { v2D ->
            val vel = v2D.velocity
            val blockDir = -coll.collisionData.normal
            val remove = blockDir * (blockDir dot vel) * (1.0f - coll.collisionData.time)

            val newVelocity = vel - remove

            v2D.velocity = newVelocity
        }

        given<Velocity2D>(b) { v2D ->
            val vel = v2D.velocity
            val blockDir = coll.collisionData.normal
            val remove = blockDir * (blockDir dot vel) * (1.0f - coll.collisionData.time)

            val newVelocity = vel - remove

            v2D.velocity = newVelocity
        }
    }

    fun update(entities: List<Entity>, dt: Float) {
        for (e in entities)
            if (e.hasComponent(CollisionComponent::class.java))
                e.clearComponents(CollisionComponent::class.java)

        updateAPriori(entities, dt)
    }

    private fun updateAPriori(entities: List<Entity>, dt: Float) {
        val boundingBoxes = ArrayList<BoundingBox2D>()
        for (e in entities)
            given<BoundingBox2D>(e) {
                boundingBoxes.add(it)
            }

        val boundingBoxSweeps = ArrayList<BoundingBox2DSweep>()

        for (e in entities) {
            if (e.hasComponent(BoundingBox2D::class.java)) {
                val start = e.getComponent(BoundingBox2D::class.java)
                val vel2D = e.getComponent(Velocity2D::class.java)
                val velocity = if (vel2D == null) Vector2f(0f, 0f) else vel2D.velocity * dt

                val moving = velocity != Vector2f(0f, 0f)

                boundingBoxSweeps.add(BoundingBox2DSweep(e, velocity, moving))
            }
        }

        for (i in boundingBoxSweeps.indices) {
            for (j in i + 1 until boundingBoxSweeps.size) {
                val a = boundingBoxSweeps[i] // use as moving object
                val b = boundingBoxSweeps[j] // use as static object
                var collisionTime = 1f

                if (!a.isMoving && b.isMoving) {
                    // Inflate A by B's size
                    //					System.out.println("A STILL");
                    val still = BoundingBox2D(a.boundingBox.center, a.boundingBox.halfSize + (b.boundingBox.halfSize))
                    collisionTime = getAABBSweepCollision(b, still)
                } else if (!b.isMoving && a.isMoving) {
                    // Inflate B by A's size
                    //					System.out.println("B STILL");
                    val still = BoundingBox2D(b.boundingBox.center, b.boundingBox.halfSize + (a.boundingBox.halfSize))
                    collisionTime = getAABBSweepCollision(a, still)
                } else if (!a.isMoving && !b.isMoving) {
                    //					System.out.println("BOTH STILL");
                    val colliding = a.boundingBox.intersects(b.boundingBox)
                    //					System.out.println(a.getBoundingBox().getCenter() + " : " + b.getBoundingBox().getCenter() + " : " + (colliding ? "YES" : "NO"));

                    if (colliding) {
                        val e1 = a.entity
                        val e2 = b.entity

                        val e1Center = boundingBoxes[i].center
                        val e2Center = boundingBoxes[j].center

                        val e1Min = e1Center - (boundingBoxes[i].halfSize)
                        val e1Max = e1Center + (boundingBoxes[i].halfSize)
                        val e2Min = e2Center - (boundingBoxes[j].halfSize)
                        val e2Max = e2Center + (boundingBoxes[j].halfSize)

                        val dX = Math.min(e1Max.x - e2Min.x, e2Max.x - e1Min.x)
                        val dY = Math.min(e1Max.y - e2Min.y, e2Max.y - e1Min.y)

                        val normal: Vector3f
                        val distance: Float

                        //						System.out.println(boundingBoxes.get(i).getCenter() + " : " + boundingBoxes.get(j).getCenter());

                        if (dX < dY) {
                            normal = Vector3f(Math.signum(e1Center.x - e2Center.x), 0f, 0f)
                            distance = dX
                        } else {
                            normal = Vector3f(0f, Math.signum(e1Center.y - e2Center.y), 0f)
                            distance = dY
                        }

                        //						System.out.println("X: " + dX + ", Y: " + dY);

                        if (boundingBoxes[i].isTrigger)
                            e2.addComponent(TriggerComponent(e1, -normal))
                        else
                            e2.addComponent(CollisionComponent(e1, -normal, distance, collisionTime))

                        if (boundingBoxes[j].isTrigger)
                            e1.addComponent(TriggerComponent(e2, normal))
                        else
                            e1.addComponent(CollisionComponent(e2, normal, distance, collisionTime))

                        continue
                    }
                } else {
                    // Inflate B by A's size
                    // Create new sweep
                    val aVel = a.velocity
                    val bVel = b.velocity
                    val bStart = b.boundingBox

                    val newVel = aVel - bVel

                    val newHalfSize = bStart!!.halfSize + (a.boundingBox!!.halfSize)
                    val still = BoundingBox2D(bStart.center, newHalfSize)

                    val moving = BoundingBox2DSweep(a.entity, newVel, true)

                    collisionTime = getAABBSweepCollision(moving, still)
                }

                if (collisionTime <= 0.0f)
                    continue

                val e1 = boundingBoxSweeps[i].entity
                val e2 = boundingBoxSweeps[j].entity

                val e1Center = boundingBoxes[i].center
                val e2Center = boundingBoxes[j].center

                val e1Min = e1Center - (boundingBoxes[i].halfSize)
                val e1Max = e1Center + (boundingBoxes[i].halfSize)
                val e2Min = e2Center - (boundingBoxes[j].halfSize)
                val e2Max = e2Center + (boundingBoxes[j].halfSize)

                val dX = Math.min(e1Max.x - e2Min.x, e2Max.x - e1Min.x)
                val dY = Math.min(e1Max.y - e2Min.y, e2Max.y - e1Min.y)

                val normal: Vector3f
                val distance: Float

                if (dX < dY) {
                    normal = Vector3f(Math.signum(e1Center.x - e2Center.x), 0f, 0f)
                    distance = dX
                } else {
                    normal = Vector3f(0f, Math.signum(e1Center.y - e2Center.y), 0f)
                    distance = dY
                }

                if (boundingBoxes[i].isTrigger)
                    e2.addComponent(TriggerComponent(e1, -normal))
                else
                    e2.addComponent(CollisionComponent(e1, -normal, distance, 0f))

                if (boundingBoxes[j].isTrigger)
                    e1.addComponent(TriggerComponent(e2, normal))
                else
                    e1.addComponent(CollisionComponent(e2, normal, distance, 0f))
            }
        }
    }

    private fun getAABBSweepCollision(a: BoundingBox2DSweep, b: BoundingBox2D): Float {
        val (x, y) = a.boundingBox!!.center
        val velocity = a.velocity

        val nearTimeX: Float
        val nearTimeY: Float
        val farTimeX: Float
        val farTimeY: Float

        val scaleX = 1f / velocity.x
        val scaleY = 1f / velocity.y

        val signX = Math.signum(scaleX)
        val signY = Math.signum(scaleY)

        nearTimeX = (b.center.x - signX * b.halfSize.x - x) * scaleX
        nearTimeY = (b.center.y - signY * b.halfSize.y - y) * scaleY
        farTimeX = (b.center.x + signX * b.halfSize.x - x) * scaleX
        farTimeY = (b.center.y + signY * b.halfSize.y - y) * scaleY

        if (nearTimeX > farTimeY || nearTimeY > farTimeX)
            return -1f

        val nearTime = if (nearTimeX > nearTimeY) nearTimeX else nearTimeY
        val farTime = if (farTimeX < farTimeY) farTimeX else farTimeY

        return if (nearTime >= 1 || farTime <= 0) -1f else MathUtils.clamp(nearTime, 0f, 1f)

    }

    override fun acceptEntity(e: Entity): Boolean {
        return e.hasComponent(BoundingBox2D::class.java)
    }

    override fun initSystem() {
        // TODO Auto-generated method stub

    }

    companion object {

        private fun updateCollider(e: Entity, collider: BoundingBox2D, transform: Transform2D?) {
            collider.center = e.getComponent(Transform2D::class.java)!!.position
        }

        fun checkAgainstWorldStatic(boxA: BoundingBox2D, entities: List<Entity>): List<CollisionData> {
            val result = ArrayList<CollisionData>()
            for (other in entities) {
                given<BoundingBox2D>(other) { boxB ->
                    val coll = checkDoubleStatic(boxA, boxB)
                    if (coll != null)
                        result.add(coll)
                }
            }

            return result
        }

        fun checkAgainstWorld(boxA: BoundingBox2D, velA: Vector2f, entities: List<Entity>, dt: Float): List<CollisionData> {
            val result = ArrayList<CollisionData>()

            for (entity in entities) {
                given<BoundingBox2D>(entity) { boxB ->
                    val vel2D = entity.getComponent(Velocity2D::class.java)
                    val velB = if (vel2D == null) Vector2f(0f, 0f) else vel2D.velocity

                    val coll: CollisionData?

                    coll = if (velA == Vector2f(0f, 0f) && velB == Vector2f(0f, 0f))
                        checkDoubleStatic(boxA, boxB)
                    else if (velA == Vector2f(0f, 0f) && velB != Vector2f(0f, 0f))
                        checkStaticDynamic(boxA, boxB, velB * dt)
                    else if (velA != Vector2f(0f, 0f) && velB == Vector2f(0f, 0f))
                        checkStaticDynamic(boxB, boxA, velA * dt)
                    else
                        checkDoubleDynamic(boxA, boxB, velA, velB, dt)

                    if (coll != null) {
                        result.add(coll)
                    }
                }
            }

            return result
        }

        private fun checkCollision(a: Entity, b: Entity, dt: Float): SweepCollision {
            val velA = a.getComponent(Velocity2D::class.java)
            val velB = b.getComponent(Velocity2D::class.java)

            given<BoundingBox2D>(a) { boxA ->
                given<BoundingBox2D>(b) { boxB ->
                    val vA = if (velA == null) Vector2f(0f, 0f) else velA.velocity
                    val vB = if (velB == null) Vector2f(0f, 0f) else velB.velocity

                    if (vA.x == 0f && vA.y == 0f) { // A STILL
                        if (vB.x == 0f && vB.y == 0f) { // A STILL | B STILL
                            val c = checkDoubleStatic(boxA, boxB)
                            return if (c != null) SweepCollision.Hit(a, b, c, boxB.center, 0f) else SweepCollision.NoHit()
                        }

                        // A STILL | B MOVING
                        val delta = vB * dt
                        val c = checkStaticDynamic(boxA, boxB, delta)
                        return getSweepCollision(a, b, boxB, delta, c)
                    }

                    if (vB.x == 0f && vB.y == 0f) { // A MOVING | B STILL
                        val delta = vA * dt
                        val c = checkStaticDynamic(boxB, boxA, delta)
                        return getSweepCollision(b, a, boxA, delta, c)
                    }

                    // A MOVING | B MOVING (CALCULATE AS IF A WAS STILL)
                    val delta = (vB - vA) * dt
                    val c = checkDoubleDynamic(boxA, boxB, vA, vB, dt)
                    return getSweepCollision(a, b, boxB, delta, c)
                }
            }

            return SweepCollision.NoHit()
        }

        private fun getSweepCollision(a: Entity, b: Entity, boxB: BoundingBox2D, delta: Vector2f, c: CollisionData?) = if (c != null) {
            val time = MathUtils.clamp(c.time, 0f, 1f)
            val pos = (boxB.center + delta) * (time)
            val dir = delta.normalize()
            val hitPos = (c.position + boxB.halfSize) * dir
            val newC = CollisionData(hitPos, c.intersection, c.normal, time)
            SweepCollision.Hit(a, b, newC, pos, time)
        } else {
            SweepCollision.NoHit()
        }

        private fun checkDoubleStatic(a: BoundingBox2D, b: BoundingBox2D): CollisionData? {
            val delta = b.center - a.center
            val proj = b.halfSize + a.halfSize - delta.abs()

            if (proj.x <= 0 || proj.y <= 0)
                return null

            val pos: Vector2f
            val inter: Vector2f
            val n: Vector2f

            if (proj.x < proj.y) {
                val signX = Math.signum(proj.x)
                inter = Vector2f(proj.x * signX, 0f)
                n = Vector2f(signX, 0f)
                pos = Vector2f(a.center.x + a.halfSize.x * signX, b.center.y)
            } else {
                val signY = Math.signum(proj.y)
                inter = Vector2f(0f, proj.y * signY)
                n = Vector2f(0f, signY)
                pos = Vector2f(b.center.x, a.center.y + a.halfSize.y * signY)
            }

            return CollisionData(pos, inter, n, 0f)
        }

        private fun checkDoubleDynamic(boxA: BoundingBox2D, boxB: BoundingBox2D, velA: Vector2f, velB: Vector2f, dt: Float): CollisionData? {
            val delta = (velB - velA) * dt
            val (x, y) = boxB.halfSize

            val position = boxB.center

            var scaleX = 1.0f / delta.x
            var scaleY = 1.0f / delta.y

            val signX = Math.signum(scaleX)
            val signY = Math.signum(scaleY)

            if (delta.x == 0.0f) scaleX = signX * java.lang.Float.MAX_VALUE
            if (delta.y == 0.0f) scaleY = signY * java.lang.Float.MAX_VALUE

            val (x1, y1) = Vector2f(signX, signY)
            val scale = Vector2f(scaleX, scaleY)

            val nearTimeX = (boxA.center.x - signX * (boxA.halfSize.x + x) - position.x) * scaleX
            val nearTimeY = (boxA.center.y - signY * (boxA.halfSize.y + y) - position.y) * scaleY
            val farTimeX = (boxA.center.x + signX * (boxA.halfSize.x + x) - position.x) * scaleX
            val farTimeY = (boxA.center.y + signY * (boxA.halfSize.y + y) - position.y) * scaleY

            if (nearTimeX > farTimeY || nearTimeY > farTimeX)
                return null

            val nearTime = if (nearTimeX > nearTimeY) nearTimeX else nearTimeY
            val farTime = if (farTimeX < farTimeY) farTimeX else farTimeY

            if (nearTime >= 1 || farTime <= 0)
                return null

            val time = MathUtils.clamp(nearTime, 0f, 1f)
            val n: Vector2f
            if (nearTimeX > nearTimeY) {
                n = Vector2f(-signX, 0f)
            } else {
                n = Vector2f(0f, -signY)
            }

            val inter = delta * time
            val pos = position + delta

            return CollisionData(pos, inter, n, time)
        }

        private fun checkStaticDynamic(boxStill: BoundingBox2D, boxMoving: BoundingBox2D, delta: Vector2f): CollisionData? {
            val position = boxMoving.center
            val (x, y) = boxMoving.halfSize

            var scaleX = 1.0f / delta.x
            var scaleY = 1.0f / delta.y

            val signX = Math.signum(scaleX)
            val signY = Math.signum(scaleY)

            if (delta.x == 0.0f) scaleX = signX * java.lang.Float.MAX_VALUE
            if (delta.y == 0.0f) scaleY = signY * java.lang.Float.MAX_VALUE

            val (x1, y1) = Vector2f(signX, signY)
            val scale = Vector2f(scaleX, scaleY)

            val nearTimeX = (boxStill.center.x - signX * (boxStill.halfSize.x + x) - position.x) * scaleX
            val nearTimeY = (boxStill.center.y - signY * (boxStill.halfSize.y + y) - position.y) * scaleY
            val farTimeX = (boxStill.center.x + signX * (boxStill.halfSize.x + x) - position.x) * scaleX
            val farTimeY = (boxStill.center.y + signY * (boxStill.halfSize.y + y) - position.y) * scaleY

            if (nearTimeX > farTimeY || nearTimeY > farTimeX)
                return null

            val nearTime = if (nearTimeX > nearTimeY) nearTimeX else nearTimeY
            val farTime = if (farTimeX < farTimeY) farTimeX else farTimeY

            if (nearTime >= 1 || farTime <= 0)
                return null

            val time = MathUtils.clamp(nearTime, 0f, 1f)
            val n: Vector2f
            if (nearTimeX > nearTimeY) {
                n = Vector2f(-signX, 0f)
            } else {
                n = Vector2f(0f, -signY)
            }

            val inter = delta * time
            val pos = position + delta

            return CollisionData(pos, inter, n, time)
        }

        fun checkAgainstWorld(point: Vector2f, entities: List<Entity>): Boolean {
            for (e in entities) {
                given<BoundingBox2D>(e) { b ->
                    if (b.contains(point)) {
                        return true
                    }
                }
            }

            return false
        }
    }
}
