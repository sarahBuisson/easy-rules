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

import org.jeasy.rules.api.RuleListener
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.api.RulesEngineListener
import org.jeasy.rules.api.RulesEngineParameters

/**
 * Base class for [RulesEngine] implementations.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class AbstractRulesEngine<FactType> internal constructor(protected var _parameters: RulesEngineParameters = RulesEngineParameters()) :
    RulesEngine<FactType> {
    protected var _ruleListeners = mutableListOf<RuleListener<FactType>>()
    protected var _rulesEngineListeners = mutableListOf<RulesEngineListener<FactType>> ()

    /**
     * Return a copy of the rules engine parameters.
     * @return copy of the rules engine parameters
     */
    override fun getParameters(): RulesEngineParameters {
        return RulesEngineParameters(
            _parameters.isSkipOnFirstAppliedRule(),
            _parameters.isSkipOnFirstFailedRule(),
            _parameters.isSkipOnFirstNonTriggeredRule(),
            _parameters.getPriorityThreshold()
        )
    }

    /**
     * Return an unmodifiable list of the registered rule listeners.
     * @return an unmodifiable list of the registered rule listeners
     */
    override fun getRuleListeners(): List<RuleListener<FactType>> {
        return _ruleListeners.toList()
    }

    /**
     * Return an unmodifiable list of the registered rules engine listeners
     * @return an unmodifiable list of the registered rules engine listeners
     */
    override fun getRulesEngineListeners(): List<RulesEngineListener<FactType>> {
        return _rulesEngineListeners.toList()
    }

    open fun registerRuleListener(ruleListener: RuleListener<FactType>) {
        _ruleListeners.add(ruleListener)
    }

    open fun registerRuleListeners(ruleListeners: List<RuleListener<FactType>>) {
        this._ruleListeners.addAll(ruleListeners)
    }

    open fun registerRulesEngineListener(rulesEngineListener: RulesEngineListener<FactType>) {
        _rulesEngineListeners.add(rulesEngineListener)
    }

    open fun registerRulesEngineListeners(rulesEngineListeners: List<RulesEngineListener<FactType>>) {
        this._rulesEngineListeners.addAll(rulesEngineListeners)
    }
}
