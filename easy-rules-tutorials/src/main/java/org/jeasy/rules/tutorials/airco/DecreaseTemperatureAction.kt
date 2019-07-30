package org.jeasy.rules.tutorials.airco

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.FactsMap

class DecreaseTemperatureAction : Action<FactsMap> {


    @Throws(Exception::class)
    override fun execute(facts: FactsMap) {
        System.out.println("It is hot! cooling air..")
        val temperature = facts.get("temperature") as Int
        facts.put("temperature", temperature - 1)
    }

    companion object {

        internal fun decreaseTemperature(): DecreaseTemperatureAction {
            return DecreaseTemperatureAction()
        }
    }
}
