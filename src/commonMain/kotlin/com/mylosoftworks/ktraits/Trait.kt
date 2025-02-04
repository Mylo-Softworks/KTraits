package com.mylosoftworks.ktraits

/**
 * Define a trait which can be used with [Target].
 *
 * See [registerTrait] for registering traits.
 */
abstract class Trait<Target: Any> {
    /**
     * [self] is a reference to the receiving class, instead of `this`.
     */
    lateinit var self: Target
}