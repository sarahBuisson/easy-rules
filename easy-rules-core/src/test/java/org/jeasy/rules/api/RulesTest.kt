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

import org.assertj.core.api.Assertions
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.core.BasicRule
import org.junit.Test
import java.util.*

class RulesTest {
    private var rules: Rules = Rules()

    @Test
    fun rulesMustHaveUniqueName() {
        val r1: Rule = BasicRule("rule")
        val r2: Rule = BasicRule("rule")
        val ruleSet: MutableSet<Rule> = HashSet()
        ruleSet.add(r1)
        ruleSet.add(r2)
        rules = Rules(ruleSet)
        Assertions.assertThat(rules).hasSize(1)
    }

    @Test
    fun isEmpty() {
        Assertions.assertThat(rules.isEmpty()).isTrue
    }

    @org.jeasy.rules.annotation.Rule
    internal class DummyRule {
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
        }
    }
}
