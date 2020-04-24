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
import org.jeasy.rules.core.FactsMap
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnitRuleGroupTest {

    @MockK
    private lateinit var rule1: Rule<FactsMap>
    @MockK
    private lateinit var  rule2: Rule<FactsMap>

    private val facts = FactsMap()
    private val rules = Rules<FactsMap>()

    private val rulesEngine = DefaultRulesEngine<FactsMap>()

    private lateinit var unitRuleGroup: org.jeasy.rules.support.UnitRuleGroup<FactsMap>


    @BeforeTest
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        every { rule1!!.evaluate(facts)} returns (true)
        every {rule2!!.evaluate(facts)} returns (true)
        every {rule2!!.compareTo(rule1)} returns (1)
    }

    @Test
    fun whenNoComposingRulesAreRegistered_thenUnitRuleGroupShouldEvaluateToFalse() {
        // given
        unitRuleGroup = UnitRuleGroup()

        // when
        val evaluationResult = unitRuleGroup!!.evaluate(facts)

        // then
        assertFalse(evaluationResult)
    }

    @Test
    @Throws(Exception::class)
    fun compositeRuleAndComposingRulesMustBeExecuted() {
        // Given
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup!!.addRule(rule1)
        unitRuleGroup!!.addRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify{rule1.execute(facts)}
        verify{rule2.execute(facts)}
    }

    @Throws(Exception::class)
    fun compositeRuleMustNotBeExecutedIfAComposingRuleEvaluatesToFalse() {
        // Given
        every {rule2!!.evaluate(facts)} returns (false)
        unitRuleGroup!!.addRule(rule1)
        unitRuleGroup!!.addRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * The composing rules should not be executed
         * since not all rules conditions evaluate to TRUE
         */

        //Rule 1 should not be executed
        verify(atLeast = 0, atMost = 0) { rule1.execute(facts) }
        //Rule 2 should not be executed
        verify(atLeast = 0, atMost = 0) { rule2.execute(facts) }
    }

    @Test
    @Throws(Exception::class)
    fun whenARuleIsRemoved_thenItShouldNotBeEvaluated() {
        // Given
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup!!.addRule(rule1)
        unitRuleGroup!!.addRule(rule2)
        unitRuleGroup!!.removeRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        //Rule 1 should be executed
        verify{rule1.execute(facts)}

        //Rule 2 should not be evaluated nor executed
        verify(atLeast = 0,atMost = 0){rule2.evaluate(facts)}
        verify(atLeast = 0,atMost = 0){rule2.execute(facts)}
    }

    @Test
    @Throws(Exception::class)
    fun testCompositeRuleWithAnnotatedComposingRules() {
        // Given
        val rule = MyRule()
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup!!.addRule(rule)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertTrue(rule.isExecuted)
    }

    @Test
    @Throws(Exception::class)
    fun whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
        // Given
        val rule = MyRule()
        val annotatedRule = MyAnnotatedRule()
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup!!.addRule(rule)
        unitRuleGroup!!.addRule(annotatedRule)
        unitRuleGroup!!.removeRule(annotatedRule)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertTrue(rule.isExecuted)
        assertFalse(annotatedRule.isExecuted)
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
    }
}
