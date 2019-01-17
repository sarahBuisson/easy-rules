/**
 * The MIT License
 *
 * Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jeasy.rules.core

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Fact
import org.jeasy.rules.annotation.Rule
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import kotlin.test.Test


import kotlin.test.assertFalse

class FactInjectionTest {
/* //TODO
    @Test
    @Throws(Exception::class)
    fun declaredFactsShouldBeCorrectlyInjectedByNameOrType() {
        // Given
        val fact1 = Fact("")
        val fact2 = Fact("")
        val facts = Facts()
        facts.put("fact1", fact1)
        facts.put("fact2", fact2)

        val rule = DummyRule()
        val rules = Rules(rule)

        // When
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        // Then
        assertThat(rule.fact1).isSameAs(fact1)
        assertThat(rule.fact2).isSameAs(fact2)
        assertThat(rule.facts).isSameAs(facts)
    }

    @Test
    @Throws(Exception::class)
    fun rulesShouldBeExecutedWhenFactsAreCorrectlyInjected() {
        // Given
        val facts = Facts()
        facts.put("rain", Fact(true)
        facts.put("age", 18)

        val weatherRule = WeatherRule()
        val ageRule = AgeRule()
        val rules = Rules(weatherRule, ageRule)

        // When
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        // Then
        assertThat(ageRule.isExecuted).isTrue()
        assertThat(weatherRule.isExecuted).isTrue()
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun whenFactTypeDoesNotMatchParameterType_thenShouldThrowRuntimeException() {
        // Given
        val facts = Facts()
        facts.put("age", Fact("foo"))
        val rules = Rules(AgeRule())
        val rulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        // expected exception
    }
    */
    @Test
    @Throws(Exception::class)
    fun whenADeclaredFactIsMissingInEvaluateMethod_thenTheRuleShouldNotBeExecuted() {
        // Given
        val facts = Facts()
        val ageRule = AgeRule()
        val rules = Rules(ageRule)
        val rulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertFalse(ageRule.isExecuted)
    }

    @Test
    @Throws(Exception::class)
    fun whenADeclaredFactIsMissingInExecuteMethod_thenTheRuleShouldNotBeExecuted() {
        // Given
        val facts = Facts()
        val rule = AnotherDummyRule()
        val rules = Rules(rule)
        val rulesEngine = DefaultRulesEngine()

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertFalse(rule.isExecuted)
    }

    @Rule
    internal inner class DummyRule {

        var fact1: Object? = null
            private set
        var fact2: Object? = null
            private set
        var facts: Facts? = null
            private set

        @Condition
        fun `when`(@Fact("fact1") fact1: Object, @Fact("fact2") fact2: Object): Boolean {
            this.fact1 = fact1
            this.fact2 = fact2
            return true
        }

        @Action
        fun then(facts: Facts) {
            this.facts = facts
        }
    }

    @Rule
    internal inner class AnotherDummyRule {

        var isExecuted: Boolean = false
            private set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then(@Fact("foo") fact: Object) {
            isExecuted = true
        }

    }

    @Rule
    internal inner class AgeRule {

        var isExecuted: Boolean = false
            private set

        @Condition
        fun isAdult(@Fact("age") age: Int): Boolean {
            return age >= 18
        }

        @Action
        fun printYourAreAdult() {
            System.out.println("You are an adult")
            isExecuted = true
        }
    }

    @Rule
    internal inner class WeatherRule {

        var isExecuted: Boolean = false
            private set

        @Condition
        fun itRains(@Fact("rain") rain: Boolean): Boolean {
            return rain
        }

        @Action
        fun takeAnUmbrella(facts: Facts) {
            System.out.println("It rains, take an umbrella!")
            isExecuted = true
        }
    }
}
