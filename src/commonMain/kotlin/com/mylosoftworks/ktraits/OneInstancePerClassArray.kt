package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

/**
 * An array which does not allow an item to be contained twice, and does not allow removing of items.
 *
 * It actually stores a list of `Pair<KClass<*: T>, () -> T>` containing the classes and a parameterless function to instantiate.
 */
class OneInstancePerClassArray<T: Any>: Collection<() -> T> {
    private val elements = mutableListOf<Pair<KClass<*/* : T */>, () -> T>>()
    val elementsCopy get() = elements.toList()

    override val size: Int
        get() = elements.size

    override fun isEmpty(): Boolean = elements.isEmpty()

    override operator fun iterator(): Iterator<() -> T> = elements.map { it.second }.iterator()

    override fun containsAll(elements: Collection<() -> T>): Boolean = this.elements.map { it.second }.containsAll(elements)

    override fun contains(element: () -> T): Boolean = elements.map { it.second }.contains(element)

    operator fun get(index: Int) = elements[index]
//    inline fun <reified A: T> get() = elementsCopy.find { it is A } as A?
    @Suppress("unchecked_cast")
    inline fun <reified A: T> get() = elementsCopy.find {
        isSubClassOf(it.first, A::class)
    }?.second as (() -> A)?

    /**
     * Add one [T] as long as it isn't already in this [OneInstancePerClassArray].
     */
    inline fun <reified A: T> add(noinline item: () -> A) {
//        val found = elementsCopy.find { it is A } != null
        val found = get<A>() != null
        if (!found) @Suppress("DEPRECATION") addUnchecked(item, A::class)
        else error("Type ${A::class} was attempted to be added twice.")
    }

    @Deprecated("Do not use addUnchecked directly, instead use add(item).", ReplaceWith("add(item)"), level = DeprecationLevel.WARNING)
    fun <A: T> addUnchecked(item: () -> T, clazz: KClass<A>) {
        elements.add(clazz to item)
    }
}