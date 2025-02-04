package com.mylosoftworks.ktraits

import kotlin.reflect.KClass

/**
 * The registry holding all currently known traits. Do not modify directly.
 *
 * Functions for defining and using traits: [registerTrait], [getTraitNoInherit], [getTrait], [hasTraitNoInherit], [hasTrait].
 *
 * Functions for accessing the registry: [getTraitRegistriesForInstance], [getTraitRegistryForClass]
 */
val traitRegistry = hashMapOf<KClass<*>, OneInstancePerClassArray<Trait<*>>>()

/**
 * Gets a `List<`[OneInstancePerClassArray]`<Trait<T>>>` containing the [OneInstancePerClassArray]`<Trait<T>>` for every subclass of this instance.
 *
 * See [getTraitRegistryForClass] for more information about the returned [OneInstancePerClassArray]`<Trait<T>>`.
 */
@Suppress("unchecked_cast")
inline fun <reified T: Any> getTraitRegistriesForInstance(instance: T): List<OneInstancePerClassArray<Trait<T>>> = traitRegistry.filterKeys {clazz ->
    clazz.isInstance(instance)
}.values.toList() as List<OneInstancePerClassArray<Trait<T>>>

/**
 * Gets a [traitRegistry] entry for the target class, and creates it if needed.
 *
 * See [getTraitRegistriesForInstance] for including subclasses.
 *
 * @return A [OneInstancePerClassArray] with all the functions to initialize the [Trait].
 */
@Suppress("unchecked_cast")
inline fun <reified T: Any> getTraitRegistryForClass(): OneInstancePerClassArray<Trait<T>> = traitRegistry.getOrPut(T::class) {
    OneInstancePerClassArray<Trait<T>>() as OneInstancePerClassArray<Trait<*>>
} as OneInstancePerClassArray<Trait<T>>

/**
 * Register a [Trait] for this class.
 *
 * Registers to [traitRegistry].
 *
 * Example: `registerTrait { ExampleTrait() }`
 */
inline fun <reified T: Any, reified Tr: Trait<T>> registerTrait(noinline trait: () -> Tr) = getTraitRegistryForClass<T>().add(trait)

/**
 * Get a trait linked to this class or null if not found. (excluding inherited traits)
 *
 * Searches [traitRegistry]
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.getTraitNoInherit(): Tr? = getTraitRegistryForClass<T>().get<Tr>()?.invoke()?.also { it.self = this }
/**
 * @see getTraitNoInherit
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.getTraitNoInherit(trait: KClass<Tr>): Tr? = getTraitRegistryForClass<T>().get<Tr>()?.invoke()?.also { it.self = this }

/**
 * Get a trait linked to this class or null if not found. (including inherited traits)
 *
 * Searches [traitRegistry]
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.getTrait(): Tr? = getTraitRegistriesForInstance(this).firstNotNullOfOrNull { it.get<Tr>() }?.invoke()?.also { it.self = this }
/**
 * @see getTrait
 */
inline fun <reified R: T, reified T: Any, reified Tr: Trait<R>> R.getTrait(trait: KClass<Tr>): Tr? = getTraitRegistriesForInstance(this).firstNotNullOfOrNull { it.get<Tr>() }?.invoke()?.also { it.self = this }

/**
 * Check if a class has a trait (excluding inherited traits)
 *
 * Searches [traitRegistry]
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.hasTraitNoInherit(): Boolean = getTraitRegistryForClass<T>().get<Tr>() != null
/**
 * @see hasTraitNoInherit
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.hasTraitNoInherit(trait: KClass<Tr>): Boolean = getTraitRegistryForClass<T>().get<Tr>() != null

/**
 * Check if a class has a trait (including inherited traits)
 *
 * Searches [traitRegistry]
 */
inline fun <reified T: Any, reified Tr: Trait<T>> T.hasTrait(): Boolean = getTraitRegistriesForInstance(this).find { it.get<Tr>() != null } != null
/**
 * @see hasTrait
 */
inline fun <reified R: T, reified T: Any, reified Tr: Trait<R>> R.hasTrait(trait: KClass<Tr>): Boolean = getTraitRegistriesForInstance(this).find { it.get<Tr>() != null } != null
