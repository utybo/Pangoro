package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.expectations.PangoroExpectation
import guru.zoroark.pangoro.expectations.PangoroExpectedOptional

/**
 * Create an optional branch. If the optional branch matches, what it stores
 * will is passed transparently, just like if the `optional { }` was not there.
 * If the optional branch does not match, nothing happens, just like if the
 * entire optional branch was not there.
 *
 * Typical usage may look like:
 *
 *  ```
 *  MyNode {
 *      // Everything here must be present in order to have a match...
 *      expect(...)
 *      expect(...)
 *      optional {
 *          // This branch is optional
 *          expect(...)
 *      }
 *  }
 */
@PangoroDsl
fun ExpectationReceiver.optional(optionalBlock: OptionalBranchBuilder.() -> Unit) {
    this += OptionalBranchBuilder().apply(optionalBlock)
}

/**
 * Builder class for an optional expectation.
 */
class OptionalBranchBuilder :
    ExpectationReceiver, Buildable<PangoroExpectation> {
    private val expectations = mutableListOf<Buildable<PangoroExpectation>>()

    override fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>) {
        expectations += expectationBuilder
    }

    /**
     * Build this expected optional
     */
    override fun build(): PangoroExpectation {
        return PangoroExpectedOptional(expectations.map { it.build() })
    }

}