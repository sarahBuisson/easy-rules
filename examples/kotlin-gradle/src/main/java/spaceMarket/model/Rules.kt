import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.RuleBuilder
import survival.ActionEnum
import survival.GameFacts
import survival.Nouriture
import survival.Player

fun buildActionRules(): Rules {

    var ruleBuy = RuleBuilder()
            .name("buy")
            .`when`(object : Condition {
                override fun evaluate(facts: Facts): Boolean {
                    return true

                }
            })
            .then(object : Action {
                override fun execute(facts: Facts) {
                    println("run")
                    (facts as GameFacts).qui().inventaire.remove((facts as GameFacts).surQui())
                    ((facts as GameFacts).qui() as Player).calorie += 10
                }
            })
            .build()
    return Rules()

}
