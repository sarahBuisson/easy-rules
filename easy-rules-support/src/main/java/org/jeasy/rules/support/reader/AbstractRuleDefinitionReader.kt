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
package org.jeasy.rules.support.reader

import org.jeasy.rules.api.Rule
import org.jeasy.rules.support.RuleDefinition
import java.io.Reader
import java.util.*

/**
 * Base class for [RuleDefinitionReader]s.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class AbstractRuleDefinitionReader : RuleDefinitionReader {
    @Throws(Exception::class)
    override fun read(reader: Reader): MutableList<RuleDefinition> {
        val ruleDefinitions: MutableList<RuleDefinition> = ArrayList()
        val rules = loadRules(reader)
        for (rule in rules) {
            ruleDefinitions.add(createRuleDefinition(rule))
        }
        return ruleDefinitions
    }

    /**
     * Load rules from the given reader as an iterable of Maps.
     *
     * @param reader to read rules from
     * @return an iterable of rule Maps
     * @throws Exception if unable to load rules
     */
    @Throws(Exception::class)
    protected abstract fun loadRules(reader: Reader): Iterable<MutableMap<String, Any>>

    /**
     * Create a rule definition.
     *
     * @param map of rule properties
     * @return a rule definition
     */
    protected fun createRuleDefinition(map: MutableMap<String, Any>): RuleDefinition {
        val ruleDefinition = RuleDefinition()
        val name = map.get("name") as String?
        ruleDefinition.name = name ?: Rule.DEFAULT_NAME
        val description = map.get("description") as String?
        ruleDefinition.description = description ?: Rule.DEFAULT_DESCRIPTION
        val priority = map.get("priority") as Int?
        ruleDefinition.priority = priority ?: Rule.DEFAULT_PRIORITY
        val compositeRuleType = map.get("compositeRuleType") as String?
        val condition = map.get("condition") as String?
        require(!(condition == null && compositeRuleType == null)) { "The rule condition must be specified" }
        ruleDefinition.condition = condition!!
        val actions = map.get("actions") as MutableList<String>?
        require(!((actions == null || actions.isEmpty()) && compositeRuleType == null)) { "The rule action(s) must be specified" }
        ruleDefinition.actions = actions!!
        val composingRules = map.get("composingRules") as MutableList<Any?>?
        require(!(composingRules != null && !composingRules.isEmpty() && compositeRuleType == null)) { "Non-composite rules cannot have composing rules" }
        require(!((composingRules == null || composingRules.isEmpty()) && compositeRuleType != null)) { "Composite rules must have composing rules specified" }
        return ruleDefinition
    }
}
