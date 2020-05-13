package guru.zoroark.pangoro

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

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
        // TODO Better error messages on reflection errors
        val callArgs =
            ctor.parameters
                .filter { args.arguments.containsKey(it.name) }
                .associateWith { args.arguments[it.name]!! }
        return ctor.callBy(callArgs)
    }

    private fun findValidConstructor(arguments: Map<String, Any>): KFunction<T> {
        return tClass.constructors.filter {
            // All parameters have a value in the map (except for optional parameters)
            // with a compatible type
            it.parameters.all { param ->
                val matchingArg =
                    arguments[param.name] ?: return@all param.isOptional
                param.isCompatibleWith(matchingArg)
            }
        }.maxBy {
            // Pick the constructor with the most non-optional parameters
            it.parameters.filter { param -> !param.isOptional }.count()
        } ?: throw PangoroException(
            "Could not find a constructor that uses keys " +
                    arguments.entries.joinToString(", ") { (k, v) ->
                        "$k{${v::class.qualifiedName}}"
                    }
        )
    }
}

private fun KParameter.isCompatibleWith(matchingArg: Any): Boolean =
    matchingArg::class.starProjectedType.isSubtypeOf(type)