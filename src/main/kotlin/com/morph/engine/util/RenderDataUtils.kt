package com.morph.engine.util

import com.morph.engine.graphics.components.RenderData

/**
 * Created on 7/30/2017.
 */

inline fun <T : RenderData> T.updateAll(body : T.() -> Unit) = with(this) {
    body()
    refreshData()
}

inline fun <T : RenderData> T.updateVertices(body : T.() -> Unit) = with(this) {
    body()
    refreshVertices()
}

inline fun <T : RenderData> T.updateColors(body : T.() -> Unit) = with(this) {
    body()
    refreshColors()
}

inline fun <T : RenderData> T.updateTexCoords(body : T.() -> Unit) = with(this) {
    body()
    refreshTexCoords()
}

inline fun <T : RenderData> T.updateIndices(body : T.() -> Unit) = with(this) {
    body()
    refreshIndices()
}