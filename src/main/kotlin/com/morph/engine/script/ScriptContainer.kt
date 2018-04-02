package com.morph.engine.script

import com.morph.engine.core.Game
import com.morph.engine.entities.Component
import com.morph.engine.entities.Entity
import com.morph.engine.util.ScriptUtils
import java.util.*

/**
 * Created on 7/5/2017.
 */
class ScriptContainer(private val game: Game, private val parent: Entity) : Component() {
    private val behaviors: HashMap<String, EntityBehavior> = HashMap()

    fun addBehaviorAsync(filename: String) {
        ScriptUtils.getScriptBehaviorAsync(filename).subscribe({ behavior ->
            val eBehavior = behavior as EntityBehavior
            eBehavior.setGame(game)
            eBehavior.self = parent
            behaviors[filename] = eBehavior
            eBehavior.init()
            eBehavior.start()
        })
    }

    @Deprecated("Adding scripts in this way blocks the current thread.", ReplaceWith("addBehaviorAsync"), DeprecationLevel.WARNING)
    private fun addBehaviorStrict(filename: String) {
        val behavior = ScriptUtils.getScriptBehavior<EntityBehavior>(filename)
        behavior?.let {
            behavior.setGame(game)
            behavior.self = parent
            behaviors[filename] = behavior
            behavior.init()
            behavior.start()
        }

        ScriptUtils.register(filename, parent)
    }

    fun replaceBehavior(filename: String, newBehavior: EntityBehavior) {
        newBehavior.setGame(game)
        newBehavior.self = parent
        behaviors.replace(filename, newBehavior)
        newBehavior.start()
    }

    fun removeBehavior(filename: String) {
        val behavior = behaviors[filename]
        behaviors.remove(filename)
        behavior?.destroy()
    }

    fun getBehaviors(): List<EntityBehavior> {
        return ArrayList<EntityBehavior>(behaviors.values)
    }
}
