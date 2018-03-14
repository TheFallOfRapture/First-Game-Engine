package com.morph.engine.entities

abstract class Component {
    open fun init() {}
    open fun destroy() {}
}
