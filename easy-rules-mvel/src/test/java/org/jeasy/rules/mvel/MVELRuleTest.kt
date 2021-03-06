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
package org.jeasy.rules.mvel

import org.jeasy.rules.api.Facts
import org.junit.Before
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class MVELRuleTest {

    private val facts = Facts()
    private val mvelRule = MVELRule().name("rn").description("rd").priority(1)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mvelRule.`when`("person.age > 18")
        mvelRule.then("person.setAdult(true);")
    }

    @Test
    @Throws(Exception::class)
    fun whenTheRuleIsTriggered_thenConditionShouldBeEvaluated() {
        // given
        facts.put("person", Person("foo", 20))

        // when
        val evaluationResult = mvelRule.evaluate(facts)

        // then
        assertThat(evaluationResult).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun whenTheConditionIsTrue_thenActionsShouldBeExecuted() {
        // given
        val foo = Person("foo", 20)
        facts.put("person", foo)

        // when
        mvelRule.execute(facts)

        // then
        assertThat(foo.isAdult).isTrue()
    }
}