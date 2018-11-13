package com.morph.engine.entities

abstract class Component {
    var parent : Entity? = null

    open fun init() {}
    open fun destroy() {}
}
