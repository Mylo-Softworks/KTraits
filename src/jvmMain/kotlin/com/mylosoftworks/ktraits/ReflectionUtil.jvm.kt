package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

actual fun isSubClassOf(clazz: KClass<*>, superClass: KClass<*>): Boolean = superClass.java.isAssignableFrom(clazz.java)