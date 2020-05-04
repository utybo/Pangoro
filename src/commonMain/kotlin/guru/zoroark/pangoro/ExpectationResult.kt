package guru.zoroark.pangoro

/**
 * Class for representing the results of an expectation
 */
sealed class ExpectationResult {
    /**
     * This expectation matched successfully. It stored values in [stored]
     * and the next index that needs to be checked would be [nextIndex].
     */
    class Success(val stored: Map<String, Any>, val nextIndex: Int) :
        ExpectationResult()

    object DidNotMatch : ExpectationResult()
}