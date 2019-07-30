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
import org.jeasy.rules.annotation.AnnotatedRuleWithMetaRuleAnnotation
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rule
import kotlin.test.Test
import kotlin.test.*

class RuleProxyTest {

    @Test
    fun proxyingHappensEvenWhenRuleIsAnnotatedWithMetaRuleAnnotation() {
        // Given
        val rule = AnnotatedRuleWithMetaRuleAnnotation()

        // When
        val proxy = RuleProxy.asRule(rule)

        // Then
        assertNotNull(proxy.description)
        assertNotNull(proxy.name)
    }

    @Test
    fun asRuleForObjectThatImplementsRule() {
        val rule = BasicRule<FactsMap>()
        val proxy = RuleProxy.asRule(rule)

        assertNotNull(proxy.description)
        assertNotNull(proxy.name)
    }

    @Test
    fun asRuleForObjectThatHasProxied() {
        val rule = DummyRule()
        val proxy1 = RuleProxy.asRule(rule)
        val proxy2 = RuleProxy.asRule(proxy1)

        assertEquals(proxy1.description, proxy2.description)
        assertEquals(proxy1.name, proxy2.name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun asRuleForPojo() {
        val rule = Object()
        val proxy = RuleProxy.asRule(rule)
    }

    @Test
    fun invokeEquals() {

        val rule = DummyRule()
        val proxy1 = RuleProxy.asRule(rule)
        val proxy2 = RuleProxy.asRule(proxy1)
        val proxy3 = RuleProxy.asRule(proxy2)
        /**
         * @see Object.equals
         */
        assertEquals(rule, rule)
        assertEquals(proxy1, proxy1)
        assertEquals(proxy2, proxy2)
        assertEquals(proxy3, proxy3)
        /**
         * @see Object.equals
         */
        assertNotEquals<Any>(rule, proxy1)
        assertNotEquals<Any>(proxy1, rule)
        assertEquals(proxy1, proxy2)
        assertEquals(proxy2, proxy1)
        /**
         * @see Object.equals
         */
        assertEquals(proxy1, proxy2)
        assertEquals(proxy2, proxy3)
        assertEquals(proxy3, proxy1)
        /**
         * @see Object.equals
         */
        assertNotEquals<Any?>(rule, null)
        assertNotEquals<Any?>(proxy1, null)
        assertNotEquals<Any?>(proxy2, null)
        assertNotEquals<Any?>(proxy3, null)
    }


    @Test
    fun invokeHashCode() {

        val rule = DummyRule()
        val proxy1 = RuleProxy.asRule(rule)
        val proxy2 = RuleProxy.asRule(proxy1)
        /**
         * @see Object.hashCode rule1
         */
        assertEquals(proxy1.hashCode(), proxy1.hashCode())
        /**
         * @see Object.hashCode rule2
         */
        assertEquals(proxy1, proxy2)
        assertEquals(proxy1.hashCode(), proxy2.hashCode())
        /**
         * @see Object.hashCode rule3
         */
        assertNotEquals<Any>(rule, proxy1)
        assertNotEquals(rule.hashCode(), proxy1.hashCode())
    }

    @Test
    fun invokeToString() {

        val rule = DummyRule()
        val proxy1 = RuleProxy.asRule(rule)
        val proxy2 = RuleProxy.asRule(proxy1)

        assertEquals(proxy1.toString(), proxy1.toString())

        assertEquals(proxy1.toString(), proxy2.toString())

        assertEquals(rule.toString(), proxy1.toString())
    }

    @Test
    fun testPriorityFromAnnotation() {

        @org.jeasy.rules.annotation.Rule(priority = 1)
        class MyRule {
            @Condition
            fun `when` (): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule = MyRule()
        val proxy = RuleProxy.asRule(rule)
        assertEquals(1, proxy.priority)
    }

    @Test
    fun testPriorityFromMethod() {

        @org.jeasy.rules.annotation.Rule
        class MyRule {

                @Priority
                fun get(): Int = 2

            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule = MyRule()
        val proxy = RuleProxy.asRule(rule)
        assertEquals(2, proxy.priority)
    }

    @Test
    fun testPriorityPrecedence() {

        @org.jeasy.rules.annotation.Rule(priority = 1)
        class MyRule {


            @Priority
            public fun getPriority():Int = 2

            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule = MyRule()
        val proxy = RuleProxy.asRule(rule)
        assertEquals(2, proxy.priority)
    }

    @Test
    fun testDefaultPriority() {

        @org.jeasy.rules.annotation.Rule
        class MyRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule = MyRule()
        val proxy = RuleProxy.asRule(rule)
        assertEquals(Rule.DEFAULT_PRIORITY, proxy.priority)
    }

    @org.jeasy.rules.annotation.Rule
    internal inner class DummyRule {
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
        }


        override fun toString(): String {
            return "I am a Dummy rule"
        }

    }

}
