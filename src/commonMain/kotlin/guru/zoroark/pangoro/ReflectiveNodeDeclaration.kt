package guru.zoroark.pangoro

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

inline fun <reified T : PangoroNode> reflective(): PangoroNodeDeclaration<T> =
    ReflectiveNodeDeclaration(T::class)

class ReflectiveNodeDeclaration<T : PangoroNode>(private val tClass: KClass<T>) :
    PangoroNodeDeclaration<T> {
    override fun make(args: PangoroTypeDescription): T {
        val ctor = findValidConstructor(args.arguments)
        val callArgs =
            ctor.parameters.map { args.arguments[it.name] }.toTypedArray()
        return ctor.call(*callArgs)
    }

    private fun findValidConstructor(arguments: Map<String, Any>): KFunction<T> {
        return tClass.constructors.firstOrNull {
            it.parameters.all { param -> arguments.containsKey(param.name) }
        } ?: throw PangoroException(
            "Could not find a constructor that used keys " + arguments.keys.joinToString(
                ", "
            )
        )
    }

}