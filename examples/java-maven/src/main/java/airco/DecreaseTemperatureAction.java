package airco;

import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.FactsMap;

public class DecreaseTemperatureAction implements Action<FactsMap> {

    static DecreaseTemperatureAction decreaseTemperature() {
        return new DecreaseTemperatureAction();
    }

    @Override
    public void execute(FactsMap facts){
        System.out.println("It is hot! cooling air..");
        Integer temperature = facts.get("temperature");
        facts.put("temperature", temperature - 1);
    }
}
