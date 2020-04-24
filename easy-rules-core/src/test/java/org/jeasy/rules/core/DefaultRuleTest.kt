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

import io.mockk.*
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import kotlin.test.Test

class DefaultRuleTest : AbstractTest() {


    private  var condition: Condition<FactsMap> = mockk<Condition<FactsMap>> (relaxed = true)

    private  var action1: Action<FactsMap> = mockk<Action<FactsMap>> (relaxed = true)

    private  var action2: Action<FactsMap> = mockk<Action<FactsMap>> (relaxed = true)

    @Test
    //@Throws(Exception::class)
    fun WhenConditionIsTrue_ThenActionsShouldBeExecutedInOrder() {
        //        // given
        condition = mockk<Condition<FactsMap>>()
        every { condition.evaluate(facts) } returns true
        val rule = RuleBuilder<FactsMap>()
                .`when`(condition)
                .then(action1)
                .then(action2)
                .build()
        rules.register(rule)

        // when
        rulesEngine.fire(rules, facts)

        // then
        verifyOrder {
            action1.execute(facts)
            action2.execute(facts)
        }
        //confirmVerified(action1)
        //confirmVerified(rule2)

    }

    @Test
    //@Throws(Exception::class)
    fun WhenConditionIsFalse_ThenActionsShouldNotBeExecuted() {
        // given
        every {condition.evaluate(any())} returns (false)
        val rule = RuleBuilder<FactsMap>()
                .`when`(condition)
                .then(action1)
                .then(action2)
                .build()
        rules.register(rule)

        // when
        rulesEngine.fire(rules, facts)

        // then
        verify(atMost = 0, atLeast = 0) { action1.execute(facts) }
        verify(atMost = 0, atLeast = 0) { action2.execute(facts) }
    }
}
