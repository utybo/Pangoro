package guru.zoroark.pangoro.dsl

@DslMarker
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
annotation class PangoroDsl