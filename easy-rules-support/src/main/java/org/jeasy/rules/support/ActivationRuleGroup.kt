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


/**
 * An activation rule group is a composite rule that fires the first applicable rule and ignores other rules in
 * the group (XOR logic). Rules are first sorted by their natural order (priority by default) within the group.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class ActivationRuleGroup<Facts> : CompositeRule<Facts> {

    private var selectedRule: Rule<Facts>? = null

    /**
     * Create an activation rule group.
     */
    constructor() {
        rules = sortedSetOf<Rule<Facts>>()
    }

    /**
     * Create an activation rule group.
     *
     * @param name of the activation rule group
     */
    constructor(name: String) : super(name) {
        rules = sortedSetOf<Rule<Facts>>()
    }

    /**
     * Create a conditional rule group.
     *
     * @param name        of the activation rule group
     * @param description of the activation rule group
     */
    constructor(name: String, description: String) : super(name, description) {
        rules = sortedSetOf<Rule<Facts>>()
    }

    /**
     * Create an activation rule group.
     *
     * @param name        of the activation rule group
     * @param description of the activation rule group
     * @param priority    of the activation rule group
     */
    constructor(name: String, description: String, priority: Int) : super(name, description, priority) {
        rules = sortedSetOf<Rule<Facts>>()
    }


    override fun evaluate(facts: Facts): Boolean {
        for (rule in rules) {
            if (rule.evaluate(facts)) {
                selectedRule = rule
                return true
            }
        }
        return false
    }


    @Throws(Exception::class)
    override fun execute(facts: Facts) {
        selectedRule!!.execute(facts)
    }
}
