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

/**
 * Basic rule implementation class that provides common methods.
 *
 * You can extend this class and override [BasicRule.evaluate] and [BasicRule.execute] to provide rule
 * conditions and actions logic.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
open class BasicRule(override var name: String = Rule.DEFAULT_NAME,
                     /**
                      * Rule description.
                      */
                     override var description: String = Rule.DEFAULT_DESCRIPTION,
                     /**
                      * Rule priority.
                      */
                     override var priority: Int = Rule.DEFAULT_PRIORITY) : Rule {
    /**
     * Create a new [BasicRule].
     *
     * @param name rule name
     * @param description rule description
     * @param priority rule priority
     */


    /**
     * {@inheritDoc}
     */
    override fun evaluate(facts: Facts): Boolean {
        return false
    }

    /**
     * {@inheritDoc}
     */
    @Throws(Exception::class)
    override fun execute(facts: Facts) {
        // no op
    }

    /*
     * Rules are unique according to their names within a rules engine registry.
     */


    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o == null || this::class !== o!!::class)
            return false

        val basicRule = o as BasicRule?

        if (priority != basicRule!!.priority)
            return false
        return if (!name.equals(basicRule.name)) false else !if (description != null) !description!!.equals(basicRule.description) else basicRule.description != null

    }


    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (description != null) description!!.hashCode() else 0
        result = 31 * result + priority
        return result
    }


    override fun toString(): String {
        return name
    }


    override operator fun compareTo(rule: Rule): Int {
        return if (priority < rule.priority) {
            -1
        } else if (priority > rule.priority) {
            1
        } else {
            name.compareTo(rule.name)
        }
    }

}
/**
 * Create a new [BasicRule].
 */
/**
 * Create a new [BasicRule].
 *
 * @param name rule name
 */
/**
 * Create a new [BasicRule].
 *
 * @param name rule name
 * @param description rule description
 */
