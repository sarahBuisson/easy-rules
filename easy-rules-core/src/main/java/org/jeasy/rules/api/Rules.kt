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
package org.jeasy.rules.api


/**
 * This class encapsulates a set of rules and represents a rules namespace.
 * Rules must have a unique name within a rules namespace.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
open class Rules : Iterable<Rule> {

    protected var rules = sortedSetOf<Rule>();

    /**
     * Check if the rule set is empty.
     *
     * @return true if the rule set is empty, false otherwise
     */
    val isEmpty: Boolean
        get() = rules.isEmpty()
    val size: Int
        get() = rules.size

    /**
     * Create a new [Rules] object.
     *
     * @param rules to register
     */
    constructor(rules: Set<Rule>) {
        this.rules.addAll(rules)
    }

    constructor() {
    }


    /**
     * Clear rules.
     */
    fun clear() {
        rules.clear()
    }


    override fun iterator(): Iterator<Rule> {
        return rules.iterator()
    }

    /**
     * Register a new rule.
     *
     * @param rule to register
     */
    fun register(rule: Rule) {
        this.rules.add(rule)
    }

    /**
     * Unregister a rule.
     *
     * @param rule to unregister
     */
    fun unregister(rule: Rule) {
        this.rules.remove(rule)
    }


    operator fun get(i: Int): Rule {
        return rules.toList()[i];
    }

    protected fun findRuleByName(ruleName: String): Rule? {
        for (rule in rules) {
            if (rule.name.toUpperCase().equals(ruleName.toUpperCase()))
                return rule
        }
        return null
    }
}
