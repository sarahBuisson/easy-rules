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

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Facts
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemOutRule

import org.assertj.core.api.Assertions.assertThat

class MVELActionTest {

    @Rule
    public open val systemOutRule = SystemOutRule().enableLog()

    @Test
    @Throws(Exception::class)
    fun testMVELActionExecution() {
        // given
        val markAsAdult = MVELAction("person.setAdult(true);")
        val facts = Facts()
        val foo = Person("foo", 20)
        facts.put("person", foo)

        // when
        markAsAdult.execute(facts)

        // then
        assertThat(foo.isAdult).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun testMVELFunctionExecution() {
        // given
        val printAction = MVELAction("def hello() { System.out.println(\"Hello from MVEL!\"); }; hello();")
        val facts = Facts()

        // when
        printAction.execute(facts)

        // then
        assertThat(systemOutRule.getLog()).contains("Hello from MVEL!")
    }
}