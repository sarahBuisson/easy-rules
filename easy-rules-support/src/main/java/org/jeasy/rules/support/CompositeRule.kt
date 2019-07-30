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
package org.jeasy.rules.support

import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.BasicRule
import org.jeasy.rules.core.RuleProxy

/**
 * Base class representing a composite rule composed of a set of rules.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class CompositeRule<Facts>
/**
 * Create a new [CompositeRule].
 *
 * @param name rule name
 * @param description rule description
 * @param priority rule priority
 */
@JvmOverloads constructor(name: String = Rule.DEFAULT_NAME, description: String = Rule.DEFAULT_DESCRIPTION, priority: Int = Rule.DEFAULT_PRIORITY) : BasicRule<Facts>(name, description, priority) {

    /**
     * The set of composing rules.
     */
    protected var rules: MutableSet<Rule<Facts>>

    private val proxyRules: MutableMap<Any, Rule<Facts>>

    init {
        rules = sortedSetOf()
        proxyRules = HashMap()
    }


    override abstract fun evaluate(facts: Facts): Boolean


    @Throws(Exception::class)
    override abstract fun execute(facts: Facts)

    /**
     * Add a rule to the composite rule.
     * @param rule the rule to add
     */
    fun addRule(rule: Any) {
        if (rule is Rule<*>) {
            rules.add(rule as Rule<Facts>)
        } else {
            val proxy: Rule<Facts> = RuleProxy.asRule(rule) as Rule<Facts> //TODO : should'nt be cast.
            rules.add(proxy)
            proxyRules.put(rule, proxy)
        }
    }

    /**
     * Remove a rule from the composite rule.
     * @param rule the rule to remove
     */
    fun removeRule(rule: Any) {
        if (rule is Rule<*>) {//TODO : should'nt be. check how it was before
            rules.remove(rule as Rule<Facts>)
        } else {
            val proxy = proxyRules[rule]
            if (proxy != null) {
                rules.remove(proxy)
            }
        }
    }

}
/**
 * Create a new [CompositeRule].
 */
/**
 * Create a new [CompositeRule].
 *
 * @param name rule name
 */
/**
 * Create a new [CompositeRule].
 *
 * @param name rule name
 * @param description rule description
 */
