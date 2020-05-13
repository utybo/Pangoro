package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.expectations.PangoroExpectation

/**
 * Simple builder for an expectation. The only real use of this class is to add
 * the "storeIn" thing on-the-fly-ish, after the call to "expect".
 */
class PangoroExpectationBuilder(private val builderFunc: (String?) -> PangoroExpectation) : Buildable<PangoroExpectation> {
    private var storeIn: String? = null

    /**
     * Signals that the expectation should store its result using the given
     * argument name.
     */
    infix fun storeIn(argName: String) {
        storeIn = argName
    }

    /**
     * Short notation for [storeIn].
     */
    operator fun rem(argName: String) {
        storeIn = argName
    }

    /**
     * Builds this expectation
     */
    override fun build(): PangoroExpectation {
        return builderFunc(storeIn)
    }
}
