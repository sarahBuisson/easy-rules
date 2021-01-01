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

import mu.KotlinLogging
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Facts
import org.jeasy.rules.spel.SpELAction
import org.springframework.expression.BeanResolver
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.ParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * This class is an implementation of [Action] that uses
 * [SpEL](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)
 * to execute the action.
 *
 * Each fact is set as a variable in the [org.springframework.expression.EvaluationContext].
 *
 * The facts map is set as the root object of the [org.springframework.expression.EvaluationContext].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class SpELAction : Action<Facts> {
    private val parser: ExpressionParser = SpelExpressionParser()
    private val expression: String
    private val compiledExpression: Expression
    private lateinit var beanResolver: BeanResolver

    /**
     * Create a new [SpELAction].
     *
     * @param expression    the action written in expression language
     * @param beanResolver  the bean resolver used to resolve bean references
     */
    constructor(expression: String, beanResolver: BeanResolver) : this(
        expression,
        ParserContext.TEMPLATE_EXPRESSION,
        beanResolver
    ) {
    }
    /**
     * Create a new [SpELAction].
     *
     * @param expression the action written in expression language
     * @param parserContext the SpEL parser context
     */
    /**
     * Create a new [SpELAction].
     *
     * @param expression the action written in expression language
     */
    @JvmOverloads
    constructor(expression: String, parserContext: ParserContext? = ParserContext.TEMPLATE_EXPRESSION) {
        this.expression = expression
        compiledExpression = parser.parseExpression(expression, parserContext)
    }

    /**
     * Create a new [SpELAction].
     *
     * @param expression    the action written in expression language
     * @param beanResolver  the bean resolver used to resolve bean references
     * @param parserContext the SpEL parser context
     */
    constructor(expression: String, parserContext: ParserContext, beanResolver: BeanResolver) {
        this.expression = expression
        this.beanResolver = beanResolver
        compiledExpression = parser.parseExpression(expression, parserContext)
    }

    override fun execute(facts: Facts) {
        try {
            val context = StandardEvaluationContext()
            context.setRootObject(facts!!.asMap())
            context.setVariables(facts.asMap())
            if (beanResolver != null) {
                context.beanResolver = beanResolver
            }
            compiledExpression.getValue(context)
        } catch (e: Exception) {
            LOGGER.error("Unable to evaluate expression: '$expression' on facts: $facts", e)
            throw e
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
