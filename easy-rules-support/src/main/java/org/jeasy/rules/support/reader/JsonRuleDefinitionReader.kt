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
package org.jeasy.rules.support.reader

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Reader
import java.util.*

/**
 * Rule definition reader based on [Jackson](https://github.com/FasterXML/jackson).
 *
 * This reader expects an array of rule definitions as input even for a single rule. For example:
 *
 * <pre>
 * [{rule1}, {rule2}]
</pre> *
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class JsonRuleDefinitionReader(private val objectMapper: ObjectMapper = ObjectMapper()) :
    AbstractRuleDefinitionReader() {
    @Throws(Exception::class)
    override fun loadRules(reader: Reader): Iterable<MutableMap<String, Any>> {
        val rulesList: MutableList<MutableMap<String, Any>> = ArrayList()
        val rules: Array<Any> = objectMapper.readValue(reader, Array<Any>::class.java)
        for (rule in rules) {
            rulesList.add(rule as MutableMap<String, Any>)
        }
        return rulesList
    }
    /**
     * Create a new [JsonRuleDefinitionReader].
     *
     * @param objectMapper to use to read rule definitions
     */
}
