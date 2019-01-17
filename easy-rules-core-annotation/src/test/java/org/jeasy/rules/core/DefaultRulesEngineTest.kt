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
package org.jeasy.rules

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.RuleListener
import org.jeasy.rules.api.RulesEngineListener
import org.jeasy.rules.core.AbstractTest
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.core.RuleProxy
import kotlin.test.*

class DefaultRulesEngineTest : AbstractTest() {

    @MockK
    private lateinit var ruleListener: RuleListener

    @MockK
    private lateinit var rulesEngineListener: RulesEngineListener

    private lateinit var annotatedRule: AnnotatedRule

    @BeforeTest
    override fun setup() {
        super.setup()
        every { rule1.name } returns ("r")
        every { rule1.priority } returns (1)
        annotatedRule = AnnotatedRule()
    }

    @Test
    fun whenConditionIsTrue_thenActionShouldBeExecuted() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify { rule1.execute(facts) }
    }

    @Test
    fun whenConditionIsFalse_thenActionShouldNotBeExecuted() {
        // Given
        every { rule1.evaluate(facts) } returns (false)
        rules.register(rule1)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify(atMost = 0, atLeast = 0) { rule1.execute(facts) }
    }

    @Test
    fun rulesMustBeTriggeredInTheirNaturalOrder() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        every { rule2.evaluate(facts) } returns (true)
        every { rule2.compareTo(rule1) } returns (1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verifyOrder {
            rule1.execute(facts)
            rule2.execute(facts)
        }
    }

    @Test
    @Throws(Exception::class)
    fun rulesMustBeCheckedInTheirNaturalOrder() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        every { rule2.evaluate(facts) } returns (true)
        every { rule2.compareTo(rule1) } returns (1)
        rules.register(rule1)
        rules.register(rule2)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verifyOrder {
            rule1.evaluate(facts)
            rule2.evaluate(facts)
        }
    }

    @Test
    fun actionsMustBeExecutedInTheDefinedOrder() {
        // Given
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        assertEquals("012", annotatedRule!!.actionSequence)
    }

    @Test
    @Throws(Exception::class)
    fun annotatedRulesAndNonAnnotatedRulesShouldBeUsableTogether() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        rulesEngine.fire(rules, facts)

        // Then
        verify { rule1.execute(facts) }
        assertTrue(annotatedRule!!.isExecuted)
    }

    @Test
    @Throws(Exception::class)
    fun whenRuleNameIsNotSpecified_thenItShouldBeEqualToClassNameByDefault() {
        val rule = RuleProxy.asRule(DummyRule())
        assertEquals(rule.name, "DummyRule")
    }

    @Test
    @Throws(Exception::class)
    fun whenRuleDescriptionIsNotSpecified_thenItShouldBeEqualToConditionNameFollowedByActionsNames() {
        val rule = RuleProxy.asRule(DummyRule())
        assertEquals(rule.description, "when condition then action1,action2")
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRules() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        rules.register(rule1)
        rules.register(annotatedRule)

        // When
        val result = rulesEngine.check(rules, facts)

        // Then
        assertEquals(result.size, 2)
        for (r in rules) {
            assertTrue(result.get(r)!!)
        }
    }

    @Test
    @Throws(Exception::class)
    fun listenerShouldBeInvokedBeforeCheckingRules() {
        // Given
        every { rule1.evaluate(facts) } returns (true)
        every { ruleListener!!.beforeEvaluate(rule1, facts) } returns (true)
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)
        rules.register(rule1)

        // When
        rulesEngine.check(rules, facts)

        // Then
        verify { ruleListener.beforeEvaluate(rule1, facts) }
    }

    @Test
    fun nullFactsShouldNotCrashTheEngine() {
        // Given
        facts.put("foo", null)

        // When
        try {
            rulesEngine.fire(rules, facts)
        } catch (e: Exception) {
            e.printStackTrace()
            fail("Unable to fire rules on known facts" + e.message)
        }

        // Then
        // Should not throw exception
    }

    @Test
    @Throws(Exception::class)
    fun testGetRuleListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRuleListener(ruleListener)

        // When
        val ruleListeners = rulesEngine.ruleListeners

        // Then
        assertTrue(ruleListeners.contains(ruleListener))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRulesEngineListeners() {
        // Given
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.registerRulesEngineListener(rulesEngineListener)

        // When
        val rulesEngineListeners = rulesEngine.rulesEngineListeners

        // Then
        assertTrue(rulesEngineListeners.contains(rulesEngineListener))
    }

    @AfterTest
    fun clearRules() {
        rules.clear()
    }

    @org.jeasy.rules.annotation.Rule(name = "myRule", description = "my rule description")
    inner class AnnotatedRule {

        var isExecuted: Boolean = false
            private set

        var actionSequence = ""
            private set

        val priority: Int
            @Priority
            get() = 0

        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        @Throws(Exception::class)
        fun then0() {
            actionSequence += "0"
        }

        @Action(order = 1)
        @Throws(Exception::class)
        fun then1() {
            actionSequence += "1"
        }

        @Action(order = 2)
        @Throws(Exception::class)
        fun then2() {
            actionSequence += "2"
            isExecuted = true
        }

    }


    @org.jeasy.rules.annotation.Rule
    inner class DummyRule {

        @Condition
        fun condition(): Boolean {
            return true
        }

        @Action(order = 1)
        @Throws(Exception::class)
        fun action1() {
            // no op
        }

        @Action(order = 2)
        @Throws(Exception::class)
        fun action2() {
            // no op
        }
    }
}
