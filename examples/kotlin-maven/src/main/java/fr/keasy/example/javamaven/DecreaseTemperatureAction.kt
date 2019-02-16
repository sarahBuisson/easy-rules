package fr.keasy.example.javamaven

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Facts

class DecreaseTemperatureAction : Action {


    @Throws(Exception::class)
    override fun execute(facts: Facts) {
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
