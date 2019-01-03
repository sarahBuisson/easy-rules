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
package org.jeasy.rules.mvel

import org.jeasy.rules.api.Rule
import org.yaml.snakeyaml.Yaml
import java.io.Reader
import java.util.*

@SuppressWarnings("unchecked")
internal class MVELRuleDefinitionReader {

    private val yaml = Yaml()

    fun read(reader: Reader): MVELRuleDefinition {
        val `object` = yaml.load(reader)
        val map = `object` as Map<String, Object>
        return createRuleDefinitionFrom(map)
    }

    fun readAll(reader: Reader): List<MVELRuleDefinition> {
        val ruleDefinitions = ArrayList<MVELRuleDefinition>()
        val rules = yaml.loadAll(reader)
        for (rule in rules) {
            val map = rule as Map<String, Object>
            ruleDefinitions.add(createRuleDefinitionFrom(map))
        }
        return ruleDefinitions
    }

    private fun createRuleDefinitionFrom(map: Map<String, Object>): MVELRuleDefinition {
        val ruleDefinition = MVELRuleDefinition()

        val name = map["name"] as String
        ruleDefinition.name = (name ?: Rule.DEFAULT_NAME)

        val description = map["description"] as String?
        ruleDefinition.description = (description ?: Rule.DEFAULT_DESCRIPTION)

        val priority = map["priority"] as Integer?
        if(priority!= null) {
            ruleDefinition.priority = priority!!.toInt()
        } else {
            ruleDefinition.priority = Rule.DEFAULT_PRIORITY
        }

        val condition = map["condition"] as String?
                ?: throw IllegalArgumentException("The rule condition must be specified")
        ruleDefinition.condition=(condition)

        val actions = map["actions"] as List<String>
        if (actions == null || actions.isEmpty()) {
            throw IllegalArgumentException("The rule action(s) must be specified")
        }
        ruleDefinition.actions=(actions)

        return ruleDefinition
    }
}
