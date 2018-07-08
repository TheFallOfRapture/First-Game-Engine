package com.morph.engine.core

import com.morph.engine.entities.Entity
import com.morph.engine.entities.EntityGrid
import com.morph.engine.entities.given
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D

/**
 * Created by Fernando on 1/19/2017.
 */
abstract class TileWorld(override val game: Game, width: Int, height: Int, val tileSize: Float) : EntityGrid(width, height), IWorld {
    private var xOffset: Float = 0.toFloat()
    private var yOffset: Float = 0.toFloat()

    override val entities: List<Entity>
        get() = asList()

    init {
        this.xOffset = 0f
        this.yOffset = 0f
    }

    // TODO: Strict implementation = return false
    override fun addEntity(e: Entity): Boolean {
        var ret : Boolean = false
        given<Transform2D>(e) { t2D ->
            val tilePos = (t2D.position / tileSize).map { x -> Math.floor(x.toDouble()).toFloat() }
            ret = set(tilePos.x.toInt(), tilePos.y.toInt(), e)
        }
        return ret
    }

    fun setXOffset(xOffset: Float) {
        this.xOffset = xOffset
    }

    fun setYOffset(yOffset: Float) {
        this.yOffset = yOffset
    }

    override fun set(tileX: Int, tileY: Int, e: Entity?): Boolean {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height)
            return false

        game.renderingEngine.register(e)

        val tmp = this[tileX, tileY]
        if (tmp != null)
            game.renderingEngine.unregister(tmp)

        this[tileX, tileY] = e

        e?.also {
            given<Transform2D>(e) {
                it.position = Vector2f(xOffset + (tileX + 0.5f) * tileSize, yOffset + height * tileSize - (tileY + 0.5f) * tileSize)
                it.scale = Vector2f(tileSize, tileSize)
            }
        }

        return true
    }

    fun addEntityGrid(grid: EntityGrid, startX: Int, startY: Int): Boolean {
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                val e = grid[x, y]
                if (e != null) {
                    val success = set(startX + x, startY + y, e)
                    if (!success) return false
                }
            }
        }

        return true
    }

    fun removeEntityGrid(grid: EntityGrid): Boolean {
        for (e in asList())
            if (grid.asList().contains(e))
                removeEntity(e)

        return true
    }

    fun lazyMoveEntityGrid(grid: EntityGrid, x: Int, y: Int): Boolean {
        removeEntityGrid(grid)
        return addEntityGrid(grid, x, y)
    }

    override fun moveEntity(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        if (startX < 0 || startX >= width || startY < 0 || startY >= height
                || endX < 0 || endX >= width || endY < 0 || endY >= height)
            return false

        if (this[endX, endY] != null)
            game.renderingEngine.unregister(this[endX, endY])

        this[endX, endY] = this[startX, startY]
        this[startX, startY] = null

        this[endX, endY]?.getComponent(Transform2D::class.java)!!.position = Vector2f(xOffset + (endX + 0.5f) * tileSize, yOffset + height * tileSize - (endY + 0.5f) * tileSize)

        return true
    }

    private fun findMatch(e: Entity?): Pair<Int, Int> {
        for (y in 0 until height)
            for (x in 0 until width)
                if (this[x, y] != null && e != null && e == this[x, y])
                    return Pair(x, y)

        return Pair(-1, -1)
    }

    override fun removeEntity(tileX: Int, tileY: Int): Boolean {
        if (tileX + tileY * width >= asList().size || this[tileX, tileY] == null)
            return false

        val temp = this[tileX, tileY]
        game.renderingEngine.unregister(temp)

        this[tileX, tileY] = null

        return true
    }

    override fun removeEntity(e: Entity): Boolean {
        val (x, y) = findMatch(e)
        return if (x == -1) false else removeEntity(x, y)

    }

    fun isEmpty(x: Int, y: Int): Boolean =
            if (x < 0 || x >= width || y < 0 || y >= height) false else this[x, y] == null

    fun areEmpty(vararg positions: Pair<Int, Int>): Boolean = positions.all { (x, y) -> isEmpty(x, y) }
}
