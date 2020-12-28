package org.jeasy.rules.core

/**
 * Null facts are not accepted by design, a declared fact can be missing though.
 */
class MissingFactAnnotationParameterTest : AbstractTest() {
    @Test
    fun testMissingFact() {
        val rules = Rules()
        rules.register(AnnotatedParametersRule())
        val facts = Facts()
        facts.put("fact1", Any())
        val results = rulesEngine.check(rules, facts)
        for (b in results.values) {
            Assert.assertFalse(b)
        }
    }

    @Rule
    class AnnotatedParametersRule {
        @Condition
        fun `when`(@Fact("fact1") fact1: Any?, @Fact("fact2") fact2: Any?): Boolean {
            return fact1 != null && fact2 == null
        }

        @Action
        fun then(@Fact("fact1") fact1: Any?, @Fact("fact2") fact2: Any?) {
        }
    }
}
