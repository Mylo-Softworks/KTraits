package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

/**
 * Returns true if `class1 == class2` or `class1() is class2`
 */
expect fun isSubClassOf(clazz: KClass<*>, superClass: KClass<*>): Boolean