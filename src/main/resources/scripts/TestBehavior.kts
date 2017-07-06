package scripts

import com.morph.engine.entities.Entity
import com.morph.engine.script.GameBehavior

/**
 * Created on 7/3/2017.
 */
class TestBehavior : GameBehavior() {
    override fun init() {
        val player: Entity? = getEntityByName("player")
        println(player)

        println("TestBehavior has been modified!")
    }

    override fun update() {
        println("Okay")
    }
}

TestBehavior()
