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
import org.jeasy.rules.core.BasicRule
import java.util.*

/**
 * Base class representing a composite rule composed of a set of rules.
 *
 * **This class is not thread-safe.
 * Sub-classes are inherently not thread-safe.**
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class CompositeRule @JvmOverloads constructor(
    name: String = Rule.DEFAULT_NAME,
    description: String = Rule.DEFAULT_DESCRIPTION,
    priority: Int = Rule.DEFAULT_PRIORITY
) : BasicRule(name, description, priority) {
    /**
     * The set of composing rules.
     */
    protected var rules: MutableSet<Rule> = TreeSet()

    /**
     * Add a rule to the composite rule.
     * @param rule the rule to add
     */
    fun addRule(rule: Rule) {
        rules.add(rule)
    }

    /**
     * Remove a rule from the composite rule.
     * @param rule the rule to remove
     */
    fun removeRule(rule: Rule) {
            rules.remove(rule)

    }
}
