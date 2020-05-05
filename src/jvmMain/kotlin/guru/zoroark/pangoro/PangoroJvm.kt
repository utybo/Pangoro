package guru.zoroark.pangoro

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * An implementation of [PangoroNodeDeclaration] that uses reflection to
 * initialize instances of the [PangoroNode] class.
 *
 * The arguments of the node class' constructor are filled in using the
 * arguments of the [PangoroTypeDescription] passed to the [make] function.
 * Keys are mapped to the constructors' argument parameter names, and the first
 * match is used to initialize the class.
 *
 * This class supports classes with multiple constructors: just remember that
 * constructors are chosen exclusively based on parameter names, not typing.
 */
actual class ReflectiveNodeDeclaration<T : PangoroNode> actual constructor(
    private val tClass: KClass<T>
) :
    PangoroNodeDeclaration<T> {
    actual override fun make(args: PangoroTypeDescription): T {
        val ctor = findValidConstructor(args.arguments)
        val callArgs =
            ctor.parameters.map { args.arguments[it.name] }.toTypedArray()
        return ctor.call(*callArgs)
    }

    private fun findValidConstructor(arguments: Map<String, Any>): KFunction<T> {
        return tClass.constructors.firstOrNull {
            it.parameters.all { param -> arguments.containsKey(param.name) }
        } ?: throw PangoroException(
            "Could not find a constructor that used keys " +
                    arguments.keys.joinToString(", ")
        )
    }
}