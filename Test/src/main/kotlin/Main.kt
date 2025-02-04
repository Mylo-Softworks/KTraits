import com.mylosoftworks.ktraits.*

// Define an object
open class Test {
    open val coolValue = "Cool value!"
}

class Test2: Test() { // Subclass of Test
    override val coolValue: String
        get() = "Replaced value!"
}

class Test3: Test() { // Different subclass of Test
    override val coolValue: String
        get() = "Another different value!"
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

// An abstract trait, similar to AlternativeToStringTrait, but we won't implement it for T directly
abstract class ExclusiveTrait<T: Test>: Trait<T>() {
    abstract fun altToStringExclusive(): String
}

// Implement for Test2
class ExclusiveTraitForTest2: ExclusiveTrait<Test2>() {
    override fun altToStringExclusive(): String = "From Test2: ${self.coolValue}"
}
class ExclusiveTraitForTest3: ExclusiveTrait<Test3>() {
    override fun altToStringExclusive(): String = "From Test3: ${self.coolValue}"
}

fun main() {
    // Register the trait
    registerTrait { AlternativeToStringTraitForTest() } // is AlternativeToStringTrait<Test>
    registerTrait { ReversedAltToString() } // T is implicitly `Test`

    // Test the traits
    println(Test().getTrait<Test, AlternativeToStringTrait<Test>>()!!.altToString())
    println(Test2().getTrait<Test2, AlternativeToStringTrait<Test2>>()!!.altToString())

    println(Test2().getTraitNoInherit(ReversedAltToString::class)!!.reversedAltToString()) // Using getTraitNoInherit is fine here, since we're getting the trait in a basic way.
    println(Test2().getTrait(ReversedAltToString::class)!!.reversedAltToString())

    // Register the exclusive traits
    registerTrait { ExclusiveTraitForTest2() }
    registerTrait { ExclusiveTraitForTest3() }

    // Test the exclusive traits
    println(Test2().getTrait(ExclusiveTraitForTest2::class)!!.altToStringExclusive())
    println(Test3().getTrait(ExclusiveTraitForTest3::class)!!.altToStringExclusive())
    println(Test3().getTrait<Test3, ExclusiveTrait<Test3>>()!!.altToStringExclusive())
}