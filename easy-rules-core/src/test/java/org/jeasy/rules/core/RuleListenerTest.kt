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

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.RuleListener
import kotlin.test.BeforeTest
import kotlin.test.Test

class RuleListenerTest : AbstractTest() {

    @MockK
    private lateinit var ruleListener1: RuleListener<Facts>

    @MockK
    private lateinit var ruleListener2: RuleListener<Facts>

    @BeforeTest
    @Throws(Exception::class)
    override fun setup() {
        super.setup()
        every { ruleListener1.beforeEvaluate(rule1, facts) } returns (true)
        every { ruleListener2.beforeEvaluate(rule1, facts) } returns (true)
        rulesEngine.registerRuleListener(ruleListener1)
        rulesEngine.registerRuleListener(ruleListener2)
    }

    @Test
    fun whenTheRuleExecutesSuccessfully_thenOnSuccessShouldBeExecuted() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then


        verifyOrder {
            (ruleListener1).beforeExecute(rule1, facts)
            (ruleListener2).beforeExecute(rule1, facts)
            (ruleListener1).onSuccess(rule1, facts)
            (ruleListener2).onSuccess(rule1, facts)
        }
    }

    @Test
    @Throws(Exception::class)
    fun whenTheRuleFails_thenOnFailureShouldBeExecuted() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        val exception = Exception("fatal error!")
        every { (rule1).execute(facts) } throws (exception)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verifyOrder {
            (ruleListener1).beforeExecute(rule1, facts)
            (ruleListener2).beforeExecute(rule1, facts)
            (ruleListener1).onFailure(rule1, facts, exception)
            (ruleListener2).onFailure(rule1, facts, exception)
        }
    }

    @Test
    fun whenListenerBeforeEvaluateReturnsFalse_thenTheRuleShouldBeSkippedBeforeBeingEvaluated() {
        // Given
        every { ruleListener1.beforeEvaluate(rule1, facts) } returns (false)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(atLeast = 0, atMost = 0) { rule1.evaluate(facts) }
    }

    @Test
    fun whenListenerBeforeEvaluateReturnsTrue_thenTheRuleShouldBeEvaluated() {
        // Given
        every { ruleListener1.beforeEvaluate(rule1, facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify { rule1.evaluate(facts) }
    }

    @Test
    fun whenTheRuleEvaluatesToTrue_thenTheListenerShouldBeInvoked() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify { (ruleListener1).afterEvaluate(rule1, facts, true) }
    }

    @Test
    fun whenTheRuleEvaluatesToFalse_thenTheListenerShouldBeInvoked() {
        // Given
        every { rule1.evaluate(facts) } returns (false)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify { (ruleListener1).afterEvaluate(rule1, facts, false) }
    }
}
