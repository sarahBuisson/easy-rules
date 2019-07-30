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

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.DefaultRule
import org.jeasy.rules.core.RuleBuilder
import kotlin.test.Test

import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class RuleBuilderTest {

    @MockK//(relaxed = true)
    private lateinit var condition: Condition<FactsMap>
    @MockK//(relaxed = true)
    private lateinit var action1: Action<FactsMap>
    @MockK//(relaxed = true)
    private lateinit var action2: Action<FactsMap>


    @BeforeTest
    fun setup(){
        MockKAnnotations.init(this/*, relaxed = true*/)
    }

    @Test
    //@Throws(Exception::class)
    fun testDefaultRuleCreationWithDefaultValues() {
        // when
        val rule = RuleBuilder<FactsMap>().build()

        // then
        assertEquals(rule.name, Rule.DEFAULT_NAME)
        assertEquals(rule.description, Rule.DEFAULT_DESCRIPTION)
        assertEquals(rule.priority, Rule.DEFAULT_PRIORITY)
        assertTrue(rule is DefaultRule<FactsMap>)
    }

    @Test
    //@Throws(Exception::class)
    fun testDefaultRuleCreationWithCustomValues() {
        // when
        val rule = RuleBuilder<FactsMap>()
                .name("myRule")
                .description("myRuleDescription")
                .priority(3)
                .`when`(condition)
                .then(action1)
                .then(action2)
                .build()

        // then
        assertEquals(rule.name, "myRule")
        assertEquals(rule.description, "myRuleDescription")
        assertEquals(rule.priority, 3)
        assertTrue(rule is DefaultRule)
        assertTrue((rule as DefaultRule).condition == (condition))
        assertTrue(rule.actions.containsAll(listOf(action1, action2)))
        assertEquals(rule.actions.size, 2)
    }
}
