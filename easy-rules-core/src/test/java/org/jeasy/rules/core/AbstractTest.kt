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

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.jeasy.rules.annotation.Fact
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import kotlin.test.*

open abstract class AbstractTest {

    @MockK(relaxed = true)
    open lateinit var rule1: Rule
    @MockK(relaxed = true)
    open lateinit var rule2: Rule
    @MockK(relaxed = true)
    open lateinit var fact1: Fact
    @MockK(relaxed = true)
    lateinit var fact2: Fact

    lateinit var facts: Facts
    lateinit var rules: Rules

    lateinit var rulesEngine: DefaultRulesEngine

    @BeforeTest
    @Throws(Exception::class)
    open fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        facts = Facts()
        facts.put("fact1", fact1)
        facts.put("fact2", fact2)
        rules = Rules()

        rulesEngine = DefaultRulesEngine()
    }

}