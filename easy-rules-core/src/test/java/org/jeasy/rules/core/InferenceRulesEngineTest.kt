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

import org.jeasy.rules.annotation.*
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import org.junit.Test



class InferenceRulesEngineTest {
    /*
        @Test
        @Throws(Exception::class)
        fun testCandidateSelection() {
            // Given
            val facts = Facts()
            facts.put("foo", true)
            val dummyRule = DummyRule()
            val anotherDummyRule = AnotherDummyRule()
            val rules = Rules(dummyRule, anotherDummyRule)
            val rulesEngine = InferenceRulesEngine()

            // When
            rulesEngine.fire(rules, facts)

            // Then
            assertThat(dummyRule.isExecuted).isTrue()
            assertThat(anotherDummyRule.isExecuted).isFalse()
        }

        @Test
        @Throws(Exception::class)
        fun testCandidateOrdering() {
            // Given
            val facts = Facts()
            facts.put("foo", true)
            facts.put("bar", true)
            val dummyRule = DummyRule()
            val anotherDummyRule = AnotherDummyRule()
            val rules = Rules(dummyRule, anotherDummyRule)
            val rulesEngine = InferenceRulesEngine()

            // When
            rulesEngine.fire(rules, facts)

            // Then
            assertThat(dummyRule.isExecuted).isTrue()
            assertThat(anotherDummyRule.isExecuted).isTrue()
            assertThat(dummyRule.timestamp).isLessThanOrEqualTo(anotherDummyRule.timestamp)
        }
    */
    @Rule
    internal inner class DummyRule {

        var isExecuted: Boolean = false
            private set
        var timestamp: Long = 0
            private set

        @Condition
        fun `when` (@Fact("foo") foo: Boolean): Boolean {
            return foo
        }

        @Action
        fun then(facts: Facts) {
            isExecuted = true
            timestamp = System.currentTimeMillis()
            facts.remove("foo")
        }

        @Priority
        fun priority(): Int {
            return 1
        }
    }

    @Rule
    internal inner class AnotherDummyRule {

        var isExecuted: Boolean = false
            private set
        var timestamp: Long = 0
            private set

        @Condition
        fun `when` (@Fact("bar") bar: Boolean): Boolean {
            return bar
        }

        @Action
        fun then(facts: Facts) {
            isExecuted = true
            timestamp = System.currentTimeMillis()
            facts.remove("bar")
        }

        @Priority
        fun priority(): Int {
            return 2
        }
    }

}