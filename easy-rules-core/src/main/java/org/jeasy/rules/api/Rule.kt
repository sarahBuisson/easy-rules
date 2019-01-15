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
 * Abstraction for a rule that can be fired by the rules engine.
 *
 * Rules are registered in a rule set of type `Rules` in which they must have a **unique** name.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
interface Rule : Comparable<Rule> {

    /**
     * Getter for rule name.
     * @return the rule name
     */
    val name: String

    /**
     * Getter for rule description.
     * @return rule description
     */
    val description: String

    /**
     * Getter for rule priority.
     * @return rule priority
     */
    val priority: Int

    /**
     * Rule conditions abstraction : this method encapsulates the rule's conditions.
     * **Implementations should handle any runtime exception and return true/false accordingly**
     *
     * @return true if the rule should be applied given the provided facts, false otherwise
     */
    fun evaluate(facts: Facts): Boolean


    /**
     * Rule actions abstraction : this method encapsulates the rule's actions.
     * @throws Exception thrown if an exception occurs during actions performing
     */
    @Throws(Exception::class)
    fun execute(facts: Facts)

    companion object {

        /**
         * Default rule name.
         */
        val DEFAULT_NAME = "rule"

        /**
         * Default rule description.
         */
        val DEFAULT_DESCRIPTION = "description"

        /**
         * Default rule priority.
         */
        val DEFAULT_PRIORITY = Integer.MAX_VALUE - 1
    }

}
