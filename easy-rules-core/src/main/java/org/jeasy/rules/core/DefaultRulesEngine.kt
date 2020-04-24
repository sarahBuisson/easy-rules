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

import mu.KotlinLogging
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.*

/**
 * Default [RulesEngine] implementation.
 *
 * This implementation handles a set of rules with unique name.
 *
 * Rules are fired according to their natural order which is priority by default.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class DefaultRulesEngine<Facts> : RulesEngine<Facts> {
    /**
     * Create a new [DefaultRulesEngine].
     *
     * @param parameters of the engine
     */


    override val parameters: RulesEngineParameters


    override val ruleListeners: MutableList<RuleListener<Facts>>

    override val rulesEngineListeners: MutableList<RulesEngineListener<Facts>>


    constructor() {
        this.parameters = RulesEngineParametersImpl()
        this.ruleListeners = mutableListOf()
        this.ruleListeners.add(DefaultRuleListener())
        this.rulesEngineListeners = ArrayList()
        this.rulesEngineListeners.add(DefaultRulesEngineListener(parameters))
    }

    constructor(parameters: RulesEngineParameters) {
        this.parameters = parameters
        this.ruleListeners = mutableListOf()
        this.ruleListeners.add(DefaultRuleListener())
        this.rulesEngineListeners = ArrayList()
        this.rulesEngineListeners.add(DefaultRulesEngineListener(parameters))
    }


    override fun fire(rules: Rules<Facts>, facts: Facts) {
        triggerListenersBeforeRules(rules, facts)
        doFire(rules, facts)
        triggerListenersAfterRules(rules, facts)
    }

    internal fun doFire(rules: Rules<Facts>, facts: Facts) {
        for (rule in rules) {
            val name = rule.name
            val priority = rule.priority
            if (priority > parameters.priorityThreshold) {
                LOGGER.info { "Rule priority threshold (${parameters.priorityThreshold}) exceeded at rule '$name' with priority=$priority, next rules will be skipped" }
                break
            }
            if (!shouldBeEvaluated(rule, facts)) {
                LOGGER.info { "Rule '$name' has been skipped before being evaluated" }
                continue
            }
            if (rule.evaluate(facts)) {
                triggerListenersAfterEvaluate(rule, facts, true)
                try {
                    triggerListenersBeforeExecute(rule, facts)
                    rule.execute(facts)
                    triggerListenersOnSuccess(rule, facts)
                    if (parameters.isSkipOnFirstAppliedRule) {
                        LOGGER.info { "Next rules will be skipped since parameter skipOnFirstAppliedRule is set" }
                        break
                    }
                } catch (exception: Exception) {
                    triggerListenersOnFailure(rule, exception, facts)
                    if (parameters.isSkipOnFirstFailedRule) {
                        LOGGER.info { "Next rules will be skipped since parameter skipOnFirstFailedRule is set" }
                        break
                    }
                }

            } else {
                triggerListenersAfterEvaluate(rule, facts, false)
                if (parameters.isSkipOnFirstNonTriggeredRule) {
                    LOGGER.info { "Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set" }
                    break
                }
            }
        }
    }


    override fun check(rules: Rules<Facts>, facts: Facts): Map<Rule<Facts>, Boolean> {
        triggerListenersBeforeRules(rules, facts)
        val result = doCheck(rules, facts)
        triggerListenersAfterRules(rules, facts)
        return result
    }

    private fun doCheck(rules: Rules<Facts>, facts: Facts): Map<Rule<Facts>, Boolean> {
        LOGGER.info { "Checking rules" }
        val result = HashMap<Rule<Facts>, Boolean>()
        for (rule in rules) {
            if (shouldBeEvaluated(rule, facts)) {
                result.put(rule, rule.evaluate(facts))
            }
        }
        return result
    }

    private fun triggerListenersOnFailure(rule: Rule<Facts>, exception: Exception, facts: Facts) {
        for (ruleListener in ruleListeners) {
            ruleListener.onFailure(rule, facts, exception)
        }
    }

    private fun triggerListenersOnSuccess(rule: Rule<Facts>, facts: Facts) {
        for (ruleListener in ruleListeners) {
            ruleListener.onSuccess(rule, facts)
        }
    }

    private fun triggerListenersBeforeExecute(rule: Rule<Facts>, facts: Facts) {
        for (ruleListener in ruleListeners) {
            ruleListener.beforeExecute(rule, facts)
        }
    }

    private fun triggerListenersBeforeEvaluate(rule: Rule<Facts>, facts: Facts): Boolean {
        for (ruleListener in ruleListeners) {
            if (!ruleListener.beforeEvaluate(rule, facts)) {
                return false
            }
        }
        return true
    }

    private fun triggerListenersAfterEvaluate(rule: Rule<Facts>, facts: Facts, evaluationResult: Boolean) {
        for (ruleListener in ruleListeners) {
            ruleListener.afterEvaluate(rule, facts, evaluationResult)
        }
    }

    private fun triggerListenersBeforeRules(rule: Rules<Facts>, facts: Facts) {
        for (rulesEngineListener in rulesEngineListeners) {
            rulesEngineListener.beforeEvaluate(rule, facts)
        }
    }

    private fun triggerListenersAfterRules(rule: Rules<Facts>, facts: Facts) {
        for (rulesEngineListener in rulesEngineListeners) {
            rulesEngineListener.afterExecute(rule, facts)
        }
    }

    private fun shouldBeEvaluated(rule: Rule<Facts>, facts: Facts): Boolean {
        return triggerListenersBeforeEvaluate(rule, facts)
    }

    /**
     * Register a rule listener.
     * @param ruleListener to register
     */
    fun registerRuleListener(ruleListener: RuleListener<Facts>) {
        ruleListeners.add(ruleListener)
    }

    /**
     * Register a list of rule listener.
     * @param ruleListeners to register
     */
    fun registerRuleListeners(ruleListeners: List<RuleListener<Facts>>) {
        this.ruleListeners.addAll(ruleListeners)
    }

    /**
     * Register a rules engine listener.
     * @param rulesEngineListener to register
     */
    fun registerRulesEngineListener(rulesEngineListener: RulesEngineListener<Facts>) {
        rulesEngineListeners.add(rulesEngineListener)
    }

    /**
     * Register a list of rules engine listener.
     * @param rulesEngineListeners to register
     */
    fun registerRulesEngineListeners(rulesEngineListeners: List<RulesEngineListener<Facts>>) {
        this.rulesEngineListeners.addAll(rulesEngineListeners)
    }

    companion object {

        private val LOGGER = KotlinLogging.logger {}
    }
}
/**
 * Create a new [DefaultRulesEngine] with default parameters.
 */
