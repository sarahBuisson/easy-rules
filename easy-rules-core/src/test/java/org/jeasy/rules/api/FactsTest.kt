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
package org.jeasy.rules.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.asserter

class FactsTest {
    private val facts: Facts = Facts()

    @Test
    fun factsMustHaveUniqueName() {
        facts.add(Fact("foo", 1))
        facts.add(Fact("foo", 2))
        assertEquals(facts.asMap().size, 1)
        val fact = facts.getFact("foo")
        assertEquals(fact?.value, 2)
    }

    @Test
    fun testAdd() {
        val fact1 = Fact("foo", 1)
        val fact2 = Fact("bar", 2)
        facts.add(fact1)
        facts.add(fact2)
        assertTrue(facts.contains(fact1))
        assertTrue(facts.contains(fact2))
    }

    @Test
    fun testPut() {
        facts.put("foo", 1)
        facts.put("bar", 2)
        assertTrue(facts.contains(Fact("foo", 1)))
        assertTrue(facts.contains(Fact("bar", 2)))
    }

    @Test
    fun testRemove() {
        val foo = Fact("foo", 1)
        facts.add(foo)
        facts.remove(foo)
        assertTrue(facts.asMap().isEmpty())
    }

    @Test
    fun testRemoveByName() {
        val foo = Fact("foo", 1)
        facts.add(foo)
        facts.remove("foo")
        assertTrue(facts.asMap().isEmpty())
    }

    @Test
    fun testGet() {
        val fact = Fact("foo", 1)
        facts.add(fact)
        val value = facts.get<Int?>("foo")
        assertEquals(value, 1)
    }

    @Test
    fun testGetFact() {
        val fact = Fact("foo", 1)
        facts.add(fact)
        val retrievedFact = facts.getFact("foo")
        assertEquals(retrievedFact, fact)
    }

    @Test
    fun testAsMap() {
        val fact1 = Fact("foo", 1)
        val fact2 = Fact("bar", 2)
        facts.add(fact1)
        facts.add(fact2)
        val map = facts.asMap()
        assertTrue(map.containsKey("foo"))
        assertTrue(map.containsKey("bar"))
        assertTrue(map.containsValue(1))
        assertTrue(map.containsValue(2))
    }

    @Test
    fun testClear() {
        val facts = Facts()
        facts.add(Fact("foo", 1))
        facts.clear()
        assertTrue(facts.asMap().isEmpty())
    }
}
