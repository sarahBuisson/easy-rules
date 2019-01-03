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
package org.jeasy.rules.mvel

import org.jeasy.rules.api.Rules

import java.io.Reader

/**
 * Factory to create [MVELRule] instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
object MVELRuleFactory {

    private val reader = MVELRuleDefinitionReader()

    /**
     * Create a new [MVELRule] from a Reader.
     *
     * @param ruleDescriptor as a Reader
     * @return a new rule
     */
    fun createRuleFrom(ruleDescriptor: Reader): MVELRule {
        val ruleDefinition = reader.read(ruleDescriptor)
        return ruleDefinition.create()
    }

    /**
     * Create a set of [MVELRule] from a Reader.
     *
     * @param rulesDescriptor as a Reader
     * @return a set of rules
     */
    fun createRulesFrom(rulesDescriptor: Reader): Rules {
        val rules = Rules()
        val ruleDefinition = reader.readAll(rulesDescriptor)
        for (mvelRuleDefinition in ruleDefinition) {
            rules.register(mvelRuleDefinition.create())
        }
        return rules
    }
}
