package fr.keasy.example.javamaven

import fr.keasy.example.javamaven.DecreaseTemperatureAction.Companion.decreaseTemperature
import fr.keasy.example.javamaven.HighTemperatureCondition.Companion.itIsHot
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder

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