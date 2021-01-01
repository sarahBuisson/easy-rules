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

import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BasicRuleTest : AbstractTest() {
    @Test
    fun basicRuleEvaluateShouldReturnFalse() {
        val basicRule = BasicRule<Facts>()
        assertFalse(basicRule.evaluate(facts))
    }

    @Test
    fun testCompareTo() {
        val rule1 = FirstRule()
        val rule2 = FirstRule()
        assertEquals(rule1.compareTo(rule2), 0)
        assertEquals(rule2.compareTo(rule1), 0)
    }

    @Test
    fun testSortSequence() {
        val rule1 = FirstRule()
        val rule2 = SecondRule()
        val rule3 = ThirdRule()
        rules = Rules(rule1, rule2, rule3)
        rulesEngine.check(rules, facts)
        assertEquals(rules.toList().get(0), rule1)
        assertEquals(rules.toList().get(1), rule3)
        assertEquals(rules.toList().get(2), rule2)
    }

    internal class FirstRule : BasicRule<Facts>(name = "rule1", priority = 1) {
        override fun evaluate(facts: Facts): Boolean {
            return true
        }
    }

    internal class SecondRule : BasicRule<Facts>(name = "rule2", priority = 3) {
        override fun evaluate(facts: Facts): Boolean {
            return true
        }
    }

    internal class ThirdRule : BasicRule<Facts>(name = "rule3", priority = 2) {
        override fun evaluate(facts: Facts): Boolean {
            return true
        }

    }
}
