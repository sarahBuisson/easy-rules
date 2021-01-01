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
import org.jeasy.rules.api.RulesEngineListener
import kotlin.test.Test
import io.mockk.impl.annotations.MockK
import io.mockk.verifyOrder
import org.jeasy.rules.api.Facts
import kotlin.test.BeforeTest

class RulesEngineListenerTest : AbstractTest() {
    @MockK
    private lateinit var rulesEngineListener1: RulesEngineListener<Facts>

    @MockK
    private lateinit var rulesEngineListener2: RulesEngineListener<Facts>

    @BeforeTest
    @Throws(Exception::class)
    override fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        super.setup()
        rulesEngine.registerRulesEngineListeners(listOf(rulesEngineListener1, rulesEngineListener2))
    }

    @Test
    @Throws(Exception::class)
    fun rulesEngineListenersShouldBeCalledInOrderWhenFiringRules() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then

        verifyOrder {
            (rulesEngineListener1).beforeEvaluate(rules, facts)
            (rulesEngineListener2).beforeEvaluate(rules, facts)
            (rule1).evaluate(facts)
            (rule1).execute(facts)
            (rulesEngineListener1).afterExecute(rules, facts)
            (rulesEngineListener2).afterExecute(rules, facts)
        }
    }

    @Test
    fun rulesEngineListenersShouldBeCalledInOrderWhenCheckingRules() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verifyOrder {
            (rulesEngineListener1).beforeEvaluate(rules, facts)
            (rulesEngineListener2).beforeEvaluate(rules, facts)
            (rule1).evaluate(facts)
            (rulesEngineListener1).afterExecute(rules, facts)
            (rulesEngineListener2).afterExecute(rules, facts)
        }
    }
}
