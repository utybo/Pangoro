package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.PangoroExpectation

class PangoroExpectationBuilder(private val builderFunc: (String?) -> PangoroExpectation) : Buildable<PangoroExpectation> {
    private var storeIn: String? = null

    infix fun storeIn(argName: String) {
        storeIn = argName
    }

    override fun build(): PangoroExpectation {
        return builderFunc(storeIn)
    }
}
