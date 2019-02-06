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

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifyOrder
import org.jeasy.rules.api.Rule
import kotlin.test.Test

class CustomRuleOrderingTest : AbstractTest() {

    @MockK
    override lateinit var rule1: Rule
    @MockK
    override lateinit var rule2: Rule

    @Test
    //@Throws(Exception::class)
    fun whenCompareToIsOverridden_thenShouldExecuteRulesInTheCustomOrder() {
        rule1 = spyk<MyRule>(MyRule())
        rule2 = spyk<MyRule>(MyRule())
        // Given
        every { rule1!!.name } returns ("a")
        every { rule1!!.priority } returns (1)
        every { rule1!!.evaluate(facts) } returns (true)

        every { rule2!!.name } returns ("b")
        every { rule2!!.priority } returns (0)
        every { rule2!!.evaluate(facts) } returns (true)

        //TODO
        /* every {
             rule2!!.compareTo(rule1)}
          .thenCallRealMethod()
 */
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * By default, if compareTo is not overridden, then rule2 should be executed first (priority 0 < 1).
         * But in this case, the compareTo method order rules by their name, so rule1 should be executed first ("a" < "b")
         */

        verifyOrder {
            rule1.execute(facts)
            rule2.execute(facts)
        }
    }

    class MyRule : BasicRule() {


        override operator fun compareTo(rule: Rule): Int {
            return name.compareTo(rule.name)
        }
    }
}

