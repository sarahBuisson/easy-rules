package org.jeasy.rules.core

@RunWith(MockitoJUnitRunner::class)
class FactInjectionTest {
    @Test
    fun declaredFactsShouldBeCorrectlyInjectedByNameOrType() {
        // Given
        val fact1 = Any()
        val fact2 = Any()
        val facts = Facts()
        facts.put("fact1", fact1)
        facts.put("fact2", fact2)
        val rule = DummyRule()
        val rules = Rules(rule)

        // When
        val rulesEngine: RulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(rule.getFact1()).isSameAs(fact1)
        Assertions.assertThat(rule.getFact2()).isSameAs(fact2)
        Assertions.assertThat(rule.getFacts()).isSameAs(facts)
    }

    @Test
    fun rulesShouldBeExecutedWhenFactsAreCorrectlyInjected() {
        // Given
        val facts = Facts()
        facts.put("rain", true)
        facts.put("age", 18)
        val weatherRule = WeatherRule()
        val ageRule = AgeRule()
        val rules = Rules(weatherRule, ageRule)

        // When
        val rulesEngine: RulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(ageRule.isExecuted()).isTrue
        Assertions.assertThat(weatherRule.isExecuted()).isTrue
    }

    @Test
    fun whenFactTypeDoesNotMatchParameterType_thenTheRuleShouldNotBeExecuted() {
        // Given
        val facts = Facts()
        facts.put("age", "foo")
        val ageRule = AgeRule()
        val rules = Rules(ageRule)
        val rulesEngine: RulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(ageRule.isExecuted()).isFalse
    }

    @Test
    fun whenADeclaredFactIsMissingInEvaluateMethod_thenTheRuleShouldNotBeExecuted() {
        // Given
        val facts = Facts()
        val ageRule = AgeRule()
        val rules = Rules(ageRule)
        val rulesEngine: RulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(ageRule.isExecuted()).isFalse
    }

    @Test
    fun whenADeclaredFactIsMissingInExecuteMethod_thenTheRuleShouldNotBeExecuted() {
        // Given
        val facts = Facts()
        val rule = AnotherDummyRule()
        val rules = Rules(rule)
        val rulesEngine: RulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(rule.isExecuted()).isFalse
    }

    @Rule
    internal class DummyRule {
        private var fact1: Any? = null
        private var fact2: Any? = null
        private var facts: Facts? = null
        @Condition
        fun `when`(@Fact("fact1") fact1: Any?, @Fact("fact2") fact2: Any?): Boolean {
            this.fact1 = fact1
            this.fact2 = fact2
            return true
        }

        @Action
        fun then(facts: Facts?) {
            this.facts = facts
        }

        fun getFact1(): Any? {
            return fact1
        }

        fun getFact2(): Any? {
            return fact2
        }

        fun getFacts(): Facts? {
            return facts
        }
    }

    @Rule
    internal class AnotherDummyRule {
        private var isExecuted = false
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then(@Fact("foo") fact: Any?) {
            isExecuted = true
        }

        fun isExecuted(): Boolean {
            return isExecuted
        }
    }

    @Rule
    internal class AgeRule {
        private var isExecuted = false
        @Condition
        fun isAdult(@Fact("age") age: Int): Boolean {
            return age >= 18
        }

        @Action
        fun printYourAreAdult() {
            println("You are an adult")
            isExecuted = true
        }

        fun isExecuted(): Boolean {
            return isExecuted
        }
    }

    @Rule
    internal class WeatherRule {
        private var isExecuted = false
        @Condition
        fun itRains(@Fact("rain") rain: Boolean): Boolean {
            return rain
        }

        @Action
        fun takeAnUmbrella(facts: Facts?) {
            println("It rains, take an umbrella!")
            isExecuted = true
        }

        fun isExecuted(): Boolean {
            return isExecuted
        }
    }
}
