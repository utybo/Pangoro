package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.pangoro.PangoroExpectedLixyToken
import guru.zoroark.pangoro.PangoroExpectedNode
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration

/**
 * Adds an expectation to this node descriptor based on a token type.
 *
 * A token of the given token type is expected at this point.
 */
infix fun <T : PangoroNode> PangoroDescriptorBuilder<T>.expect(tokenType: LixyTokenType): PangoroExpectationBuilder =
    PangoroExpectationBuilder {
        PangoroExpectedLixyToken(tokenType, it)
    }.also {
        this += it
    }

/**
 * Adds an expectation to this node descriptor based on a node
 *
 * A chain of tokens that corresponds to the given node is expected at this
 * point
 */
infix fun <T : PangoroNode> PangoroDescriptorBuilder<T>.expect(node: PangoroNodeDeclaration<*>): PangoroExpectationBuilder =
    PangoroExpectationBuilder {
        PangoroExpectedNode(node, it)
    }.also {
        this += it
    }