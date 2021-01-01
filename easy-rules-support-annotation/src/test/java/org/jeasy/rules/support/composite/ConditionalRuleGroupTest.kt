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
package org.jeasy.rules.support.composite

import org.assertj.core.api.Assertions
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.annotation.Rule
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.BasicRule
import org.jeasy.rules.core.DefaultRulesEngine
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import java.util.*

class ConditionalRuleGroupTest {
    private lateinit var rule1: TestRule
    private lateinit var rule2: TestRule
    private lateinit var conditionalRule: TestRule
    private lateinit var conditionalRuleGroup: ConditionalRuleGroup
    private val facts: Facts = Facts()
    private val rules: Rules = Rules()
    private val rulesEngine: DefaultRulesEngine = DefaultRulesEngine()

    @Before
    fun setUp() {
        conditionalRule = TestRule("conditionalRule", "description0", 0, true)
        rule1 = TestRule("rule1", "description1", 1, true)
        rule2 = TestRule("rule2", "description2", 2, true)
        conditionalRuleGroup = ConditionalRuleGroup()
        conditionalRuleGroup.addRule(rule1)
        conditionalRuleGroup.addRule(rule2)
        conditionalRuleGroup.addRule(conditionalRule)
        rules.register(conditionalRuleGroup)
    }

    @After
    fun tearDown() {
        rules.clear()
        actions.clear()
    }

    @Test
    fun rulesMustNotBeExecutedIfConditionalRuleEvaluatesToFalse() {
        // Given
        conditionalRule.evaluationResult = (false)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * The composing rules should not be executed
         * since the conditional rule evaluate to FALSE
         */

        // primaryRule should not be executed
        assertTrue(conditionalRule.isExecuted()).isFalse
        //Rule 1 should not be executed
        assertTrue(rule1.isExecuted()).isFalse
        //Rule 2 should not be executed
        assertTrue(rule2.isExecuted()).isFalse
    }

    @Test
    fun selectedRulesMustBeExecutedIfConditionalRuleEvaluatesToTrue() {
        // Given
        rule1.evaluationResult = (false)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * Selected composing rules should be executed
         * since the conditional rule evaluates to TRUE
         */

        // primaryRule should be executed
        assertTrue(conditionalRule.isExecuted()).isTrue
        //Rule 1 should not be executed
        assertTrue(rule1.isExecuted()).isFalse
        //Rule 2 should be executed
        assertTrue(rule2.isExecuted()).isTrue
    }

    @Test
    fun whenARuleIsRemoved_thenItShouldNotBeEvaluated() {
        // Given
        conditionalRuleGroup.removeRule(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        // primaryRule should be executed
        assertTrue(conditionalRule.isExecuted()).isTrue
        //Rule 1 should be executed
        assertTrue(rule1.isExecuted()).isTrue
        // Rule 2 should not be executed
        assertTrue(rule2.isExecuted()).isFalse
    }

    @Test
    fun testCompositeRuleWithAnnotatedComposingRules() {
        // Given
        val rule = MyRule()
        conditionalRuleGroup.addRule(rule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertTrue(conditionalRule.isExecuted()).isTrue
        assertTrue(rule.isExecuted()).isTrue
    }
/*
    @Test
    fun whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
        // Given
        val rule = MyRule()
        val annotatedRule = MyAnnotatedRule()
        conditionalRuleGroup.addRule(rule)
        conditionalRuleGroup.addRule(annotatedRule)
        conditionalRuleGroup.removeRule(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertTrue(conditionalRule.isExecuted()).isTrue
        assertTrue(rule.isExecuted()).isTrue
        assertTrue(annotatedRule.isExecuted()).isFalse
    }
*/
    @Test(expected = IllegalArgumentException::class)
    fun twoRulesWithSameHighestPriorityIsNotAllowed() {
        conditionalRuleGroup.addRule(MyOtherRule(0)) // same priority as conditionalRule
        conditionalRuleGroup.addRule(MyOtherRule(1))
        conditionalRuleGroup.addRule(MyRule())
        conditionalRuleGroup.evaluate(facts)
    }

    @Test
    fun twoRulesWithSamePriorityIsAllowedIfAnotherRuleHasHigherPriority() {
        val rule1 = MyOtherRule(3)
        conditionalRuleGroup.addRule(rule1)
        conditionalRuleGroup.addRule(MyOtherRule(2))
        conditionalRuleGroup.addRule(MyRule())
        rules.register(conditionalRuleGroup)
        rulesEngine.fire(rules, facts)
        assertTrue(rule1.isExecuted()).isTrue
    }

    @Test
    fun aRuleWithoutPriorityHasLowestPriority() {
        // given
        val rule = UnprioritizedRule()
        conditionalRuleGroup.addRule(rule)

        // when
        rulesEngine.fire(rules, facts)

        // then
        assertTrue(actions).containsExactly(
            "conditionalRule",
            "rule1",
            "rule2",
            "UnprioritizedRule"
        )
    }

    @Test
    fun testComposingRulesExecutionOrder() {
        // When
        rulesEngine.fire(rules, facts)

        // Then
        // rule 1 has higher priority than rule 2 (lower values for highers priorities),
        // it should be executed first
        assertTrue(actions).containsExactly(
            "conditionalRule",
            "rule1",
            "rule2"
        )
    }

    @Rule
    class MyRule : BasicRule() {
        private var executed = false

        override fun evaluate(facts: Facts): Boolean {
            return true
        }

        override fun execute(facts: Facts) {
            executed = true
        }

        @Priority
        fun priority(): Int {
            return 2
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule
    class MyAnnotatedRule {
        private var executed = false

        @Condition
        fun evaluate(): Boolean {
            return true
        }

        @Action
        fun execute() {
            executed = true
        }

        @Priority
        fun priority(): Int {
            return 3
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule
    class MyOtherRule(priority: Int) : BasicRule(priority = priority) {
        private var executed = false

        override fun evaluate(facts: Facts): Boolean {
            return true
        }

        override fun execute(facts: Facts) {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule
    class UnprioritizedRule : BasicRule() {
        var executed = false
        override fun evaluate(facts: Facts): Boolean {
            return true
        }

        override fun execute(facts: Facts) {
            executed = true
            actions.add("UnprioritizedRule")
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    class TestRule internal constructor(
        name: String,
        description: String,
        priority: Int,
        var evaluationResult: Boolean
    ) : BasicRule(name, description, priority) {
        var executed = false
        override fun evaluate(facts: Facts): Boolean {
            return evaluationResult
        }

        override fun execute(facts: Facts) {
            executed = true
            actions.add(name)
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    companion object {
        private val actions: MutableList<String> = ArrayList()
    }
}
