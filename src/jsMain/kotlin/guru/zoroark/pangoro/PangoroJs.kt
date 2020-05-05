package guru.zoroark.pangoro

import kotlin.reflect.KClass

/**
 * This class is not supported on JavaScript.
 */
actual class ReflectiveNodeDeclaration<T : PangoroNode> actual constructor(
    tClass: KClass<T>
) : PangoroNodeDeclaration<T> {
    init {
        error("reflective() and ReflectiveNodeDeclaration are not supported on JS")
    }
    actual override fun make(args: PangoroTypeDescription): T {
        error("reflective() and ReflectiveNodeDeclaration are not supported on JS")
    }
}