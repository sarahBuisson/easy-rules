package airco

import airco.DecreaseTemperatureAction.Companion.decreaseTemperature
import airco.HighTemperatureCondition.Companion.itIsHot
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder

open public class Launcher {



   public fun exec(args: Array<String>):Facts {
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
        return facts

    }
}