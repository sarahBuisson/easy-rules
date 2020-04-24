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

import mu.KotlinLogging
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngineListener
import org.jeasy.rules.api.RulesEngineParameters

internal class DefaultRulesEngineListener<Facts>(private val parameters: RulesEngineParameters) : RulesEngineListener<Facts> {


    override fun beforeEvaluate(rules: Rules<Facts>, facts: Facts) {
        if (!rules.isEmpty) {
            logEngineParameters()
            log(rules)
            log(facts)
            LOGGER.info { "Rules evaluation started" }
        } else {
            LOGGER.warn { "No rules registered! Nothing to apply" }
        }
    }


    override fun afterExecute(rules: Rules<Facts>, facts: Facts) {

    }

    private fun logEngineParameters() {
        LOGGER.info { parameters.toString() }
    }

    private fun log(rules: Rules<Facts>) {
        LOGGER.info { "Registered rules:" }
        for (rule in rules) {
            LOGGER.info { "Rule { name = '${rule.name}', description = '${rule.description}', priority = '{$rule.priority}'}" }
        }
    }

    private fun log(facts: Facts) {
        LOGGER.info { "Known facts:" }

            LOGGER.info { facts.toString() }

    }

    companion object {

        private val LOGGER = KotlinLogging.logger(this::class.simpleName!!)
    }
}
