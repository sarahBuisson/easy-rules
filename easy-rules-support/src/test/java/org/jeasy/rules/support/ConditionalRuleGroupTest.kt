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

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConditionalRuleGroupTest {

    @MockK
    lateinit var rule1: Rule<FactsMap>
    @MockK
    lateinit var rule2: Rule<FactsMap>
    @MockK
    lateinit var conditionalRule: Rule<FactsMap>

    private val facts = FactsMap()
    private val rules = Rules<FactsMap>()

    private val rulesEngine = DefaultRulesEngine<FactsMap>()

    private lateinit var conditionalRuleGroup: ConditionalRuleGroup<FactsMap>

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        every { rule1!!.evaluate(facts) } returns (false)
        every { rule1!!.priority } returns (2)
        every { rule2!!.evaluate(facts) } returns (true)
        every { rule2!!.priority } returns (3)
        every { rule2!!.compareTo(rule1) } returns (1)
        every { conditionalRule!!.compareTo(rule1) } returns (1)
        every { conditionalRule!!.compareTo(rule2) } returns (1)
        every { conditionalRule!!.priority } returns (100)
        conditionalRuleGroup = ConditionalRuleGroup()
    }


    @Test
    @Throws(Exception::class)
    fun rulesMustNotBeExecutedIfConditionalRuleEvaluatesToFalse() {
        // Given
        every { conditionalRule!!.evaluate(facts) } returns (false)
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
        verify(atLeast = 0, atMost = 0) { conditionalRule.execute(facts) }
        //Rule 1 should not be executed
        verify(atLeast = 0, atMost = 0) { rule1.execute(facts) }
        //Rule 2 should not be executed
        verify(atLeast = 0, atMost = 0) { rule2.execute(facts) }
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeExecutedForThoseThatEvaluateToTrueIfConditionalRuleEvaluatesToTrue() {
        // Given
        every { conditionalRule!!.evaluate(facts) } returns (true)
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
        verify(atLeast = 1, atMost = 1) { conditionalRule.execute(facts) }
        //Rule 1 should not be executed
        verify(atLeast = 0, atMost = 0) { rule1.execute(facts) }
        //Rule 2 should be executed
        verify(atLeast = 1, atMost = 1) { rule2.execute(facts) }
    }

    @Test
    @Throws(Exception::class)
    fun whenARuleIsRemoved_thenItShouldNotBeEvaluated() {
        // Given
        every { conditionalRule!!.evaluate(facts) } returns (true)
        conditionalRuleGroup!!.addRule(rule1)
        conditionalRuleGroup!!.addRule(rule2)
        conditionalRuleGroup!!.addRule(conditionalRule)
        conditionalRuleGroup!!.removeRule(rule2)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        // primaryRule should be executed
        verify(atLeast = 1, atMost = 1) { conditionalRule.execute(facts) }
        //Rule 1 should not be executed
        verify(atLeast = 1, atMost = 1) { rule1.evaluate(facts) }
        verify(atLeast = 0, atMost = 0) { rule1.execute(facts) }
        // Rule 2 should not be evaluated nor executed
        verify(atLeast = 0, atMost = 0) { rule2.evaluate(facts) }
        verify(atLeast = 0, atMost = 0) { rule2.execute(facts) }

    }

    @Test
    @Throws(Exception::class)
    fun testCompositeRuleWithAnnotatedComposingRules() {
        // Given
        every { conditionalRule!!.evaluate(facts) } returns (true)
        val rule = MyRule()
        conditionalRuleGroup = ConditionalRuleGroup("myConditinalRule")
        conditionalRuleGroup!!.addRule(rule)
        every { conditionalRule!!.compareTo(ofType(Rule::class) as Rule<FactsMap>) } returns (1)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(atMost = 1, atLeast = 1) { conditionalRule.execute(facts) }
        assertTrue(rule.isExecuted)
    }

    @Test
    @Throws(Exception::class)
    fun whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
        // Given
        every { conditionalRule!!.evaluate(facts) } returns (true)
        val rule = MyRule()
        val annotatedRule = MyAnnotatedRule()
        conditionalRuleGroup = ConditionalRuleGroup("myCompositeRule", "composite rule with mixed types of rules")
        conditionalRuleGroup!!.addRule(rule)
        conditionalRuleGroup!!.addRule(annotatedRule)
        conditionalRuleGroup!!.removeRule(annotatedRule)
        every { conditionalRule!!.compareTo(ofType(Rule::class) as Rule<FactsMap>) } returns (1)
        conditionalRuleGroup!!.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(atMost = 1) { conditionalRule.execute(facts) }
        assertTrue(rule.isExecuted)
        assertFalse(annotatedRule.isExecuted)
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
        assertTrue(rule1.isExecuted)
    }

    @SuppressWarnings("unchecked")
    @Test
    @Throws(NoSuchMethodException::class, IllegalAccessException::class)
    fun aRuleWithoutPriorityHasAHighPriororty() {
        val rule1 = MyOtherRule(3)
        conditionalRuleGroup.addRule(rule1)
        conditionalRuleGroup.addRule(UnprioritizedRule())
        val m = conditionalRuleGroup::class.members.find { it.name == "sortRules" }
        //m.setAccessible(true)
        val sorted = m!!.call(conditionalRuleGroup) as List<Rule<FactsMap>>
        assertEquals(sorted[0].priority, Integer.MAX_VALUE - 1)
        assertEquals(sorted[1].priority, 3)
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
    inner class MyAnnotatedRule {
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
