package fr.keasy.example.javamaven

import fr.keasy.example.javamaven.DecreaseTemperatureAction.Companion.decreaseTemperature
import fr.keasy.example.javamaven.HighTemperatureCondition.Companion.itIsHot
import org.jeasy.rules.api.FactsMap
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder

class Launcher {



}
fun main(args: Array<String>) {
    // define facts
    val facts = FactsMap()
    facts.put("temperature", 30)

    // define rules
    val airConditioningRule = RuleBuilder<FactsMap>()
            .name("air conditioning rule")
            .`when`(itIsHot())
            .then(decreaseTemperature())
            .build()
    val rules = Rules<FactsMap>()
    rules.register(airConditioningRule)

    // fire rules on known facts
    val rulesEngine = InferenceRulesEngine<FactsMap>()
    rulesEngine.fire(rules, facts)
}
