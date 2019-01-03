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

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import java.util.Arrays.asList
import org.assertj.core.api.Assertions.assertThat

@RunWith(MockitoJUnitRunner::class)
class RuleBuilderTest {

    @Mock
    private lateinit var condition: Condition
    @Mock
    private lateinit var action1: Action
    @Mock
    private lateinit var action2: Action

    @Test
    @Throws(Exception::class)
    fun testDefaultRuleCreationWithDefaultValues() {
        // when
        val rule = RuleBuilder().build()

        // then
        assertThat(rule.name).isEqualTo(Rule.DEFAULT_NAME)
        assertThat(rule.description).isEqualTo(Rule.DEFAULT_DESCRIPTION)
        assertThat(rule.priority).isEqualTo(Rule.DEFAULT_PRIORITY)
        assertThat(rule).isInstanceOf(DefaultRule::class.java)
    }

    @Test
    @Throws(Exception::class)
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
        assertThat(rule.name).isEqualTo("myRule")
        assertThat(rule.description).isEqualTo("myRuleDescription")
        assertThat(rule.priority).isEqualTo(3)
        assertThat(rule).isInstanceOf(DefaultRule::class.java)
        assertThat(rule).extracting("condition").containsExactly(condition)
        assertThat(rule).extracting("actions").containsExactly(asList(action1, action2))
    }
}
