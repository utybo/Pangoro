package guru.zoroark.pangoro.dsl

/**
 * Marker for the Pangoro DSL.
 */
@DslMarker
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
annotation class PangoroDsl