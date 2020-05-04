package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.expectations.PangoroEitherBranch
import guru.zoroark.pangoro.expectations.PangoroExpectation
import guru.zoroark.pangoro.expectations.PangoroExpectedEither

/**
 * Build an "either" construct, with different branches the parsing process can
 * take.
 *
 * The branches are checked in the order they are declared.
 *
 * Typical use may look like:
 *
 *  ```
 *  MyNode {
 *      expect(...)
 *      either {
 *          // Expectations for branch one...
 *          expect(tokenX1)
 *          expect(OtherNode)
 *      } or {
 *          // Expectations for branch two...
 *          expect(SomethingElse)
 *          expect(tokenY2) storeIn "hello"
 *      } or {
 *          // Expectations for branch three
 *          expect(EvenMoreDifferent)
 *      }
 *      expect(...)
 *  }
 *  ```
 *
 * Anything stored within a branch will be available for the constructed node,
 * provided that the branch was executed.
 *
 * Note: The lambda receives a builder for a single branch, but this function
 * returns the builder for the entire either construct, meaning that:
 *
 * - You cannot add branches from a branch
 *
 * - The recommended way to add branches is to call [or], like in the example
 * above.
 */
@PangoroDsl
fun ExpectationReceiver.either(branchBuilder: PangoroEitherBranchBuilder.() -> Unit): PangoroEitherBuilder {
    val builder = PangoroEitherBuilder()
    builder.addBranch(branchBuilder)
    this += builder
    return builder
}

/**
 * The builder for a single branch. This is the receiver type of every [either]
 * branch.
 */
@PangoroDsl
class PangoroEitherBranchBuilder : ExpectationReceiver,
    Buildable<PangoroEitherBranch> {
    private val expectations = mutableListOf<Buildable<PangoroExpectation>>()

    override fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>) {
        expectations += expectationBuilder
    }

    /**
     * Builds this branch
     */
    override fun build(): PangoroEitherBranch {
        return PangoroEitherBranch(expectations.map { it.build() })
    }
}

/**
 * The builder for the entire either construct. This is different from a single
 * branch.
 */
@PangoroDsl
class PangoroEitherBuilder : Buildable<PangoroExpectedEither> {
    private val branches = mutableListOf<Buildable<PangoroEitherBranch>>()

    /**
     * Build this either expectation
     */
    override fun build(): PangoroExpectedEither {
        return PangoroExpectedEither(branches.map { it.build() })
    }

    /**
     * Add a branch builder to this either builder
     */
    fun addBranch(branch: Buildable<PangoroEitherBranch>) {
        branches += branch
    }
}

/**
 * Add a branch to this builder. The branch is first initialized through the
 * [branchInit] argument.
 */
inline fun PangoroEitherBuilder.addBranch(branchInit: PangoroEitherBranchBuilder.() -> Unit) {
    addBranch(PangoroEitherBranchBuilder().apply(branchInit))
}

/**
 * Adds a branch to the `either` construct. `or` can be used multiple times to get more than two branches, like so:
 *
 *  ```
 *  either {
 *      // ...
 *  } or {
 *      // ...
 *  } or {
 *      // ...
 *  }
 *  ```
 */
@PangoroDsl
inline infix fun PangoroEitherBuilder.or(branchInit: PangoroEitherBranchBuilder.() -> Unit): PangoroEitherBuilder {
    addBranch(branchInit)
    return this
}