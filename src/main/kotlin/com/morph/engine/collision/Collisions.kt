package com.morph.engine.collision

import com.morph.engine.entities.Entity
import com.morph.engine.math.Vector2f

sealed class Collision {
    class NoHit : Collision()
    class Hit(val entity: Entity, val hit: Entity, val data: CollisionData) : Collision()
}

data class CollisionData(val position: Vector2f, val intersection: Vector2f, val normal: Vector2f, val time: Float)

sealed class SweepCollision {
    class NoHit : SweepCollision()
    class Hit(val entity: Entity, val hit: Entity, val collisionData: CollisionData, val position: Vector2f, val time: Float) : SweepCollision()
}

