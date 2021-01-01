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
package org.jeasy.rules.support

import mu.KotlinLogging
import org.jeasy.rules.api.Rule
import org.jeasy.rules.support.AbstractRuleFactory
import org.jeasy.rules.support.composite.ActivationRuleGroup
import org.jeasy.rules.support.composite.CompositeRule
import org.jeasy.rules.support.composite.ConditionalRuleGroup
import org.jeasy.rules.support.composite.UnitRuleGroup

/**
 * Base class for rule factories.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class AbstractRuleFactory<FactType> {
    protected fun createRule(ruleDefinition: RuleDefinition): Rule<FactType> {
        return if (ruleDefinition.isCompositeRule()) {
            createCompositeRule(ruleDefinition)
        } else {
            createSimpleRule(ruleDefinition)
        }
    }

    protected abstract fun createSimpleRule(ruleDefinition: RuleDefinition): Rule<FactType>
    protected fun createCompositeRule(ruleDefinition: RuleDefinition): Rule<FactType> {
        if (ruleDefinition.condition != null) {
            LOGGER.warn {
                "Condition '${ruleDefinition.condition}' in composite rule '${ruleDefinition.name}' of type ${ruleDefinition.compositeRuleType} will be ignored."

            }
        }
        if (ruleDefinition.actions != null && !ruleDefinition.actions.isEmpty()) {
            LOGGER.warn {
                "Actions '${ruleDefinition.actions}' in composite rule '${ruleDefinition.name}' of type ${ruleDefinition.compositeRuleType} will be ignored."

            }
        }
        val compositeRule: CompositeRule<FactType>
        val name = ruleDefinition.name
        compositeRule = when (ruleDefinition.compositeRuleType) {
            "UnitRuleGroup" -> UnitRuleGroup<FactType>(name)
            "ActivationRuleGroup" -> ActivationRuleGroup<FactType>(name)
            "ConditionalRuleGroup" -> ConditionalRuleGroup<FactType>(name)
            else -> throw IllegalArgumentException("Invalid composite rule type, must be one of " + ALLOWED_COMPOSITE_RULE_TYPES)
        }
        compositeRule.description = (ruleDefinition.description)
        compositeRule.priority = (ruleDefinition.priority)
        for (composingRuleDefinition in ruleDefinition.composingRules) {
            compositeRule.addRule(createRule(composingRuleDefinition))
        }
        return compositeRule
    }

    companion object {
        private val LOGGER = KotlinLogging.logger { }
        private val ALLOWED_COMPOSITE_RULE_TYPES = listOf(
            "UnitRuleGroup",
            "ConditionalRuleGroup",
            "ActivationRuleGroup"
        )
    }
}
