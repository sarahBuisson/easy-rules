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
package org.jeasy.rules.api

import org.junit.Test

import java.util.HashMap

import org.assertj.core.api.Assertions.assertThat

class FactsTest {

    private val facts = Facts()

    @Test
    @Throws(Exception::class)
    fun factsMustHaveUniqueName() {
        facts.put("foo", 1)
        facts.put("foo", 2)

        assertThat(facts).hasSize(1)
        assertThat(facts.get("foo") as Int).isEqualTo(2)
    }

    @Test
    fun returnOfPut() {
        val o1 = facts.put("foo", 1)
        val o2 = facts.put("foo", 2)

        assertThat(o1).isEqualTo(null)
        assertThat(o2).isEqualTo(1)
    }

    @Test
    @Throws(Exception::class)
    fun remove() {
        facts.put("foo", 1)
        facts.remove("foo")

        assertThat(facts).isEmpty()
    }

    @Test
    fun returnOfRemove() {
        facts.put("foo", 1)
        val o1 = facts.remove("foo")
        val o2 = facts.remove("bar")

        assertThat(o1).isEqualTo(1)
        assertThat(o2).isEqualTo(null)
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        facts.put("foo", 1)
        assertThat(facts.get("foo") as Int).isEqualTo(1)
    }

    @Test
    fun asMap() {
        val o = facts.asMap()
        assertThat(o is HashMap).isTrue()
    }
/* TODO: useless
    @Test(expected = NullPointerException::class)
    @Throws(Exception::class)
    fun whenPutNullFact_thenShouldThrowNullPointerException() {
        facts.put(null, "foo")
    }

    @Test(expected = NullPointerException::class)
    @Throws(Exception::class)
    fun whenRemoveNullFact_thenShouldThrowNullPointerException() {
        facts.remove(null)
    }

    @Test(expected = NullPointerException::class)
    @Throws(Exception::class)
    fun whenGetNullFact_thenShouldThrowNullPointerException() {
        facts.get(null)
    }*/
}