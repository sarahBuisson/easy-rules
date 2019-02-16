package fr.keasy.example.javamaven

import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
import java.util.concurrent.locks.Condition

class HighTemperatureCondition : Condition {


    override fun evaluate(facts: Facts): Boolean {
        val temperature = facts.get("temperature") as Int?
        return temperature!! > 25
    }

    companion object {

        internal fun itIsHot(): HighTemperatureCondition {
            return HighTemperatureCondition()
        }
    }

}
