package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

actual fun isSubClassOf(clazz: KClass<*>, superClass: KClass<*>): Boolean = isJsSubClassOf(clazz.toJsReference(), superClass.toJsReference())

fun isJsSubClassOf(clazz: JsReference<KClass<*>>, superClass: JsReference<KClass<*>>): Boolean = js("clazz.prototype instanceof superClass") // Untested
