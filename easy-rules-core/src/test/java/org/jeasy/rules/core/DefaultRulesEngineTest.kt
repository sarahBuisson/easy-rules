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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.assertj.core.api.Assertions
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.RuleListener
import org.jeasy.rules.api.RulesEngineListener
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mock

class DefaultRulesEngineTest : AbstractTest() {

    @Mock
    private lateinit var ruleListener: RuleListener

    @Mock
    private lateinit var rulesEngineListener: RulesEngineListener

    private lateinit var annotatedRule: AnnotatedRule

    @Before
    @Throws(Exception::class)
    override  fun setup() {
        super.setup()
        `when`(rule1.name).thenReturn("r")
        `when`(rule1.priority).thenReturn(1)
        annotatedRule = AnnotatedRule()
    }

    @Test
    @Throws(Exception::class)
    fun whenConditionIsTrue_thenActionShouldBeExecuted() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(rule1).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun whenConditionIsFalse_thenActionShouldNotBeExecuted() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(false)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(rule1, never()).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeTriggeredInTheirNaturalOrder() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        `when`(rule2.evaluate(facts)).thenReturn(true)
        `when`(rule2.compareTo(rule1)).thenReturn(1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        val inOrder = inOrder(rule1, rule2)
        inOrder.verify(rule1).execute(facts)
        inOrder.verify(rule2).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeCheckedInTheirNaturalOrder() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        `when`(rule2.evaluate(facts)).thenReturn(true)
        `when`(rule2.compareTo(rule1)).thenReturn(1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.check(rules, facts)

        // Then
        val inOrder = inOrder(rule1, rule2)
        inOrder.verify(rule1).evaluate(facts)
        inOrder.verify(rule2).evaluate(facts)
    }

    @Test
    fun actionsMustBeExecutedInTheDefinedOrder() {
        // Given
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertEquals("012", annotatedRule!!.actionSequence)
    }

    @Test
    @Throws(Exception::class)
    fun annotatedRulesAndNonAnnotatedRulesShouldBeUsableTogether() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(rule1).execute(facts)
        assertThat(annotatedRule!!.isExecuted).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun whenRuleNameIsNotSpecified_thenItShouldBeEqualToClassNameByDefault() {
        val rule = RuleProxy.asRule(DummyRule())
        assertThat(rule.name).isEqualTo("DummyRule")
    }

    @Test
    @Throws(Exception::class)
    fun whenRuleDescriptionIsNotSpecified_thenItShouldBeEqualToConditionNameFollowedByActionsNames() {
        val rule = RuleProxy.asRule(DummyRule())
        assertThat(rule.description).isEqualTo("when condition then action1,action2")
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRules() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        val result = rulesEngine.check(rules, facts)

        // Then
        assertThat(result).hasSize(2)
        for (r in rules) {
            assertThat(result.get(r)).isTrue()
        }
    }

    @Test
    @Throws(Exception::class)
    fun listenerShouldBeInvokedBeforeCheckingRules() {
        // Given
        `when`(rule1.evaluate(facts)).thenReturn(true)
        `when`(ruleListener!!.beforeEvaluate(rule1, facts)).thenReturn(true)
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)
        rules.register(rule1)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verify(ruleListener).beforeEvaluate(rule1, facts)
    }

    @Test
    fun nullFactsShouldNotCrashTheEngine() {
        // Given
        facts.put("foo", null)

        // When
        try {
            rulesEngine.fire(rules, facts)
        } catch (e: Exception) {
            Assertions.fail("Unable to fire rules on known facts", e)
        }

        // Then
        // Should not throw exception
    }

    @Test
    @Throws(Exception::class)
    fun testGetRuleListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.ruleListeners

        // Then
        assertThat(ruleListeners).contains(ruleListener)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRulesEngineListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRulesEngineListener(rulesEngineListener)

        // When
        val rulesEngineListeners = rulesEngine.rulesEngineListeners

        // Then
        assertThat(rulesEngineListeners).contains(rulesEngineListener)
    }

    @After
    fun clearRules() {
        rules.clear()
    }

    @org.jeasy.rules.annotation.Rule(name = "myRule", description = "my rule description")
    inner class AnnotatedRule {

        var isExecuted: Boolean = false
            private set

        var actionSequence = ""
            private set

        val priority: Int
            @Priority
            get() = 0

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        @Throws(Exception::class)
        fun then0() {
            actionSequence += "0"
        }

        @Action(order = 1)
        @Throws(Exception::class)
        fun then1() {
            actionSequence += "1"
        }

        @Action(order = 2)
        @Throws(Exception::class)
        fun then2() {
            actionSequence += "2"
            isExecuted = true
        }

    }

    @org.jeasy.rules.annotation.Rule
    inner class DummyRule {

        @Condition
        fun condition(): Boolean {
            return true
        }

        @Action(order = 1)
        @Throws(Exception::class)
        fun action1() {
            // no op
        }

        @Action(order = 2)
        @Throws(Exception::class)
        fun action2() {
            // no op
        }
    }

}
