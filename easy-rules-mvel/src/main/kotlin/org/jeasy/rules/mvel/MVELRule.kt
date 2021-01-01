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
package org.jeasy.rules.mvel

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.BasicRule
import org.mvel2.ParserContext
import java.util.*

/**
 * A [org.jeasy.rules.api.Rule] implementation that uses
 * [MVEL](https://github.com/mvel/mvel) to evaluate and execute the rule.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class MVELRule
/**
 * Create a new MVEL rule.
 */ @JvmOverloads constructor(private val parserContext: ParserContext = ParserContext()) :
    BasicRule(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY) {
    private var condition = Condition.FALSE
    private val actions: MutableList<Action> = ArrayList()

    /**
     * Set rule name.
     *
     * @param name of the rule
     * @return this rule
     */
    fun name(name: String): MVELRule {
        this.name = name
        return this
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return this rule
     */
    fun description(description: String): MVELRule {
        this.description = description
        return this
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return this rule
     */
    fun priority(priority: Int): MVELRule {
        this.priority = priority
        return this
    }

    /**
     * Specify the rule's condition as MVEL expression.
     * @param condition of the rule
     * @return this rule
     */
    fun `when`(condition: String): MVELRule {
        this.condition = MVELCondition(condition, parserContext)
        return this
    }

    /**
     * Add an action specified as an MVEL expression to the rule.
     * @param action to add to the rule
     * @return this rule
     */
    fun then(action: String): MVELRule {
        actions.add(MVELAction(action, parserContext))
        return this
    }

    override fun evaluate(facts: FactType): Boolean {
        return condition!!.evaluate(facts)
    }

    @Throws(Exception::class)
    override fun execute(facts: FactType) {
        for (action in actions) {
            action.execute(facts)
        }
    }
    /**
     * Create a new MVEL rule.
     *
     * @param parserContext used to parse condition/action expressions
     */
}
