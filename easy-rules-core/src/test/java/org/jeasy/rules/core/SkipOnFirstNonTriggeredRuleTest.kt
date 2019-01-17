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
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

class SkipOnFirstNonTriggeredRuleTest : AbstractTest() {

    @BeforeTest
    @Throws(Exception::class)
    override fun setup() {
        super.setup()
        val parameters = RulesEngineParameters().skipOnFirstNonTriggeredRule(true)
        rulesEngine = DefaultRulesEngine(parameters)
    }

    @Test
    @Throws(Exception::class)
    fun testSkipOnFirstNonTriggeredRule() {
        // Given
        every {rule1.evaluate(facts)} returns (false)
        every {rule2.compareTo(rule1)} returns (1)

        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        //Rule1 is not triggered
        verify(atMost = 0, atLeast = 0) {rule1.execute(facts) }
        //Rule 2 should be skipped since Rule 1 has not been executed
        verify(atMost = 0, atLeast = 0) {rule2.execute(facts)  }
    }

}
