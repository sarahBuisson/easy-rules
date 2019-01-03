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
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.annotation.Rule
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat
import org.jeasy.rules.annotation.Fact

class AnnotationInheritanceTest : AbstractTest() {

    @Test
    @Throws(Exception::class)
    fun annotationsShouldBeInherited() {
        // Given
        val myChildRule = MyChildRule()
        rules.register(myChildRule)
        // When
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        // Then
        assertThat(myChildRule.isExecuted).isTrue()
    }

    @Rule
    open internal  class MyBaseRule {
        var isExecuted: Boolean = false
            protected set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }

        fun thsssen() {
            isExecuted = true
        }
    }

    open internal  class MyChildRule : MyBaseRule(){
        fun thsssddden() {
            isExecuted = true
        }

    }
}
