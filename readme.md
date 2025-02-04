[![](https://jitpack.io/v/gitmylo/KTraits.svg)](https://jitpack.io/#gitmylo/KTraits)

# KTraits
Runtime traits for Kotlin Multiplatform (Jvm, Js and WasmJs (untested))

I'm not sure if this library is useful yet. There are quite a few limitations due to the fact `getTrait` is inline and requires reified generics.

## What are traits in this context?
Traits are classes similar to interfaces, but implemented during runtime. They allow adding new functions to existing classes.

These traits are semi-inspired by rust's traits. However, due to limitations they aren't as efficient.

This project is written in pure Kotlin without compiler plugins.

# Usage
> When writing traits, beware of type erasure. Generics of trait targets will be erased during runtime.
> 
> ```kotlin
> // This would be implemented for Target<*> for whatever Target's generic upper bound is.
> class ExampleTrait<Generic: Any, T: Target<Generic>>: Trait<T>()
> ```

## Creating traits
Traits can be created by extending `Trait<T>`. Traits have access to the receiver through a variable named `self`.

There are 2 types of traits that you can create. Template traits and implementation traits.
### Template traits
Template traits are templates which define functions, but don't have an implementation yet. Similar to rust's traits.
```kotlin
// An example trait "Printable" with a single function "print" which returns nothing
abstract class Printable<T: Any>: Trait<T> {
    abstract fun print()
}
```

### Implementation traits
Implementation traits are traits intended to be registered directly, they contain complete code, similar to rust's `implement`.  
Implementation traits can be based on `Trait<T>` or a defined template trait (See above).
```kotlin
// Using template traits
class PrintableString: Printable<String> { // Searchable with Printable since we extend it
    override fun print() = println(self) // "self" is the string
}

// Using trait directly. (It is recommended to use extension methods instead, since they have a nicer syntax)
class PrintableStringDirect: Trait<String> { // Not searchable with Printable since we don't extend it
    fun print() = println(self)
}
```

## Registering traits
Before you're able to use your traits, you have to register them first.  
To register a trait, you can call `registerTrait` and supply it with a code block which returns a new instance of your trait.
```kotlin
registerTrait { TraitClass() }
```

## Using traits
### Getting a trait reference
```kotlin
val printableStringFromString = "Example".getTrait<String, Printable<String>>() // Will give PrintableString cast as Printable<String>
//                      or
val printableStringFromString2 = "Example".getTrait(PrintableString::class) // Won't work when using generics since they're reified.
```
### Using the trait reference
```kotlin
printableStringFromString.print() // Calls the function
```