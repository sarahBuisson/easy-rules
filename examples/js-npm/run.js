var kotlin = require('kotlin')



//new log.mu.internal.KLoggerJS("ee").info("eeeeeee")
let rulesBook = require('./lib/rules')
let easy = require('easy-rules-core').org.jeasy

//log(easy);

console.log("rulesBook.spaceMarket.ruleProduce")
var factsMap = new easy.rules.api.FactsMap({acheteur:{money:10}});
factsMap.put_4w9ihe$("acheteur",{money:10,inventory:[]})
factsMap.put_4w9ihe$("vendeur",{money:10,inventory:[]})

var itemType =new rulesBook.spaceMarket.model.ItemType("spaceship");

var facts=new rulesBook.spaceMarket.model.MarketFact()
facts.acheteur={...new rulesBook.spaceMarket.model.Player(), money:10}
facts.producer={...new rulesBook.spaceMarket.model.Planet(), products:[itemType]}
facts.vendeur={...new rulesBook.spaceMarket.model.Player(), money:10, inventory:[{...new rulesBook.spaceMarket.model.Item(itemType)}]}
facts.objet=itemType;
facts.product=itemType;
facts.prix=7;

console.log(rulesBook.spaceMarket)

console.log(rulesBook.spaceMarket.model.ruleProduce)

console.log("rulesBook.spaceMarket.ruleProduce")

console.log(new rulesBook.spaceMarket.model.SpaceRule());
console.log(Object.keys(new rulesBook.spaceMarket.model.SpaceRule()))
console.log(new rulesBook.spaceMarket.model.SpaceRule().runRules(facts));
console.log(rulesBook.spaceMarket.model.SpaceRule.$metadata$);

/*
rules.register_sddt03$(rulesBook.spaceMarket.model.ruleBuy);

rules.register_sddt03$(rulesBook.spaceMarket.model.ruleProduce);

facts.put_4w9ihe$("acheteur", new rulesBook.spaceMarket.model.Player("yoyo", [], 1000))
var toSell = new rulesBook.spaceMarket.model.Item()
facts.put_4w9ihe$("vendeur", new rulesBook.spaceMarket.model.Player("dede", [toSell], 1000))

facts.put_4w9ihe$("item", toSell)
facts.put_4w9ihe$("prix", 500)


console.log("easy.core")
console.log(rulesBook)
console.log(new easy.rules.core.InferenceRulesEngine().fire_eo0djw$(rules, facts));
*/
