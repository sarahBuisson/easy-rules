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

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.core.BasicRule
import org.jeasy.rules.core.RuleProxy
import java.util.HashSet
import kotlin.test.*

class RulesTest {

    private var rules = Rules()


    @BeforeTest
    fun setup(){
        rules = Rules()
    }

    @Test
    @Throws(Exception::class)
    fun register() {
        rules.register(DummyRule())

        assertEquals(rules.size,1)
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustHaveUniqueName() {
        val r1 = BasicRule("rule")
        val r2 = BasicRule("rule")
        val ruleSet = mutableSetOf<Rule>()
        ruleSet.add(r1)
        ruleSet.add(r2)

        rules = Rules(ruleSet)

        assertEquals(rules.size,1)
    }

    @Test
    @Throws(Exception::class)
    fun unregister() {
        val rule = DummyRule()
        rules.register(rule)
        rules.unregister(rule)

        assertTrue(rules.isEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun unregisterByName() {
        val r1 = BasicRule("rule1")
        val r2 = BasicRule("rule2")
        val ruleSet = mutableSetOf<Rule>()
        ruleSet.add(r1)
        ruleSet.add(r2)

        rules = Rules(ruleSet)
        rules.unregister("rule2")
        assertEquals(rules.size,1)
        assertTrue(rules.contains(r1))
    }

    @Test
    @Throws(Exception::class)
    fun unregisterByNameNonExistingRule() {
        val r1 = BasicRule("rule1")
        val ruleSet = HashSet<Rule>()
        ruleSet.add(r1)

        rules = Rules(ruleSet)
        rules.unregister("rule2")


        assertEquals(rules.size,1)
        assertTrue(rules.contains(r1))
    }

    @Test
    @Throws(Exception::class)
    fun isEmpty() {
        assertTrue (rules.isEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun clear() {
        rules.register(DummyRule())
        rules.clear()

        assertTrue (rules.isEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun sort() {
        val r1 = BasicRule("rule", "", 1)
        val r2 = BasicRule("rule", "", Integer.MAX_VALUE)
        val r3 = DummyRule()

        rules.register(r3)
        rules.register(r1)
        rules.register(r2)

        assertEquals(rules[0],r1)
        assertEquals<Any>(rules[1].name, "DummyRule")//r3
        assertEquals(rules[2],r2)
    }
    /* //TODO: useless
        @Test(expected = NullPointerException::class)
        @Throws(Exception::class)
        fun whenRegisterNullRule_thenShouldThrowNullPointerException() {
            rules.register(null)
        }

        @Test(expected = NullPointerException::class)
        @Throws(Exception::class)
        fun whenUnregisterNullRule_thenShouldThrowNullPointerException() {
            rules.unregister(null)
        }
    */
    @org.jeasy.rules.annotation.Rule()
    internal inner class DummyRule {
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
        }
    }

}