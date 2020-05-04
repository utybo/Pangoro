package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.pangoro.PangoroExpectedLixyToken
import guru.zoroark.pangoro.PangoroExpectedNode
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration

infix fun <T : PangoroNode> PangoroDescriptorBuilder<T>.expect(tokenType: LixyTokenType): PangoroExpectationBuilder =
    PangoroExpectationBuilder {
        PangoroExpectedLixyToken(tokenType, it)
    }.also {
        this += it
    }

infix fun <T : PangoroNode> PangoroDescriptorBuilder<T>.expect(node: PangoroNodeDeclaration<*>): PangoroExpectationBuilder =
    PangoroExpectationBuilder {
        PangoroExpectedNode(node, it)
    }.also {
        this += it
    }