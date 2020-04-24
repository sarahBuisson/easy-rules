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
 * A listener for rules engine execution events.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
interface RulesEngineListener<Facts> {

    /**
     * Triggered before evaluating the rule set.
     * **When this listener is used with a [InferenceRulesEngine], this method will be triggered before the evaluation of each candidate rule set in each iteration.**
     *
     * @param rules to fire
     * @param facts present before firing rules
     */
    fun beforeEvaluate(rules: Rules<Facts>, facts: Facts)

    /**
     * Triggered after executing the rule set
     * **When this listener is used with a [InferenceRulesEngine], this method will be triggered after the execution of each candidate rule set in each iteration.**
     *
     * @param rules fired
     * @param facts present after firing rules
     */
    fun afterExecute(rules: Rules<Facts>, facts: Facts)
}
