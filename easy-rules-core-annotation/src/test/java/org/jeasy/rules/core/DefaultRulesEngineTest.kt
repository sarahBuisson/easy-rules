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
import org.jeasy.rules.annotation.*
import org.jeasy.rules.api.*
import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.RuleProxy
import org.junit.After
import org.junit.Assert
import org.junit.Before
import kotlin.test.Test
import io.mockk.impl.annotations.MockK
import jdk.nashorn.internal.objects.NativeArray.every

class DefaultRulesEngineTest : AbstractTest() {
    @MockK
    private lateinit var rule1: Rule<Facts>
    @MockK
    private lateinit var rule2: Rule<Facts>

    @MockK
    private lateinit var ruleListener: RuleListener

    @MockK
    private val rulesEngineListener: RulesEngineListener? = null
    private var annotatedRule: AnnotatedRule? = null
    @Before
    @Throws(Exception::class)
    override fun setup() {
        super.setup()
        every {rule1.name} returns("r")
        every {rule1.priority} returns(1)
        annotatedRule = AnnotatedRule()
    }

    @Test
    @Throws(Exception::class)
    fun whenConditionIsTrue_thenActionShouldBeExecuted() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
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
        every {rule1.evaluate(facts)} returns(false)
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
        every {rule1.evaluate(facts)} returns(true)
        every {rule2.evaluate(facts)} returns(true)
        every {rule2.compareTo(rule1)} returns(1)
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
        every {rule1.evaluate(facts)} returns(true)
        every {rule2.evaluate(facts)} returns(true)
        every {rule2.compareTo(rule1)} returns(1)
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
    fun actionsMustBeExecutedInTheDefinedOrder() {
        // Given
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Assert.assertEquals("012", annotatedRule.getActionSequence())
    }

    @Test
    @Throws(Exception::class)
    fun annotatedRulesAndNonAnnotatedRulesShouldBeUsableTogether() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        Mockito.verify(rule1).execute(facts)
        assertTrue(annotatedRule.isExecuted()).isTrue
    }

    @Test
    fun whenRuleNameIsNotSpecified_thenItShouldBeEqualToClassNameByDefault() {
        val rule: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(DummyRule())
        assertTrue(rule.name,"DummyRule")
    }

    @Test
    fun whenRuleDescriptionIsNotSpecified_thenItShouldBeEqualToConditionNameFollowedByActionsNames() {
        val rule: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(DummyRule())
        assertTrue(rule.description,"when condition then action1,action2")
    }

    @Test
    fun testCheckRules() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        val result = rulesEngine.check(rules, facts)

        // Then
        assertTrue(result.asMap().size, 2)
        for (r in rules) {
            assertTrue(result[r]).isTrue
        }
    }

    @Test
    fun listenerShouldBeInvokedBeforeCheckingRules() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
        every {ruleListener.beforeEvaluate(rule1, facts)} returns(true)
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
        assertTrue(engineParameters).isNotSameAs(parameters)
        assertTrue(engineParameters).usingRecursiveComparison(,parameters)
    }

    @Test
    fun testGetRuleListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.getRuleListeners()

        // Then
        assertTrue(ruleListeners).contains(ruleListener)
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
        assertTrue(rulesEngineListeners).contains(rulesEngineListener)
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

    @Rule(name = "myRule", description = "my rule description")
    class AnnotatedRule {
        private var executed = false
        private var actionSequence: String? = ""
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then0() {
            actionSequence += "0"
        }

        @Action(order = 1)
        fun then1() {
            actionSequence += "1"
        }

        @Action(order = 2)
        fun then2() {
            actionSequence += "2"
            executed = true
        }

        @Priority
        fun getPriority(): Int {
            return 0
        }

        fun isExecuted(): Boolean {
            return executed
        }

        fun getActionSequence(): String? {
            return actionSequence
        }
    }

    @Rule
    class DummyRule {
        @Condition
        fun condition(): Boolean {
            return true
        }

        @Action(order = 1)
        fun action1() {
            // no op
        }

        @Action(order = 2)
        fun action2() {
            // no op
        }
    }
}
