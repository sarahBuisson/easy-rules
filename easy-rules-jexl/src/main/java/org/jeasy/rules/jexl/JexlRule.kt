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
package org.jeasy.rules.jexl

import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.JexlEngine
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.BasicRule
import java.util.*

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
class JexlRule @JvmOverloads constructor(jexl: JexlEngine? = DEFAULT_JEXL) :
    BasicRule(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY) {
    private var condition = Condition.FALSE
    private val actions: MutableList<Action> = ArrayList()
    private val jexl: JexlEngine
    fun name(name: String): JexlRule {
        this.name = Objects.requireNonNull(name, "name cannot be null")
        return this
    }

    fun description(description: String): JexlRule {
        this.description = Objects.requireNonNull(description, "description cannot be null")
        return this
    }

    fun priority(priority: Int): JexlRule {
        this.priority = priority
        return this
    }

    fun `when`(condition: String): JexlRule {
        Objects.requireNonNull(condition, "condition cannot be null")
        this.condition = JexlCondition(condition, jexl)
        return this
    }

    fun then(action: String): JexlRule {
        Objects.requireNonNull(action, "action cannot be null")
        actions.add(JexlAction(action, jexl))
        return this
    }

    override fun evaluate(facts: FactType): Boolean {
        Objects.requireNonNull(facts, "facts cannot be null")
        return condition.evaluate(facts)
    }

    @Throws(Exception::class)
    override fun execute(facts: FactType) {
        Objects.requireNonNull(facts, "facts cannot be null")
        for (action in actions) {
            action.execute(facts)
        }
    }

    companion object {
        val DEFAULT_JEXL = JexlBuilder().create()
    }

    init {
        this.jexl = Objects.requireNonNull(jexl, "jexl cannot be null")!!
    }
}
