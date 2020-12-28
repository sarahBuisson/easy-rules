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
package org.jeasy.rules.api

/**
 * Abstraction for a rule that can be fired by a rules engine.
 *
 * Rules are registered in a namespace of rule of type [Rules]
 * in which they must have a **unique** name.
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
     * This method implements the rule's condition(s).
     * **Implementations should handle any runtime exception and return true/false accordingly**
     *
     * @return true if the rule should be applied given the provided facts, false otherwise
     */
    open fun evaluate(facts: Facts): Boolean

    /**
     * This method implements the rule's action(s).
     * @throws Exception thrown if an exception occurs when performing action(s)
     */
    @Throws(Exception::class)
    open fun execute(facts: Facts)

    companion object {
        /**
         * Default rule name.
         */
        const val DEFAULT_NAME: String = "rule"

        /**
         * Default rule description.
         */
        const val DEFAULT_DESCRIPTION: String = "description"

        /**
         * Default rule priority.
         */
        const val DEFAULT_PRIORITY = Int.MAX_VALUE - 1
    }
}
