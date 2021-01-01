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

import io.mockk.MockKAnnotations
import org.jeasy.rules.api.*
import kotlin.test.Test
import io.mockk.impl.annotations.MockK
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class RuleBuilderTest {
    @MockK
    private lateinit var condition: Condition

    @MockK
    private lateinit var action1: Action

    @MockK
    private lateinit var action2: Action

    @BeforeTest
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun testDefaultRuleCreationWithDefaultValues() {
        // when
        val rule = RuleBuilder().build()

        // then
        assertEquals(rule.name,Rule.Companion.DEFAULT_NAME)
        assertEquals(rule.description,Rule.Companion.DEFAULT_DESCRIPTION)
        assertEquals(rule.priority,Rule.Companion.DEFAULT_PRIORITY)
    }

    @Test
    fun testDefaultRuleCreationWithCustomValues() {
        // when
        val rule = RuleBuilder()
            .name("myRule")
            .description("myRuleDescription")
            .priority(3)
            .`when`(condition)
            .then(action1)
            .then(action2)
            .build()

        // then
        assertEquals(rule.name,"myRule")
        assertEquals(rule.description,"myRuleDescription")
        assertEquals(rule.priority,3)
        //assertEquals(rule).isInstanceOf(DefaultRule::class.java)
        //assertSame(rule.condition extracting("condition",condition))
        //assertTrue(rule.extracting("actions").asList().containsExactly(action1, action2)
    }
}
