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

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Rule

import kotlin.collections.ArrayList

/**
 * Builder to create [Rule] instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class RuleBuilder<Facts> {

    private var name = Rule.DEFAULT_NAME
    private var description = Rule.DEFAULT_DESCRIPTION
    private var priority = Rule.DEFAULT_PRIORITY

    private var condition = Condition.FALSE as Condition<Facts>
    private val actions = ArrayList<Action<Facts>>()

    /**
     * Set rule name.
     *
     * @param name of the rule
     * @return the builder instance
     */
    fun name(name: String): RuleBuilder<Facts> {
        this.name = name
        return this
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return the builder instance
     */
    fun description(description: String): RuleBuilder<Facts> {
        this.description = description
        return this
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return the builder instance
     */
    fun priority(priority: Int): RuleBuilder<Facts> {
        this.priority = priority
        return this
    }

    /**
     * Set rule condition.
     *
     * @param condition of the rule
     * @return the builder instance
     */
    fun `when` (condition: Condition<Facts>): RuleBuilder<Facts> {
        this.condition = condition
        return this
    }

    /**
     * Add an action to the rule.
     *
     * @param action to add
     * @return the builder instance
     */
    fun then(action: Action<Facts>): RuleBuilder<Facts> {
        this.actions.add(action)
        return this
    }

    /**
     * Create a new [Rule].
     *
     * @return a new rule instance
     */
    fun build(): Rule<Facts> {
        return DefaultRule(name, description, priority, condition, actions)
    }
}
