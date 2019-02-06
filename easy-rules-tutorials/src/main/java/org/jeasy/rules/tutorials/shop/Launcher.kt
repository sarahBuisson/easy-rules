/**
 * The MIT License
 *
 * Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.rules.tutorials.shop
//TODO : to decoment once MVEL is ok
/*
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.core.Rules2
import org.jeasy.rules.mvel.MVELRule
import org.jeasy.rules.mvel.MVELRuleFactory

import java.io.FileNotFoundException
import java.io.FileReader

object Launcher {


}

/*
@Throws(FileNotFoundException::class)
fun main(args: Array<String>) {
    //create a person instance (fact)
    val tom = Person("Tom", 14)
    val facts = Facts()
    facts.put("person", tom)

    // create rules
    val ageRule = MVELRule()
            .name("age rule")
            .description("Check if person's age is > 18 and marks the person as adult")
            .priority(1)
            .`when`("person.age > 18")
            .then("person.setAdult(true);")
    val alcoholRule = MVELRuleFactory.createRuleFrom(FileReader("src/main/java/org/jeasy/rules/tutorials/shop/alcohol-rule.yml"))

    // create a rule set
    val rules = Rules2()
    rules.register(ageRule)
    rules.register(alcoholRule)

    //create a default rules engine and fire rules on known facts
    val rulesEngine = DefaultRulesEngine()

    System.out.println("Tom: Hi! can I have some Vodka please?")
    rulesEngine.fire(rules, facts)
}*/