import com.morph.engine.entities.Entity
import com.morph.engine.script.GameBehavior

/**
 * Created on 7/3/2017.
 */
class TestBehavior2 : GameBehavior() {
    override fun init() {
        val player: Entity? = getEntityByName("player")
        println("$player | We've got support for multiple scripts!")
    }
}

TestBehavior2()