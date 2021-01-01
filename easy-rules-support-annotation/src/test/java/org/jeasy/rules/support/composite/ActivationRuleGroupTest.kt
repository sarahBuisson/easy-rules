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
package org.jeasy.rules.support.composite

import org.assertj.core.api.Assertions
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Rule
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.BasicRule
import org.jeasy.rules.core.DefaultRulesEngine
import kotlin.test.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ActivationRuleGroupTest {
    private val facts: FactType = Facts()
    private val rules: Rules = Rules()
    private val rulesEngine: DefaultRulesEngine  = DefaultRulesEngine()

    @Test
    fun onlySelectedRuleShouldBeExecuted_whenComposingRulesHaveDifferentPriorities() {
        // given
        val rule1 = Rule1()
        val rule2 = Rule2()
        val activationRuleGroup = ActivationRuleGroup("my activation rule", "rule1 xor rule2")
        activationRuleGroup.addRule(rule1)
        activationRuleGroup.addRule(rule2)
        rules.register(activationRuleGroup)

        // when
        rulesEngine.fire(rules, facts)

        // then
        assertTrue(rule1.isExecuted()).isTrue
        assertTrue(rule2.isExecuted()).isFalse
    }

    @Test
    fun onlySelectedRuleShouldBeExecuted_whenComposingRulesHaveSamePriority() {
        // given
        val rule2 = Rule2()
        val rule3 = Rule3()
        val activationRuleGroup = ActivationRuleGroup("my activation rule", "rule2 xor rule3")
        activationRuleGroup.addRule(rule2)
        activationRuleGroup.addRule(rule3)
        rules.register(activationRuleGroup)

        // when
        rulesEngine.fire(rules, facts)

        // then
        // we don't know upfront which rule will be selected, but only one of them should be executed
        if (rule2.isExecuted()) {
            assertTrue(rule3.isExecuted()).isFalse
        } else {
            assertTrue(rule3.isExecuted()).isTrue
        }
    }

    @Test
    fun whenNoSelectedRule_thenNothingShouldHappen() {
        // given
        val rule4 = Rule4()
        val activationRuleGroup = ActivationRuleGroup("my activation rule", "rule4")
        activationRuleGroup.addRule(rule4)

        //when
        rules.register(activationRuleGroup)

        //then
        rulesEngine.fire(rules, facts)

        // rule4 will not be selected, so it should not be executed
        assertTrue(rule4.isExecuted()).isFalse
    }

    @Rule(priority = 1)
    class Rule1 : BasicRule() {
        private var executed = false

        override fun evaluate(facts: FactType): Boolean {
            return true
        }

        override fun execute(facts: FactType) {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule(priority = 2)
    class Rule2 : BasicRule() {
        private var executed = false

        override fun evaluate(facts: FactType): Boolean {
            return true
        }

        override fun execute(facts: FactType) {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule(priority = 2)
    class Rule3 : BasicRule() {
        private var executed = false

        override fun evaluate(facts: FactType): Boolean {
            return true
        }

        override fun execute(facts: FactType) {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }

    @Rule(priority = 1)
    class Rule4 : BasicRule()  {
        private var executed = false

        override fun evaluate(facts: FactType): Boolean {
            return false
        }

        override fun execute(facts: FactType) {
            executed = true
        }

        fun isExecuted(): Boolean {
            return executed
        }
    }
}
