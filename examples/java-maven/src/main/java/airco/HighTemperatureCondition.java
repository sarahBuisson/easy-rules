package airco;

import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.FactsMap;

public class HighTemperatureCondition implements Condition<FactsMap> {

    static HighTemperatureCondition itIsHot() {
        return new HighTemperatureCondition();
    }

    @Override
    public boolean evaluate(FactsMap facts) {
        Integer temperature = facts.get("temperature");
        return temperature > 25;
    }

}
