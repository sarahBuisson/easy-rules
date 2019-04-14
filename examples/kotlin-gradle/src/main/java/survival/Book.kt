package survival

import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Facts
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

class GameFacts : Facts() {
    fun qui(): Vivant {
        return (get(FactEnum.qui.name) as Vivant)
    }

    fun qui(qui: Vivant) {
        this.put(FactEnum.qui.name, qui)
    }

    fun surQui(): Any {
        return get(FactEnum.surQui.name)
    }

    fun surQui(qui: Any) {
        this.put(FactEnum.surQui.name, qui)
    }

    fun faitQuoi(): ActionEnum {
        return (get(FactEnum.faitQuoi.name) as ActionEnum)
    }

    fun faitQuoi(quoi: ActionEnum) {
        put(FactEnum.faitQuoi.name, quoi)
    }
}

fun buildActionRules(): Rules {

    // define rules
    val mangeRule = RuleBuilder()
            .name("mange")
            .`when`(object : Condition {
                override fun evaluate(facts: Facts): Boolean {
                    return (facts is GameFacts)
                            && facts.qui().inventaire.contains((facts).surQui())
                            && facts.surQui() is Nouriture
                            && facts.faitQuoi() == ActionEnum.utiliser

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
    val rules = Rules()
    rules.register(mangeRule)

    register(rules, "bouge", object : Condition {
        override fun evaluate(facts: Facts): Boolean {
            return (facts is GameFacts)
                    && facts.faitQuoi().equals(ActionEnum.bouger)
                    && (facts.qui() as Player).energie > 0
        }
    }, object : Action {
        override fun execute(facts: Facts) {
            if (facts is GameFacts) {
                (facts.qui() as Player).energie -= 10
                (facts.qui() as Player).location = facts.surQui() as Location
            }
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })


    register(rules, "rammasser", object : Condition {
        override fun evaluate(facts: Facts): Boolean {
            return (facts is GameFacts)
                    && facts.faitQuoi().equals(ActionEnum.rammasser)
                    && (facts.qui() as Player).energie > 0
                    && (facts.qui() as Player).location.content.contains((facts.surQui() as Item))
        }
    }, object : Action {
        override fun execute(facts: Facts) {
            if (facts is GameFacts) {
                (facts.qui() as Player).energie -= 10
                (facts.qui() as Player).calorie -= 10
                (facts.qui() as Player).inventaire.add(facts.surQui() as Item)
                (facts.qui() as Player).location.content.remove(facts.surQui() as Item)
            }
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })

    return rules


}

fun register(rules: Rules, name: String, condition: Condition, action: Action) {
    rules.register(RuleBuilder().name(name).`when`(condition).then(action).build())
}


fun main() {

    val facts = GameFacts()
    facts.qui(Player())
            //  facts.qui(Player().apply { inventaire.add(Nouriture()) })
    facts.put(FactEnum.surQui.name, Nouriture())
//    facts.put(FactEnum.surQui.name, (facts.qui() as Player).inventaire.first())
    facts.put(FactEnum.faitQuoi.name, ActionEnum.utiliser)

    println((facts.qui() as Player).calorie)
    InferenceRulesEngine().fire(buildActionRules(), facts)
    println((facts.qui() as Player).calorie)
}
