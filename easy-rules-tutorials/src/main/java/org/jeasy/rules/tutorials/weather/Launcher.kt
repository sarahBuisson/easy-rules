package org.jeasy.rules.tutorials.weather

import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.core.DefaultRulesEngine

object Launcher {

    fun main(args: Array<String>) {
        // define facts
        val facts = Facts()
        facts.put("rain", true)

        // define rules
        val weatherRule = WeatherRule()
        val rules = Rules()
        rules.register(weatherRule)

        // fire rules on known facts
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)
    }

}