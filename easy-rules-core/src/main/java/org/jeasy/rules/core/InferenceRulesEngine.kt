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
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.*

/**
 * Inference [RulesEngine] implementation.
 *
 * Rules are selected based on given facts and fired according to their natural order which is priority by default.
 *
 * The engine continuously selects and fires rules until no more rules are applicable.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class InferenceRulesEngine<Facts>
/**
 * Create a new inference rules engine.
 *
 * @param parameters of the engine
 */
 constructor(
                          override val parameters: RulesEngineParameters = RulesEngineParametersImpl()) : RulesEngine<Facts> {

    override val ruleListeners: MutableList<RuleListener<Facts>>

    override val rulesEngineListeners: MutableList<RulesEngineListener<Facts>>
    private val delegate: DefaultRulesEngine<Facts>

    init {
        delegate = DefaultRulesEngine(parameters)
        ruleListeners = ArrayList()
        rulesEngineListeners = ArrayList()
    }


    override fun fire(rules: Rules<Facts>, facts: Facts) {
        var selectedRules: Set<Rule<Facts>>
        do {
            LOGGER.info{"Selecting candidate rules based on the following facts: $facts"}
            selectedRules = selectCandidates(rules, facts)
            if (!selectedRules.isEmpty()) {
                delegate.doFire(RulesImpl<Facts>(selectedRules), facts)
            } else {
                LOGGER.info{"No candidate rules found for facts: $facts" }
            }
        } while (!selectedRules.isEmpty())
    }

    private fun selectCandidates(rules: Rules<Facts>, facts: Facts): Set<Rule<Facts>> {
        val candidates = mutableSetOf<Rule<Facts>>()
        for (rule in rules) {
            if (rule.evaluate(facts)) {
                candidates.add(rule)
            }
        }
        return candidates.sorted().toSet()
    }


    override fun check(rules: Rules<Facts>, facts: Facts): Map<Rule<Facts>, Boolean> {
        return delegate.check(rules, facts)
    }

    /**
     * Register a rule listener.
     * @param ruleListener to register
     */
    fun registerRuleListener(ruleListener: RuleListener<Facts>) {
        ruleListeners.add(ruleListener)
        delegate.registerRuleListener(ruleListener)
    }

    /**
     * Register a list of rule listener.
     * @param ruleListeners to register
     */
    fun registerRuleListeners(ruleListeners: List<RuleListener<Facts>>) {
        this.ruleListeners.addAll(ruleListeners)
        delegate.registerRuleListeners(ruleListeners)
    }

    /**
     * Register a rules engine listener.
     * @param rulesEngineListener to register
     */
    fun registerRulesEngineListener(rulesEngineListener: RulesEngineListener<Facts>) {
        rulesEngineListeners.add(rulesEngineListener)
        delegate.registerRulesEngineListener(rulesEngineListener)
    }

    /**
     * Register a list of rules engine listener.
     * @param rulesEngineListeners to register
     */
    fun registerRulesEngineListeners(rulesEngineListeners: List<RulesEngineListener<Facts>>) {
        this.rulesEngineListeners.addAll(rulesEngineListeners)
        delegate.registerRulesEngineListeners(rulesEngineListeners)
    }

    companion object {

        private val LOGGER = KotlinLogging.logger {}
    }
}
/**
 * Create a new inference rules engine with default parameters.
 */
