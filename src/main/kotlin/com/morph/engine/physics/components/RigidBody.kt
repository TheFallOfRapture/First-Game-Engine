package com.morph.engine.physics.components

import com.morph.engine.entities.Component
import com.morph.engine.math.Vector2f

class RigidBody(val mass : Float = 1f, var velocity : Vector2f = Vector2f(), var acceleration : Vector2f = Vector2f()) : Component() {
    private var netForce : Vector2f = Vector2f()
    private var netImpulse : Vector2f = Vector2f()

    /*
	 * Leapfrog Integration
	 * x_new = x_current + v_current*dt + 0.5*a_current*dt^2
	 * v_new = v_current + 0.5(a_current + a_new)*dt
	 */

    /**
     * Updates the rigid body using numerical integration (leapfrog method shown above).
     * @param dt the time step
     */
    fun update(dt: Float): Vector2f {
        val netAcceleration = netForce / mass // F = ma -> a = F / m
        val netVelocity = netImpulse / mass

        val translation = (velocity * dt) + (acceleration * (0.5f * dt * dt))
        velocity += ((acceleration + netAcceleration) * 0.5f * dt) + netVelocity

        acceleration.set(netAcceleration)
        netForce.clear()
        netImpulse.clear()

        return translation
    }

    /**
     * Applies a force to the rigid body.
     * @param force the force applied
     */
    fun applyForce(force: Vector2f) {
        netForce += force
    }

    /**
     * Applies an impulse to the rigid body.
     * @param impulse the impulse applied
     */
    fun applyImpulse(impulse: Vector2f) {
        netImpulse += impulse
    }

    /**
     * Resets the rigid body system.
     */
    fun clear() {
        velocity.clear()
        acceleration.clear()
    }
}
