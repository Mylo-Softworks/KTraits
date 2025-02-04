package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

actual fun isSubClassOf(clazz: KClass<*>, superClass: KClass<*>): Boolean {
    if (clazz == superClass) return true

    val c1 = clazz.js
    val c1Proto = c1.asDynamic().prototype
    val c2 = superClass.js

    return js("c1Proto instanceof c2") as Boolean
}

fun equals(v1: String, v2: String) = v1.hashCode() == v2.hashCode()