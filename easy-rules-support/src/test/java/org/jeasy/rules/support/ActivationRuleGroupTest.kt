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
package org.jeasy.rules.support

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActivationRuleGroupTest {

    private val facts = FactsMap()
    private val rules = Rules<FactsMap>()

    private val rulesEngine = DefaultRulesEngine<FactsMap>()

    @Test
    fun onlySelectedRuleShouldBeExecuted_whenComposingRulesHaveDifferentPriorities() {
        // given
        val rule1 = Rule1()
        val rule2 = Rule2()
        val activationRuleGroup = ActivationRuleGroup<FactsMap>("my activation rule", "rule1 xor rule2")
        activationRuleGroup.addRule(rule1)
        activationRuleGroup.addRule(rule2)
        rules.register(activationRuleGroup)

        // when
        rulesEngine.fire(rules, facts)

        // then
        assertTrue(rule1.isExecuted)
        assertFalse(rule2.isExecuted)
    }

    @Test
    fun onlySelectedRuleShouldBeExecuted_whenComposingRulesHaveSamePriority() {
        // given
        val rule2 = Rule2()
        val rule3 = Rule3()
        val activationRuleGroup = ActivationRuleGroup<FactsMap>("my activation rule", "rule2 xor rule3")
        activationRuleGroup.addRule(rule2)
        activationRuleGroup.addRule(rule3)
        rules.register(activationRuleGroup)

        // when
        rulesEngine.fire(rules, facts)

        // then
        // we don't know upfront which rule will be selected, but only one of them should be executed
        if (rule2.isExecuted) {
            assertFalse(rule3.isExecuted)
        } else {
            assertTrue(rule3.isExecuted)
        }
    }

    @org.jeasy.rules.annotation.Rule(priority = 1)
    inner class Rule1 {
        var isExecuted: Boolean = false
            private set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }
    }

    @org.jeasy.rules.annotation.Rule(priority = 2)
    inner class Rule2 {
        var isExecuted: Boolean = false
            private set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }
    }

    @org.jeasy.rules.annotation.Rule(priority = 2)
    inner class Rule3 {
        var isExecuted: Boolean = false
            private set

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
            isExecuted = true
        }
    }
}
