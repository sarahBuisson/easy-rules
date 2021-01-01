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
package org.jeasy.rules.support

import org.jeasy.rules.api.Rule

/**
 * Rule definition as defined in a rule descriptor.
 * This class encapsulates the static definition of a [Rule].
 *
 * Rule definitions are produced by a `RuleDefinitionReader`s
 * and consumed by rule factories to create rules.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class RuleDefinition {
     var name = Rule.DEFAULT_NAME
     var description = Rule.DEFAULT_DESCRIPTION
     var priority = Rule.DEFAULT_PRIORITY
     lateinit var condition: String
     var actions: MutableList<String> = ArrayList()
     var composingRules: MutableList<RuleDefinition> = ArrayList()
     lateinit var compositeRuleType: String

     fun isCompositeRule(): Boolean {
          return !composingRules.isEmpty()
     }
}
