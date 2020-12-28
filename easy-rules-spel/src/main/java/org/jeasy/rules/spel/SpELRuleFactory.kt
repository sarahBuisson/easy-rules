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
package org.jeasy.rules.spel

import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.support.AbstractRuleFactory
import org.jeasy.rules.support.RuleDefinition
import org.jeasy.rules.support.reader.RuleDefinitionReader
import org.springframework.expression.BeanResolver
import org.springframework.expression.ParserContext
import java.io.Reader

/**
 * Factory to create [SpELRule] instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class SpELRuleFactory : AbstractRuleFactory {
    private val reader: RuleDefinitionReader
    private lateinit var beanResolver: BeanResolver
    private lateinit var parserContext: ParserContext
    /**
     * Create a new [SpELRuleFactory] with a given reader.
     *
     * @param reader used to read rule definitions
     * @param parserContext used to parse SpEL expressions
     * @see YamlRuleDefinitionReader
     *
     * @see JsonRuleDefinitionReader
     */
    /**
     * Create a new [SpELRuleFactory] with a given reader.
     *
     * @param reader used to read rule definitions
     * @see YamlRuleDefinitionReader
     *
     * @see JsonRuleDefinitionReader
     */
    @JvmOverloads
    constructor(reader: RuleDefinitionReader, parserContext: ParserContext = ParserContext.TEMPLATE_EXPRESSION) {
        this.reader = reader
        this.parserContext = parserContext
    }

    /**
     * Create a new [SpELRuleFactory] with a given reader.
     *
     * @param reader used to read rule definitions
     * @param beanResolver used to resolve bean references in SpEL expressions
     * @see YamlRuleDefinitionReader
     *
     * @see JsonRuleDefinitionReader
     */
    constructor(reader: RuleDefinitionReader, beanResolver: BeanResolver) {
        this.reader = reader
        this.beanResolver = beanResolver
    }

    /**
     * Create a new [SpELRuleFactory] with a given reader.
     *
     * @param reader used to read rule definitions
     * @param parserContext used to parse SpEL expressions
     * @param beanResolver used to resolve bean references in SpEL expressions
     * @see YamlRuleDefinitionReader
     *
     * @see JsonRuleDefinitionReader
     */
    constructor(reader: RuleDefinitionReader, parserContext: ParserContext, beanResolver: BeanResolver) {
        this.reader = reader
        this.parserContext = parserContext
        this.beanResolver = beanResolver
    }

    /**
     * Create a new [SpELRule] from a Reader.
     *
     * The rule descriptor should contain a single rule definition.
     * If no rule definitions are found, a [IllegalArgumentException] will be thrown.
     * If more than a rule is defined in the descriptor, the first rule will be returned.
     *
     * @param ruleDescriptor descriptor of rule definition
     * @return a new rule
     * @throws Exception if unable to create the rule from the descriptor
     */
    @Throws(Exception::class)
    fun createRule(ruleDescriptor: Reader): Rule {
        val ruleDefinitions = reader.read(ruleDescriptor)
        require(!ruleDefinitions.isEmpty()) { "rule descriptor is empty" }
        return createRule(ruleDefinitions[0])
    }

    /**
     * Create a set of [SpELRule] from a Reader.
     *
     * @param rulesDescriptor descriptor of rule definitions
     * @return a set of rules
     * @throws Exception if unable to create rules from the descriptor
     */
    @Throws(Exception::class)
    fun createRules(rulesDescriptor: Reader): Rules {
        val rules = Rules()
        val ruleDefinitions = reader.read(rulesDescriptor)
        for (ruleDefinition in ruleDefinitions) {
            rules.register(createRule(ruleDefinition))
        }
        return rules
    }

    override fun createSimpleRule(ruleDefinition: RuleDefinition): Rule {
        val spELRule = SpELRule(parserContext, beanResolver)
            .name(ruleDefinition.name)
            .description(ruleDefinition.description)
            .priority(ruleDefinition.priority)
            .`when`(ruleDefinition.condition)
        for (action in ruleDefinition.actions) {
            spELRule!!.then(action)
        }
        return spELRule!!
    }
}
