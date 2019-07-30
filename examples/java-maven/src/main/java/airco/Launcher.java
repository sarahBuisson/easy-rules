package airco;

import org.jeasy.rules.api.FactsMap;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.InferenceRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import static airco.DecreaseTemperatureAction.decreaseTemperature;
import static airco.HighTemperatureCondition.itIsHot;

public class Launcher {

    public static void main(String[] args) {
        // define facts
        FactsMap facts = new FactsMap();
        facts.put("temperature", 30);

        // define rules
        Rule airConditioningRule = new RuleBuilder()
                .name("air conditioning rule")
                .when(itIsHot())
                .then(decreaseTemperature())
                .build();
        Rules rules = new Rules();
        rules.register(airConditioningRule);

        // fire rules on known facts
        RulesEngine rulesEngine = new InferenceRulesEngine();
        rulesEngine.fire(rules, facts);
    }

}
