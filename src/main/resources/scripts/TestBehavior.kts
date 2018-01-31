import com.morph.engine.entities.Entity
import com.morph.engine.script.GameBehavior

/**
 * Created on 7/3/2017.
 */
class TestBehavior : GameBehavior() {
    override fun init() {
        val player: Entity? = getEntityByName("player")
        println(player)

        println("TestBehavior has been modified for a third time!")
    }

    override fun start() {
        println("Test again for blocking 2")
    }
}

TestBehavior()
