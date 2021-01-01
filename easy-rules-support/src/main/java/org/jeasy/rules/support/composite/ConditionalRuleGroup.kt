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
package org.jeasy.rules.support.composite

import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule

/**
 * A conditional rule group is a composite rule where the rule with the highest
 * priority acts as a condition: if the rule with the highest priority evaluates
 * to true, then we try to evaluate the rest of the rules and execute the ones
 * that evaluate to true.
 *
 * **This class is not thread-safe.**
 *
 * @author Dag Framstad (dagframstad@gmail.com)
 */
class ConditionalRuleGroup : CompositeRule {
    private lateinit var successfulEvaluations: MutableSet<Rule>
    private lateinit var conditionalRule: Rule

    /**
     * Create a conditional rule group.
     */
    constructor() {}

    /**
     * Create a conditional rule group.
     *
     * @param name of the conditional rule
     */
    constructor(name: String) : super(name) {}

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     */
    constructor(name: String, description: String) : super(name, description) {}

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     * @param priority    of the composite rule
     */
    constructor(name: String, description: String, priority: Int) : super(name, description, priority) {}

    /**
     * A conditional rule group will trigger all its composing rules if the condition
     * of the rule with highest priority evaluates to true.
     *
     * @param facts The facts.
     * @return true if the conditions of all composing rules evaluate to true
     */
    override fun evaluate(facts: Facts): Boolean {
        successfulEvaluations = HashSet()
        conditionalRule = getRuleWithHighestPriority()
        if (conditionalRule.evaluate(facts)) {
            for (rule in rules) {
                if (rule !== conditionalRule && rule.evaluate(facts)) {
                    successfulEvaluations.add(rule)
                }
            }
            return true
        }
        return false
    }

    /**
     * When a conditional rule group is executed, all rules that evaluated to true
     * are performed in their natural order, but with the conditional rule
     * (the one with the highest priority) first.
     *
     * @param facts The facts.
     *
     * @throws Exception thrown if an exception occurs during actions performing
     */
    @Throws(Exception::class)
    override fun execute(facts: Facts) {
        conditionalRule.execute(facts)
        for (rule in sort(successfulEvaluations)) {
            rule.execute(facts)
        }
    }

    private fun getRuleWithHighestPriority(): Rule {
        val copy = sort(rules)
        // make sure we only have one rule with the highest priority
        val highest = copy.get(0)
        require(
            !(copy.size > 1 && copy.get(1).priority == highest.priority)
        ) { "Only one rule can have highest priority" }
        return highest
    }

    private fun sort(rules: MutableSet<Rule>): MutableList<Rule> {
        return ArrayList(rules.toList().sorted())
    }
}
