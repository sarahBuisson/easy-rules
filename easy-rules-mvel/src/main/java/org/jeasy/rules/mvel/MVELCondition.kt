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

import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
import org.mvel2.MVEL
import org.mvel2.ParserContext
import java.io.Serializable

/**
 * This class is an implementation of [Condition] that uses
 * [MVEL](https://github.com/mvel/mvel) to evaluate the condition.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class MVELCondition : Condition {
    private val compiledExpression: Serializable

    /**
     * Create a new [MVELCondition].
     *
     * @param expression the condition written in expression language
     */
    constructor(expression: String) {
        compiledExpression = MVEL.compileExpression(expression)
    }

    /**
     * Create a new [MVELCondition].
     *
     * @param expression the condition written in expression language
     * @param parserContext the MVEL parser context
     */
    constructor(expression: String, parserContext: ParserContext) {
        compiledExpression = MVEL.compileExpression(expression, parserContext)
    }

    override fun evaluate(facts: Facts): Boolean {
        // MVEL.evalToBoolean does not accept compiled expressions..
        return MVEL.executeExpression(compiledExpression, facts!!.asMap()) as Boolean
    }
}
