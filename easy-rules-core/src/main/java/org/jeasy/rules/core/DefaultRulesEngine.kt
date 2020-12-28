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

import org.jeasy.rules.api.*
import org.jeasy.rules.core.DefaultRulesEngine
import org.slf4j.LoggerFactory
import java.util.*
import java.util.function.Consumer

/**
 * Default [RulesEngine] implementation.
 *
 * Rules are fired according to their natural order which is priority by default.
 * This implementation iterates over the sorted set of rules, evaluates the condition
 * of each rule and executes its actions if the condition evaluates to true.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class DefaultRulesEngine : AbstractRulesEngine {
    /**
     * Create a new [DefaultRulesEngine] with default _parameters.
     */
    constructor() : super() {}

    /**
     * Create a new [DefaultRulesEngine].
     *
     * @param _parameters of the engine
     */
    constructor(_parameters: RulesEngineParameters) : super(_parameters) {}

    override fun fire(rules: Rules, facts: Facts) {
        triggerListenersBeforeRules(rules, facts)
        doFire(rules, facts)
        triggerListenersAfterRules(rules, facts)
    }

    fun doFire(rules: Rules, facts: Facts) {
        if (rules.isEmpty()) {
            LOGGER.warn("No rules registered! Nothing to apply")
            return
        }
        logEngineParameters()
        log(rules)
        log(facts)
        LOGGER.debug("Rules evaluation started")
        for (rule in rules) {
            val name = rule.name
            val priority = rule.priority
            if (priority > _parameters.getPriorityThreshold()) {
                LOGGER.debug(
                    "Rule priority threshold ({}) exceeded at rule '{}' with priority={}, next rules will be skipped",
                    _parameters.getPriorityThreshold(), name, priority
                )
                break
            }
            if (!shouldBeEvaluated(rule, facts)) {
                LOGGER.debug("Rule '{}' has been skipped before being evaluated", name)
                continue
            }
            var evaluationResult = false
            try {
                evaluationResult = rule.evaluate(facts)
            } catch (exception: RuntimeException) {
                LOGGER.error("Rule '$name' evaluated with error", exception)
                triggerListenersOnEvaluationError(rule, facts, exception)
                // give the option to either skip next rules on evaluation error or continue by considering the evaluation error as false
                if (_parameters.isSkipOnFirstNonTriggeredRule()) {
                    LOGGER.debug("Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set")
                    break
                }
            }
            if (evaluationResult) {
                LOGGER.debug("Rule '{}' triggered", name)
                triggerListenersAfterEvaluate(rule, facts, true)
                try {
                    triggerListenersBeforeExecute(rule, facts)
                    rule.execute(facts)
                    LOGGER.debug("Rule '{}' performed successfully", name)
                    triggerListenersOnSuccess(rule, facts)
                    if (_parameters.isSkipOnFirstAppliedRule()) {
                        LOGGER.debug("Next rules will be skipped since parameter skipOnFirstAppliedRule is set")
                        break
                    }
                } catch (exception: Exception) {
                    LOGGER.error("Rule '$name' performed with error", exception)
                    triggerListenersOnFailure(rule, exception, facts)
                    if (_parameters.isSkipOnFirstFailedRule()) {
                        LOGGER.debug("Next rules will be skipped since parameter skipOnFirstFailedRule is set")
                        break
                    }
                }
            } else {
                LOGGER.debug("Rule '{}' has been evaluated to false, it has not been executed", name)
                triggerListenersAfterEvaluate(rule, facts, false)
                if (_parameters.isSkipOnFirstNonTriggeredRule()) {
                    LOGGER.debug("Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set")
                    break
                }
            }
        }
    }

    private fun logEngineParameters() {
        LOGGER.debug("{}", _parameters)
    }

    private fun log(rules: Rules) {
        LOGGER.debug("Registered rules:")
        for (rule in rules) {
            LOGGER.debug(
                "Rule { name = '{}', description = '{}', priority = '{}'}",
                rule.name, rule.description, rule.priority
            )
        }
    }

    private fun log(facts: Facts) {
        LOGGER.debug("Known facts:")
        for (fact in facts) {
            LOGGER.debug("{}", fact)
        }
    }

    override fun check(rules: Rules, facts: Facts): MutableMap<Rule, Boolean> {
        triggerListenersBeforeRules(rules, facts)
        val result = doCheck(rules, facts)
        triggerListenersAfterRules(rules, facts)
        return result
    }

    private fun doCheck(rules: Rules, facts: Facts): MutableMap<Rule, Boolean> {
        LOGGER.debug("Checking rules")
        val result: MutableMap<Rule, Boolean> = HashMap()
        for (rule in rules) {
            if (shouldBeEvaluated(rule, facts)) {
                result[rule] = rule.evaluate(facts)
            }
        }
        return result
    }

    private fun triggerListenersOnFailure(rule: Rule, exception: Exception?, facts: Facts?) {
        _ruleListeners.forEach(Consumer { ruleListener: RuleListener ->
            ruleListener.onFailure(
                rule,
                facts,
                exception
            )
        })
    }

    private fun triggerListenersOnSuccess(rule: Rule, facts: Facts) {
        _ruleListeners.forEach(Consumer { ruleListener: RuleListener -> ruleListener.onSuccess(rule, facts) })
    }

    private fun triggerListenersBeforeExecute(rule: Rule, facts: Facts) {
        _ruleListeners.forEach(Consumer { ruleListener: RuleListener -> ruleListener.beforeExecute(rule, facts) })
    }

    private fun triggerListenersBeforeEvaluate(rule: Rule, facts: Facts): Boolean {
        return _ruleListeners.stream()
            .allMatch { ruleListener: RuleListener -> ruleListener.beforeEvaluate(rule, facts) }
    }

    private fun triggerListenersAfterEvaluate(rule: Rule?, facts: Facts, evaluationResult: Boolean) {
        _ruleListeners.forEach(Consumer { ruleListener: RuleListener ->
            ruleListener.afterEvaluate(
                rule,
                facts,
                evaluationResult
            )
        })
    }

    private fun triggerListenersOnEvaluationError(rule: Rule, facts: Facts, exception: Exception) {
        _ruleListeners.forEach(Consumer { ruleListener: RuleListener ->
            ruleListener.onEvaluationError(
                rule,
                facts,
                exception
            )
        })
    }

    private fun triggerListenersBeforeRules(rule: Rules, facts: Facts) {
        _rulesEngineListeners.forEach(Consumer { rulesEngineListener: RulesEngineListener ->
            rulesEngineListener.beforeEvaluate(
                rule,
                facts
            )
        })
    }

    private fun triggerListenersAfterRules(rule: Rules, facts: Facts) {
        _rulesEngineListeners.forEach(Consumer { rulesEngineListener: RulesEngineListener ->
            rulesEngineListener.afterExecute(
                rule,
                facts
            )
        })
    }

    private fun shouldBeEvaluated(rule: Rule, facts: Facts): Boolean {
        return triggerListenersBeforeEvaluate(rule, facts)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultRulesEngine::class.java)
    }
}
