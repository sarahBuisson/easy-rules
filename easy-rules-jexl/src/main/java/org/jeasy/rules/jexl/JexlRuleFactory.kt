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
package org.jeasy.rules.jexl

import org.apache.commons.jexl3.JexlEngine
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.jexl.JexlRule
import org.jeasy.rules.support.AbstractRuleFactory
import org.jeasy.rules.support.RuleDefinition
import org.jeasy.rules.support.reader.RuleDefinitionReader
import java.io.Reader
import java.util.*

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
class JexlRuleFactory @JvmOverloads constructor(
    reader: RuleDefinitionReader?,
    jexl: JexlEngine? = JexlRule.Companion.DEFAULT_JEXL
) : AbstractRuleFactory() {
    private val reader: RuleDefinitionReader
    private val jexl: JexlEngine
    @Throws(Exception::class)
    fun createRule(ruleDescriptor: Reader): Rule {
        Objects.requireNonNull(ruleDescriptor, "ruleDescriptor cannot be null")
        Objects.requireNonNull(jexl, "jexl cannot be null")
        val ruleDefinitions = reader.read(ruleDescriptor)
        require(!ruleDefinitions.isEmpty()) { "rule descriptor is empty" }
        return createRule(ruleDefinitions[0])
    }

    @Throws(Exception::class)
    fun createRules(rulesDescriptor: Reader): Rules {
        Objects.requireNonNull(rulesDescriptor, "rulesDescriptor cannot be null")
        val rules = Rules()
        val ruleDefinitions = reader.read(rulesDescriptor)
        for (ruleDefinition in ruleDefinitions) {
            rules.register(createRule(ruleDefinition))
        }
        return rules
    }

    override fun createSimpleRule(ruleDefinition: RuleDefinition): Rule {
        Objects.requireNonNull(ruleDefinition, "ruleDefinition cannot be null")
        val rule = JexlRule(jexl)
            .name(ruleDefinition.name)
            .description(ruleDefinition.description)
            .priority(ruleDefinition.priority)
            .`when`(ruleDefinition.condition)
        for (action in ruleDefinition.actions) {
            rule.then(action)
        }
        return rule
    }

    init {
        this.reader = Objects.requireNonNull(reader, "reader cannot be null")!!
        this.jexl = Objects.requireNonNull(jexl, "Jexl Engine cannot be null")!!
    }
}
