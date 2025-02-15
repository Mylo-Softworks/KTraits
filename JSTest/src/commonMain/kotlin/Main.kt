import com.mylosoftworks.ktraits.*

// Define an object
open class Test {
    open val coolValue = "Cool value!"
}

class Test2: Test() { // Subclass of Test
    override val coolValue: String
        get() = "Replaced value!"
}

// Define a trait, for universal traits
abstract class AlternativeToStringTrait<T: Any>: Trait<T>() {
    abstract fun altToString(): String
}

// Define a special implementation for Test
class AlternativeToStringTraitForTest: AlternativeToStringTrait<Test>() {
    override fun altToString(): String = self.coolValue
}

// Direct implementation, for adding features as traits. (In many cases, extension methods will be a fine alternative)
class ReversedAltToString: Trait<Test>() {
    fun reversedAltToString() = self.coolValue.reversed()
}

fun main() {
    // Register the trait
    registerTrait(::AlternativeToStringTraitForTest) // is AlternativeToStringTrait<Test>
    registerTrait(::ReversedAltToString) // T is implicitly `Test`

    println(Test().getTrait<Test, AlternativeToStringTrait<Test>>()!!.altToString())
    println(Test2().getTrait<Test2, AlternativeToStringTrait<Test2>>()!!.altToString())

    println(Test2().getTraitNoInherit(ReversedAltToString::class)!!.reversedAltToString()) // Using getTraitNoInherit is fine here, since we're getting the trait in a basic way.
    println(Test2().getTrait(ReversedAltToString::class)!!.reversedAltToString())

//    println(Test().hasTrait<Test, AlternativeToStringTrait<Test>>())
//    println(Test2().hasTrait<Test2, AlternativeToStringTrait<Test2>>())
}