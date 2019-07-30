package airco

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.FactsMap

class DecreaseTemperatureAction : Action<FactsMap> {



    override fun execute(facts: FactsMap) {
        println("It is hot! cooling air..")
        val temperature = facts.get("temperature") as Int
        facts.put("temperature", temperature - 1)
    }

    companion object {

        internal fun decreaseTemperature(): DecreaseTemperatureAction {
            return DecreaseTemperatureAction()
        }
    }
}
