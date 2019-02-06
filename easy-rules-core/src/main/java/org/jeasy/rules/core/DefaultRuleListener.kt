/**
 * The MIT License
 *
 * Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jeasy.rules.core

import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.RuleListener



internal class DefaultRuleListener : RuleListener {


    override fun beforeEvaluate(rule: Rule, facts: Facts): Boolean {
        return true
    }


    override fun afterEvaluate(rule: Rule, facts: Facts, evaluationResult: Boolean) {
        val ruleName = rule.name
        if (evaluationResult) {
            LOGGER.info{"Rule '$ruleName' triggered"}
        } else {
            LOGGER.info{"Rule '$ruleName' has been evaluated to false, it has not been executed"}
        }
    }


    override fun beforeExecute(rule: Rule, facts: Facts) {

    }


    override fun onSuccess(rule: Rule, facts: Facts) {
        LOGGER.info { "Rule '${rule.name}' performed successfully" }
    }


    override fun onFailure(rule: Rule, facts: Facts, exception: Exception) {
        LOGGER.info { "Rule '" + rule.name + "' performed with error" }
        LOGGER.info { exception }
    }

    companion object {

        private val LOGGER =  mu.KotlinLogging.logger {}
    }
}
