package spaceMarket.model

import kotlinx.serialization.ImplicitReflectionSerializer
import org.jeasy.rules.api.Action
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.InferenceRulesEngine
import org.jeasy.rules.core.RuleBuilder
import kotlin.js.JsName


public val ruleBuy = RuleBuilder<MarketFact>()
    .name("buy")
    .`when`(object : Condition<MarketFact> {
        override fun evaluate(facts: MarketFact): Boolean {
            var acheteur: Player = facts.acheteur
            var vendeur: Player = facts.vendeur
            var objet: Item = facts.objet
            var prix: Double = facts.prix
            return acheteur.money >= prix && vendeur.inventory.contains(objet)

        }
    })
    .then(object : Action<MarketFact> {
        override fun execute(facts: MarketFact) {
            var acheteur: Player = facts.acheteur
            var vendeur: Player = facts.vendeur
            var objet: Item = facts.objet
            var prix: Double = facts.prix
            acheteur.money -= prix;
            vendeur.money += prix;
            vendeur.inventory = vendeur.inventory.filter { it==objet }.toTypedArray()
            acheteur.inventory= acheteur.inventory.plus( objet)
        }
    })
    .build()

val ruleProduce = RuleBuilder<MarketFact>()
    .name("produce")
    .`when`(object : Condition<MarketFact> {
        override fun evaluate(facts: MarketFact): Boolean {
            var acheteur: Player = facts.acheteur
            var producer: Planet = facts.producer
            var what: ItemType = facts.product

            return producer.products.contains(what) && !producer.inventory.map { it.type }.contains(what)

        }
    })
    .then(object : Action<MarketFact> {
        override fun execute(facts: MarketFact) {
            var acheteur: Player = facts.acheteur
            var producer: Planet = facts.producer
            var what: ItemType = facts.product

            producer.inventory += Item(what, 1);
        }
    })
    .build()


val ruleTake = RuleBuilder<MarketFact>()
    .name("take")
    .`when`(object : Condition<MarketFact> {
        override fun evaluate(facts: MarketFact): Boolean {
            var acheteur: Player = facts.acheteur
            var producer: Planet = facts.producer
            var what: ItemType = facts.product

            return producer.inventory.filter { it.type == what }.isNotEmpty()

        }
    })
    .then(object : Action<MarketFact> {
        override fun execute(facts: MarketFact) {
            var acheteur: Player = facts.acheteur
            var producer: Planet = facts.producer
            var what: ItemType = facts.product

            acheteur.inventory = acheteur.inventory.plus(Item(what))
        }
    })
    .build()

fun buildActionRules(): Rules<MarketFact> {

    return Rules(setOf(ruleBuy, ruleProduce, ruleTake))

}


 class SpaceRule {

    @UseExperimental(ImplicitReflectionSerializer::class)
    @JsName("runRules")
    public fun runRules(arg: MarketFact): MarketFact {


        val rules = buildActionRules()

        // fire rules on known facts
        val rulesEngine = InferenceRulesEngine<MarketFact>()

        var facts:MarketFact;

        facts=arg;

        rulesEngine.fire(rules, facts);
        return facts

    }

}

