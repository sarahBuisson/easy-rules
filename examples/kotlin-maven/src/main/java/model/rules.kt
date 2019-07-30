package spaceMarket.model

import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.core.Condition;
import org.jeasy.rules.core.Facts;
import org.jeasy.rules.core.Action;


public val ruleBuy = RuleBuilder()
        .name("buy")
        .`when`(object : Condition {
            override fun evaluate(facts: Facts): Boolean {
                var acheteur: Player = facts.get("acheteur");
                var vendeur: Player = facts.get("vendeur");
                var objet: Item = facts.get("objet");
                var prix: Double = facts.get("prix");
                return acheteur.money>=prix && vendeur.inventory.contains(objet)

            }
        })
        .then(object : Action {
            override fun execute(facts: Facts) {
                var acheteur: Player = facts.get("acheteur");
                var vendeur: Player = facts.get("vendeur");
                var objet: Item = facts.get("objet");
                var prix: Double = facts.get("prix");
                acheteur.money-=prix;
                vendeur.money+=prix;
                vendeur.inventory-=objet;
                acheteur.inventory+=objet;
            }
        })
        .build()

val ruleProduce= RuleBuilder()
        .name("produce")
        .`when`(object : Condition {
            override fun evaluate(facts: Facts): Boolean {
                var acheteur: Player = facts.get("acheteur");
                var producer: Planet = facts.get("producer");
                var what: ItemType = facts.get("product");

                return producer.products.contains(what)

            }
        })
        .then(object : Action {
            override fun execute(facts: Facts) {
                var acheteur: Player = facts.get("acheteur");
                var producer: Planet = facts.get("producer");
                var what: ItemType = facts.get("product");

                acheteur.inventory+=Item(what,1);
            }
        })
        .build()


val ruleTake = RuleBuilder()
        .name("take")
        .`when`(object : Condition {
            override fun evaluate(facts: Facts): Boolean {
                var acheteur: Player = facts.get("acheteur");
                var producer: Planet = facts.get("producer");
                var what: Item = facts.get("product");

                return producer.inventory.contains(what)

            }
        })
        .then(object : Action {
            override fun execute(facts: Facts) {
                var acheteur: Player = facts.get("acheteur");
                var producer: Planet = facts.get("producer");
                var what: Item = facts.get("product");

                acheteur.inventory+=what
            }
        })
        .build()

fun buildActionRules(): Rules {

    return Rules(setOf(ruleBuy,ruleProduce,ruleTake))

}


