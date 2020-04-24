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
 * Parameters of a rules engine.
 *
 *
 *  * When parameters are used with a [DefaultRulesEngine], they are applied on **all registered rules**.
 *  * When parameters are used with a [InferenceRulesEngine], they are applied on **candidate rules in each iteration**.
 *
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
interface RulesEngineParameters {

    /**
     * Parameter to skip next applicable rules when a rule is applied.
     */
    var isSkipOnFirstAppliedRule: Boolean

    /**
     * Parameter to skip next applicable rules when a rule is non triggered
     */
    var isSkipOnFirstNonTriggeredRule: Boolean

    /**
     * Parameter to skip next applicable rules when a rule has failed.
     */
    var isSkipOnFirstFailedRule: Boolean

    /**
     * Parameter to skip next rules if priority exceeds a user defined threshold.
     */
    var priorityThreshold: Int


    fun priorityThreshold(priorityThreshold: Int): RulesEngineParameters 

    fun skipOnFirstAppliedRule(skipOnFirstAppliedRule: Boolean): RulesEngineParameters

    fun skipOnFirstNonTriggeredRule(skipOnFirstNonTriggeredRule: Boolean): RulesEngineParameters

    fun skipOnFirstFailedRule(skipOnFirstFailedRule: Boolean): RulesEngineParameters


    companion object {

        /**
         * Default rule priority threshold.
         */
        val DEFAULT_RULE_PRIORITY_THRESHOLD = Int.MAX_VALUE
    }
}
