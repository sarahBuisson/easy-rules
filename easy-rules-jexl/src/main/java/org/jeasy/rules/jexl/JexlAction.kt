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

import mu.KotlinLogging
import org.apache.commons.jexl3.JexlEngine
import org.apache.commons.jexl3.JexlException
import org.apache.commons.jexl3.JexlScript
import org.apache.commons.jexl3.MapContext
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Facts
import org.jeasy.rules.jexl.JexlAction
import org.jeasy.rules.jexl.JexlRule
import java.util.*

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
class JexlAction : Action {
    private val compiledScript: JexlScript
    private val expression: String

    constructor(expression: String) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null")
        compiledScript = JexlRule.Companion.DEFAULT_JEXL.createScript(expression)
    }

    constructor(expression: String, jexl: JexlEngine) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null")
        Objects.requireNonNull(jexl, "jexl cannot be null")
        compiledScript = jexl.createScript(expression)
    }

    override fun execute(facts: Facts) {
        Objects.requireNonNull(facts, "facts cannot be null")
        val ctx = MapContext(facts.asMap())
        try {
            compiledScript.execute(ctx)
        } catch (e: JexlException) {
            LOGGER.error("Unable to execute expression: '$expression' on facts: $facts", e)
            throw e
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
