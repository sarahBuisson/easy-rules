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

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Fact
import org.jeasy.rules.annotation.Rule
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import kotlin.test.*

/**
 * Null value in facts must be accepted, this is not same thing that fact missing
 */
class NullFactAnnotationParameterTest : AbstractTest() {

    @Test
    fun testNullFact() {
        val rules = Rules()
        rules.register(AnnotatedParametersRule())

        val facts = Facts()
        facts.put("fact1", Object())
        facts.put("fact2", null)

        val results = rulesEngine.check(rules, facts)

        for (b in results.values) {
            assertTrue(b)
        }
    }

    @Ignore// TODO : I don't agree with the purpose of this.
    @Test
    fun testMissingFact() {
        val rules = Rules()
        rules.register(AnnotatedParametersRule())

        val facts = Facts()
        facts.put("fact1", Object())

        val results = rulesEngine.check(rules, facts)

        for (b in results.values) {
            assertFalse(b)
        }
    }

    @Rule
    inner class AnnotatedParametersRule {

        @Condition
        fun `when`(@Fact("fact1") fact1: Object?, @Fact("fact2") fact2: Object?): Boolean {
            return fact1 != null && fact2 == null
        }

        @Action
        fun then(@Fact("fact1") fact1: Object, @Fact("fact2") fact2: Object) {
        }

    }
}
