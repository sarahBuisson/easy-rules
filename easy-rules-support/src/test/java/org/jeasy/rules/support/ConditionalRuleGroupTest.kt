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
package org.jeasy.rules.support

import org.assertj.core.api.Assertions.assertThat
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.InvocationTargetException

@RunWith(MockitoJUnitRunner::class)
class ConditionalRuleGroupTest {

    @Mock
    lateinit var rule1: Rule
    @Mock
    lateinit var rule2: Rule
    @Mock
    lateinit var conditionalRule: Rule

    private val facts = Facts()
    private val rules = Rules()

    private val rulesEngine = DefaultRulesEngine()

    private lateinit var conditionalRuleGroup: ConditionalRuleGroup

    @Before
    fun setUp() {
        `when`(rule1!!.evaluate(facts)).thenReturn(false)
        `when`(rule1!!.priority).thenReturn(2)
        `when`(rule2!!.evaluate(facts)).thenReturn(true)
        `when`(rule2!!.priority).thenReturn(3)
        `when`(rule2!!.compareTo(rule1)).thenReturn(1)
        `when`(conditionalRule!!.compareTo(rule1)).thenReturn(1)
        `when`(conditionalRule!!.compareTo(rule2)).thenReturn(1)
        `when`(conditionalRule!!.priority).thenReturn(100)
        conditionalRuleGroup = ConditionalRuleGroup()
    }


    @Test
    @Throws(Exception::class)
    fun rulesMustNotBeExecutedIfConditionalRuleEvaluatesToFalse() {
        // Given
        `when`(conditionalRule!!.evaluate(facts)).thenReturn(false)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(rule2)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * The composing rules should not be executed
         * since the conditional rule evaluate to FALSE
         */

        // primaryRule should not be executed
        verify(conditionalRule, never()).execute(facts)
        //Rule 1 should not be executed
        verify(rule1, never()).execute(facts)
        //Rule 2 should not be executed
        verify(rule2, never()).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeExecutedForThoseThatEvaluateToTrueIfConditionalRuleEvaluatesToTrue() {
        // Given
        `when`(conditionalRule!!.evaluate(facts)).thenReturn(true)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(rule2)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * Some of he composing rules should be executed
         * since the conditional rule evaluate to TRUE
         */

        // primaryRule should be executed
        verify(conditionalRule, times(1)).execute(facts)
        //Rule 1 should not be executed
        verify(rule1, never()).execute(facts)
        //Rule 2 should be executed
        verify(rule2, times(1)).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun whenARuleIsRemoved_thenItShouldNotBeEvaluated() {
        // Given
        `when`(conditionalRule!!.evaluate(facts)).thenReturn(true)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(rule2)
        conditionalRuleGroup!!.addRule(conditionalRule)
        conditionalRuleGroup!!.removeRule(rule2)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        // primaryRule should be executed
        verify(conditionalRule, times(1)).execute(facts)
        //Rule 1 should not be executed
        verify(rule1, times(1)).evaluate(facts)
        verify(rule1, never()).execute(facts)
        // Rule 2 should not be evaluated nor executed
        verify(rule2, never()).evaluate(facts)
        verify(rule2, never()).execute(facts)

    }

    @Test
    @Throws(Exception::class)
    fun testCompositeRuleWithAnnotatedComposingRules() {
        // Given
        `when`(conditionalRule!!.evaluate(facts)).thenReturn(true)
        val rule = MyRule()
        conditionalRuleGroup = ConditionalRuleGroup("myConditinalRule")
        conditionalRuleGroup!!.addRule(rule)
        `when`(conditionalRule!!.compareTo(any(Rule::class.java))).thenReturn(1)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(conditionalRule, times(1)).execute(facts)
        assertThat(rule.isExecuted).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
        // Given
        `when`(conditionalRule!!.evaluate(facts)).thenReturn(true)
        val rule = MyRule()
        val annotatedRule = MyAnnotatedRule()
        conditionalRuleGroup = ConditionalRuleGroup("myCompositeRule", "composite rule with mixed types of rules")
        conditionalRuleGroup!!.addRule(rule)
        conditionalRuleGroup!!.addRule(annotatedRule)
        conditionalRuleGroup!!.removeRule(annotatedRule)
        `when`(conditionalRule!!.compareTo(any(Rule::class.java))).thenReturn(1)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(conditionalRule, times(1)).execute(facts)
        assertThat(rule.isExecuted).isTrue()
        assertThat(annotatedRule.isExecuted).isFalse()
    }

    @Test(expected = IllegalArgumentException::class)
    fun twoRulesWithSameHighestPriorityIsNotAllowed() {
        conditionalRuleGroup!!.addRule(MyOtherRule(1))
        conditionalRuleGroup!!.addRule(MyOtherRule(2))
        conditionalRuleGroup!!.addRule(MyRule())
        rules.register(conditionalRuleGroup)
        rulesEngine.fire(rules, facts)
    }

    @Test
    fun twoRulesWithSamePriorityIsAllowedIfAnotherRuleHasHigherPriority() {
        val rule1 = MyOtherRule(3)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(MyOtherRule(2))
        conditionalRuleGroup!!.addRule(MyRule())
        rules.register(conditionalRuleGroup)
        rulesEngine.fire(rules, facts)
        assertThat(rule1.isExecuted).isTrue()
    }

    @SuppressWarnings("unchecked")
    @Test
    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    fun aRuleWithoutPriorityHasAHighPriororty() {
        val rule1 = MyOtherRule(3)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(UnprioritizedRule())
        val m = conditionalRuleGroup!!::class.members.find { it.name=="sortRules"}
        //m.setAccessible(true)
        val sorted = m!!.call(conditionalRuleGroup) as List<Rule>
        assertThat(sorted[0].priority).isEqualTo(Integer.MAX_VALUE - 1)
        assertThat(sorted[1].priority).isEqualTo(3)
    }

    @org.jeasy.rules.annotation.Rule
    inner class MyRule {
        var isExecuted: Boolean = false
            internal set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }

        @Priority
        fun priority(): Int {
            return 2
        }

    }

    @org.jeasy.rules.annotation.Rule
    class MyAnnotatedRule {
        var isExecuted: Boolean = false
            private set

        @Condition
        fun evaluate(): Boolean {
            return true
        }

        @Action
        fun execute() {
            isExecuted = true
        }

        @Priority
        fun priority(): Int {
            return 3
        }
    }

    @org.jeasy.rules.annotation.Rule
    inner class MyOtherRule(private val priority: Int) {
        var isExecuted: Boolean = false
            internal set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }

        @Priority
        fun priority(): Int {
            return priority
        }

    }

    @org.jeasy.rules.annotation.Rule
    inner class UnprioritizedRule {
        var isExecuted: Boolean = false
            internal set

        @Condition
        fun `when`(): Boolean {
            return false
        }

        @Action
        fun then() {
            isExecuted = true
        }

    }
}
