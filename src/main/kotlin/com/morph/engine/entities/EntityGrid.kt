package com.morph.engine.entities

/**
 * Created by Fernando on 2/11/2017.
 */
open class EntityGrid(val width: Int, val height: Int) {
    private val entities: Array<Entity?> = arrayOfNulls(width * height)

    fun asList(): List<Entity> {
        return entities.filterNotNull()
    }

    open operator fun set(tileX: Int, tileY: Int, e: Entity?): Boolean {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height)
            return false

        entities[tileX + tileY * width] = e
        return true
    }

    open fun moveEntity(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        if (startX < 0 || startX >= width || startY < 0 || startY >= height
                || endX < 0 || endX >= width || endY < 0 || endY >= height)
            return false

        entities[endX + endY * width] = entities[startX + startY * width]
        entities[startX + startY * width] = null

        return true
    }

    fun moveEntity(e: Entity, x: Int, y: Int): Boolean {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false

        val location = findMatch(e)

        return if (location[0] == -1) false else moveEntity(location[0], location[1], x, y)

    }

    fun translateEntity(e: Entity, dx: Int, dy: Int): Boolean {
        val location = findMatch(e)

        if (location[0] == -1)
            return false

        val x = location[0] + dx
        val y = location[1] + dy

        return if (x < 0 || x >= width || y < 0 || y >= height) false else moveEntity(location[0], location[1], x, y)

    }

    fun translateEntity(tileX: Int, tileY: Int, dx: Int, dy: Int): Boolean {
        val x = tileX + dx
        val y = tileY + dy

        return if (x < 0 || x >= width || y < 0 || y >= height) false else moveEntity(tileX, tileY, x, y)

    }

    private fun findMatch(e: Entity): IntArray {
        for (y in 0 until height)
            for (x in 0 until width)
                if (e == entities[x + y * width])
                    return intArrayOf(x, y)

        return intArrayOf(-1, -1)
    }

    operator fun get(tileX: Int, tileY: Int): Entity? =
            if (tileX >= width || tileY >= height) null else entities[tileX + tileY * width]

    open fun removeEntity(tileX: Int, tileY: Int): Boolean {
        if (tileX + tileY * width >= entities.size || entities[tileX + tileY * width] == null)
            return false

        entities[tileX + tileY * width] = null

        return true
    }
}
