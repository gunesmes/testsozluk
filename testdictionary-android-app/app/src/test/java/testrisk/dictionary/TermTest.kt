package testrisk.dictionary

import org.junit.Assert.assertEquals
import org.junit.Test

class TermTest {
    @Test
    fun testsTerms() {
        var oneTerm = Term("test term", "test meaning")
        assertEquals(oneTerm.term, "test term")
        assertEquals(oneTerm.meaning, "test meaning")
    }
}