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

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import org.jeasy.rules.api.*
import kotlin.test.*

class DefaultRulesEngineTest : AbstractTest() {

    @MockK
    private lateinit var ruleListener: RuleListener<Facts>

    @MockK
    private lateinit var rulesEngineListener: RulesEngineListener<Facts>

    @BeforeTest
    @Throws(Exception::class)
    override fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        super.setup()
        every {rule1.name} returns("r")
        every {rule1.priority} returns(1)
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
        verify { rule1.execute(facts) }
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
        verify(atLeast = 0, atMost = 0) { rule1.execute(facts) }
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
        verifyOrder {
            (rule1).execute(facts)
            (rule2).execute(facts)
        }
    }

    @Test
    fun rulesMustBeCheckedInTheirNaturalOrder() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        every { rule2.evaluate(facts) } returns (true)
        every { rule2.compareTo(rule1) } returns (1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verifyOrder {
            rule1.evaluate(facts)
            rule2.evaluate(facts)
        }
    }

    @Test
    fun testCheckRules() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
        rules.register(rule1)

        // When
        val result = rulesEngine.check(rules, facts)

        // Then
        assertEquals(result.toList().size, 1)
        for (r in rules) {
            assertTrue(result.get(r)!!)
        }
    }

    @Test
    fun listenerShouldBeInvokedBeforeCheckingRules() {
        // Given
        every {rule1.evaluate(facts)} returns(true)
        every {ruleListener.beforeEvaluate(rule1, facts)} returns(true)
        val rulesEngine = DefaultRulesEngine<Facts>()
        rulesEngine.registerRuleListener(ruleListener)
        rules.register(rule1)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verify { ruleListener.beforeEvaluate(rule1, facts) }
    }

    @Test
    fun getParametersShouldReturnACopyOfTheParameters() {
        // Given
        val parameters = RulesEngineParameters()
            .skipOnFirstAppliedRule(true)
            .skipOnFirstFailedRule(true)
            .skipOnFirstNonTriggeredRule(true)
            .priorityThreshold(42)
        val rulesEngine = DefaultRulesEngine<Facts>(parameters)

        // When
        val engineParameters = rulesEngine.getParameters()

        // Then
        assertNotEquals(engineParameters, parameters)
        assertSame(engineParameters.getPriorityThreshold(), parameters.getPriorityThreshold())
        assertSame(engineParameters.isSkipOnFirstNonTriggeredRule(), parameters.isSkipOnFirstNonTriggeredRule())
        assertSame(engineParameters.isSkipOnFirstAppliedRule(), parameters.isSkipOnFirstAppliedRule())
        assertSame(engineParameters.isSkipOnFirstFailedRule(), parameters.isSkipOnFirstFailedRule())
    }

    @Test
    fun testGetRuleListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine<Facts>()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.getRuleListeners()

        // Then
        assertTrue(ruleListeners.contains(ruleListener))
    }

    @Test
    fun testGetRulesEngineListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine<Facts>()
        rulesEngine.registerRulesEngineListener(rulesEngineListener)

        // When
        val rulesEngineListeners = rulesEngine.getRulesEngineListeners()

        // Then
        assertTrue(rulesEngineListeners.contains(rulesEngineListener))
    }

    @AfterTest
    fun clearRules() {
        rules.clear()
    }


    class DummyRule: BasicRule<Facts>() {

        override fun evaluate(facts: Facts): Boolean {
            return true
        }


        override fun execute(facts: Facts) {
            // no op
        }
    }
}
