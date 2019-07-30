package org.jeasy.rules.tutorials.airco

import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder
import org.jeasy.rules.tutorials.airco.DecreaseTemperatureAction.Companion.decreaseTemperature

import org.jeasy.rules.tutorials.airco.HighTemperatureCondition.Companion.itIsHot

class Launcher {



}
fun main(args: Array<String>) {
    // define facts
    val facts = Facts()
    facts.put("temperature", 30)

    // define rules
    val airConditioningRule = RuleBuilder()
            .name("air conditioning rule")
            .`when`(itIsHot())
            .then(decreaseTemperature())
            .build()
    val rules = Rules()
    rules.register(airConditioningRule)

    // fire rules on known facts
    val rulesEngine = InferenceRulesEngine()
    rulesEngine.fire(rules, facts)
}
