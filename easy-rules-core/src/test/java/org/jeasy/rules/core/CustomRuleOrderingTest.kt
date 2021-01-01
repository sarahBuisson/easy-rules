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
import io.mockk.MockKAnnotations
import io.mockk.verifyOrder
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import kotlin.test.Test
import kotlin.test.BeforeTest

class CustomRuleOrderingTest : AbstractTest() {
    @MockK
    protected lateinit var myRule1: MyRule

    @MockK
    protected lateinit var myRule2: MyRule

    @BeforeTest
    @Throws(Exception::class)
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    @Throws(Exception::class)
    fun whenCompareToIsOverridden_thenShouldExecuteRulesInTheCustomOrder() {
        // Given
        every { myRule1.name } returns ("a")
        every { myRule1.priority } returns (1)
        every { myRule1.evaluate(facts) } returns (true)
        every { myRule2.name } returns ("b")
        every { myRule2.priority } returns (0)
        every { myRule2.evaluate(facts) } returns (true)
        //every {myRule2.compareTo(myRule1)}  .thenCallRealMethod()
        rules.register(myRule1)
        rules.register(myRule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        /*
         * By default, if compareTo is not overridden, then myRule2 should be executed first (priority 0 < 1).
         * But in this case, the compareTo method order rules by their name, so myRule1 should be executed first ("a" < "b")
         */

        verifyOrder {
            (myRule1).execute(facts)
            (myRule2).execute(facts)
        }
    }

    open class MyRule : BasicRule<Facts>() {
        override fun compareTo(rule: Rule<Facts>): Int {
            return name.compareTo(rule.name)
        }
    }
}
