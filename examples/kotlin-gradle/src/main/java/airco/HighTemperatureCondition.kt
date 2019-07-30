package airco

import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.FactsMap

class HighTemperatureCondition : Condition<FactsMap> {


    override fun evaluate(facts: FactsMap): Boolean {
        val temperature = facts.get("temperature") as Int?
        return temperature!! > 25
    }

    companion object {

        internal fun itIsHot(): HighTemperatureCondition {
            return HighTemperatureCondition()
        }
    }

}
