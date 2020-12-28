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
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.BasicRule
import org.jeasy.rules.core.DefaultRulesEngine
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UnitRuleGroupTest {
    @Mock
    private lateinit var rule1: Rule

    @Mock
    private lateinit var rule2: Rule
    private val facts: Facts = Facts()
    private val rules: Rules = Rules()
    private val rulesEngine: DefaultRulesEngine = DefaultRulesEngine()
    private lateinit var unitRuleGroup: UnitRuleGroup
    @Before
    fun setUp() {
        Mockito.`when`(rule1.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.evaluate(facts)).thenReturn(true)
        Mockito.`when`(rule2.compareTo(rule1)).thenReturn(1)
    }

    @Test
    fun whenNoComposingRulesAreRegistered_thenUnitRuleGroupShouldEvaluateToFalse() {
        // given
        unitRuleGroup = UnitRuleGroup()

        // when
        val evaluationResult = unitRuleGroup.evaluate(facts)

        // then
        Assertions.assertThat(evaluationResult).isFalse
    }

    @Test
    @Throws(Exception::class)
    fun compositeRuleAndComposingRulesMustBeExecuted() {
        // Given
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup.addRule(rule1)
        unitRuleGroup.addRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Mockito.verify(rule1).execute(facts)
        Mockito.verify(rule2).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun compositeRuleMustNotBeExecutedIfAComposingRuleEvaluatesToFalse() {
        // Given
        Mockito.`when`(rule2.evaluate(facts)).thenReturn(false)
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup.addRule(rule1)
        unitRuleGroup.addRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * The composing rules should not be executed
         * since not all rules conditions evaluate to TRUE
         */

        //Rule 1 should not be executed
        Mockito.verify(rule1, Mockito.never()).execute(facts)
        //Rule 2 should not be executed
        Mockito.verify(rule2, Mockito.never()).execute(facts)
    }

    @Test
    @Throws(Exception::class)
    fun whenARuleIsRemoved_thenItShouldNotBeEvaluated() {
        // Given
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup.addRule(rule1)
        unitRuleGroup.addRule(rule2)
        unitRuleGroup.removeRule(rule2)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        //Rule 1 should be executed
        Mockito.verify(rule1).execute(facts)

        //Rule 2 should not be evaluated nor executed
        Mockito.verify(rule2, Mockito.never()).evaluate(facts)
        Mockito.verify(rule2, Mockito.never()).execute(facts)
    }

    @Test
    fun testCompositeRuleWithAnnotatedComposingRules() {
        // Given
        val rule = MyRule()
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup.addRule(rule)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(rule.isExecuted()).isTrue
    }

   @Test
    fun whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
        // Given
        val rule = MyRule()
        val annotatedRule = MyAnnotatedRule()
        unitRuleGroup = UnitRuleGroup()
        unitRuleGroup.addRule(rule)
        unitRuleGroup.addRule(annotatedRule)
        unitRuleGroup.removeRule(annotatedRule)
        rules.register(unitRuleGroup)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assertions.assertThat(rule.isExecuted()).isTrue
        Assertions.assertThat(annotatedRule.isExecuted()).isFalse
    }

    class MyRule : BasicRule() {
        var executed = false
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @org.jeasy.rules.annotation.Rule
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

        fun isExecuted(): Boolean {
            return executed
        }
    }
}
