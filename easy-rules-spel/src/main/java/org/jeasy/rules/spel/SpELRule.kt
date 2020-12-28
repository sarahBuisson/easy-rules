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

import org.jeasy.rules.api.*
import org.jeasy.rules.core.BasicRule
import org.springframework.expression.BeanResolver
import org.springframework.expression.ParserContext
import java.util.*

/**
 * A [Rule] implementation that uses
 * [SpEL](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)
 * to evaluate and execute the rule.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class SpELRule : BasicRule {
    private var condition = Condition.FALSE
    private val actions: MutableList<Action> = ArrayList()
    private val parserContext: ParserContext
    private lateinit var beanResolver: BeanResolver
    /**
     * Create a new SpEL rule.
     *
     * @param parserContext used when parsing expressions
     */
    /**
     * Create a new SpEL rule.
     */
    @JvmOverloads
    constructor(parserContext: ParserContext = ParserContext.TEMPLATE_EXPRESSION) : super(
        Rule.DEFAULT_NAME,
        Rule.DEFAULT_DESCRIPTION,
        Rule.DEFAULT_PRIORITY
    ) {
        this.parserContext = parserContext
    }

    /**
     * Create a new SpEL rule.
     *
     * @param beanResolver used to resolve bean references in expressions
     */
    constructor(beanResolver: BeanResolver) : super(
        Rule.DEFAULT_NAME,
        Rule.DEFAULT_DESCRIPTION,
        Rule.DEFAULT_PRIORITY
    ) {
        parserContext = ParserContext.TEMPLATE_EXPRESSION
        this.beanResolver = beanResolver
    }

    /**
     * Create a new SpEL rule.
     *
     * @param parserContext used when parsing expressions
     * @param beanResolver used to resolve bean references in expressions
     */
    constructor(parserContext: ParserContext, beanResolver: BeanResolver) : super(
        Rule.DEFAULT_NAME,
        Rule.DEFAULT_DESCRIPTION,
        Rule.DEFAULT_PRIORITY
    ) {
        this.parserContext = parserContext
        this.beanResolver = beanResolver
    }

    /**
     * Set rule name.
     *
     * @param name of the rule
     * @return this rule
     */
    fun name(name: String): SpELRule {
        this.name = name
        return this
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return this rule
     */
    fun description(description: String): SpELRule {
        this.description = description
        return this
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return this rule
     */
    fun priority(priority: Int): SpELRule {
        this.priority = priority
        return this
    }

    /**
     * Specify the rule's condition as SpEL expression.
     * @param condition of the rule
     * @return this rule
     */
    fun `when`(condition: String?): SpELRule {
        this.condition = SpELCondition(condition, parserContext, beanResolver)
        return this
    }

    /**
     * Add an action specified as an SpEL expression to the rule.
     * @param action to add to the rule
     * @return this rule
     */
    fun then(action: String): SpELRule {
        actions.add(SpELAction(action, parserContext, beanResolver))
        return this
    }

    override fun evaluate(facts: Facts): Boolean {
        return condition!!.evaluate(facts)
    }

    @Throws(Exception::class)
    override fun execute(facts: Facts) {
        for (action in actions) {
            action.execute(facts)
        }
    }
}
