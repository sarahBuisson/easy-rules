/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.core

import org.assertj.core.api.Assertions
import org.jeasy.rules.api.*
import org.jeasy.rules.api.Rule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class DefaultRulesEngineTest : AbstractTest() {

    @Mock
    private lateinit var ruleListener: RuleListener

    @Mock
    private lateinit var rulesEngineListener: RulesEngineListener

    @Before
    @Throws(Exception::class)
    override fun setup() {
        super.setup()
        Mockito.`when`(rule1.name).thenReturn("r")
        Mockito.`when`(rule1.priority).thenReturn(1)
    }

    @Test
    @Throws(Exception::class)
    fun whenConditionIsTrue_thenActionShouldBeExecuted() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Mockito.verify(rule1).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun whenConditionIsFalse_thenActionShouldNotBeExecuted() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(false)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Mockito.verify(rule1, Mockito.never()).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeTriggeredInTheirNaturalOrder() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.compareTo(rule1)).thenReturn(1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        val inOrder = Mockito.inOrder(rule1, rule2)
        inOrder.verify(rule1).execute(facts)
        inOrder.verify(rule2).execute(facts)
    }

    @Test
    fun rulesMustBeCheckedInTheirNaturalOrder() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.compareTo(rule1)).thenReturn(1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.check(rules, facts)

        // Then
        val inOrder = Mockito.inOrder(rule1, rule2)
        inOrder.verify(rule1).evaluate(facts)
        inOrder.verify(rule2).evaluate(facts)
    }

    @Test
    fun testCheckRules() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        rules.register(rule1)

        // When
        val result = rulesEngine.check(rules, facts)

        // Then
        Assertions.assertThat(result).hasSize(1)
        for (r in rules) {
            Assertions.assertThat(result[r]).isTrue
        }
    }

    @Test
    fun listenerShouldBeInvokedBeforeCheckingRules() {
        // Given
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        Mockito.`when`(ruleListener.beforeEvaluate(rule1, facts)).thenReturn(true)
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)
        rules.register(rule1)

        // When
        rulesEngine.check(rules, facts)

        // Then
        Mockito.verify(ruleListener).beforeEvaluate(rule1, facts)
    }

    @Test
    fun getParametersShouldReturnACopyOfTheParameters() {
        // Given
        val parameters = RulesEngineParameters()
            .skipOnFirstAppliedRule(true)
            .skipOnFirstFailedRule(true)
            .skipOnFirstNonTriggeredRule(true)
            .priorityThreshold(42)
        val rulesEngine = DefaultRulesEngine(parameters)

        // When
        val engineParameters = rulesEngine.getParameters()

        // Then
        Assertions.assertThat(engineParameters).isNotSameAs(parameters)
        Assertions.assertThat(engineParameters).usingRecursiveComparison().isEqualTo(parameters)
    }

    @Test
    fun testGetRuleListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.getRuleListeners()

        // Then
        Assertions.assertThat(ruleListeners).contains(ruleListener)
    }

    @Test
    fun getRuleListenersShouldReturnAnUnmodifiableList() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.getRuleListeners()

        // Then
        Assertions.assertThatThrownBy { ruleListeners.clear() }.isInstanceOf(
            UnsupportedOperationException::class.java
        )
    }

    @Test
    fun testGetRulesEngineListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRulesEngineListener(rulesEngineListener)

        // When
        val rulesEngineListeners = rulesEngine.getRulesEngineListeners()

        // Then
        Assertions.assertThat(rulesEngineListeners).contains(rulesEngineListener)
    }

    @Test
    fun getRulesEngineListenersShouldReturnAnUnmodifiableList() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRulesEngineListener(rulesEngineListener)

        // When
        val rulesEngineListeners = rulesEngine.getRulesEngineListeners()

        // Then
        Assertions.assertThatThrownBy { rulesEngineListeners.clear() }.isInstanceOf(
            UnsupportedOperationException::class.java
        )
    }

    @After
    fun clearRules() {
        rules.clear()
    }


    class DummyRule: BasicRule() {

        override fun evaluate(facts: Facts): Boolean {
            return true
        }


        override fun execute(facts: Facts) {
            // no op
        }
    }
}
