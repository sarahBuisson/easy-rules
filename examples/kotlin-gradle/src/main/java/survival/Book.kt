package survival

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder


class Player : Vivant() {

    var hydratation: Int = 100
    var energie: Int = 100
    var calorie: Int = 100
    var temperature: Int = 100
    var oxygen: Int = 100
}

open class Vivant {
    var santé: Int = 100
    val inventaire: MutableCollection<Item> = mutableListOf()
    lateinit var location: Location
}

open class Item {}
open class Nouriture : Item() {}
open class Eau : Item() {}
open class Epee : Item() {}


enum class ActionEnum {
    attaquer, bouger, rammasser, utiliser
}

enum class FactEnum {
    qui, surQui, faitQuoi
}


open class Location {
    val content: MutableCollection<Item> = mutableListOf()
}

class GameFacts {
    var qui: Vivant? = null
    var surQui: Any? = null
    lateinit var faitQuoi: ActionEnum
}

fun buildActionRules(): Rules<GameFacts> {

    // define rules
    val mangeRule = RuleBuilder<GameFacts>()
        .name("mange")
        .`when`(object : Condition<GameFacts> {
            override fun evaluate(facts: GameFacts): Boolean {
                return facts.qui!!.inventaire.contains(facts.surQui)
                        && facts.surQui is Nouriture
                        && facts.faitQuoi == ActionEnum.utiliser

            }
        })
        .then(object : Action<GameFacts> {
            override fun execute(facts: GameFacts) {
                println("run")
                facts.qui!!.inventaire.remove(facts.surQui)
                (facts.qui as Player).calorie += 10
            }
        })
        .build()
    val rules = Rules<GameFacts>()
    rules.register(mangeRule)

    register(rules, "bouge", object : Condition<GameFacts> {
        override fun evaluate(facts: GameFacts): Boolean {
            return facts.faitQuoi.equals(ActionEnum.bouger)
                    && (facts.qui as Player).energie > 0
        }
    }, object : Action<GameFacts> {
        override fun execute(facts: GameFacts) {
            (facts.qui as Player).energie -= 10
            (facts.qui as Player).location = facts.surQui as Location


        }
    })


    register(rules, "rammasser", object : Condition<GameFacts> {
        override fun evaluate(facts: GameFacts): Boolean {
            return  facts.faitQuoi.equals(ActionEnum.rammasser)
                    && (facts.qui as Player).energie > 0
                    && (facts.qui as Player).location.content.contains((facts.surQui as Item))
        }
    }, object : Action<GameFacts> {
        override fun execute(facts: GameFacts) {

                (facts.qui as Player).energie -= 10
                (facts.qui as Player).calorie -= 10
                (facts.qui as Player).inventaire.add(facts.surQui as Item)
                (facts.qui as Player).location.content.remove(facts.surQui as Item)
            }
    })

    return rules


}

fun register(rules: Rules<GameFacts>, name: String, condition: Condition<GameFacts>, action: Action<GameFacts>) {
    rules.register(RuleBuilder<GameFacts>().name(name).`when`(condition).then(action).build())
}


fun main() {

    val facts = GameFacts()
    facts.qui = (Player())
    //  facts.qui(Player().apply { inventaire.add(Nouriture()) })
    facts.surQui = Nouriture()
//    facts.put(FactEnum.surQui.name, (facts.qui() as Player).inventaire.first())
    facts.faitQuoi = ActionEnum.utiliser

    println((facts.qui as Player).calorie)
    InferenceRulesEngine<GameFacts>().fire(buildActionRules(), facts)
    println((facts.qui as Player).calorie)
}


public class RuleBookRules {

    companion object {

        public fun runSurvivalRules(facts: GameFacts): GameFacts {


            InferenceRulesEngine<GameFacts>().fire(buildActionRules(), facts)

            return facts

        }
    }
}
