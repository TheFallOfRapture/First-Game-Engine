package com.morph.engine.entities

interface Component {
    open fun init() {}
    open fun destroy() {}
}
