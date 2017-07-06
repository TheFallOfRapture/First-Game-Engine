package scripts

import com.morph.engine.entities.Entity
import com.morph.engine.script.EntityBehavior

class EScript : EntityBehavior() {
    override fun start() {
        println("Hello again.")
    }
}

EScript()
