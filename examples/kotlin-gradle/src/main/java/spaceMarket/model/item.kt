package spaceMarket.model

import kotlinx.serialization.Serializable
@Serializable
data class ItemType(val name: String)
@Serializable
data class Item(val type: ItemType, val quantity: Int = 1)
@Serializable
data class Planet(
    val name: String,
    var inventory: Array<Item> = arrayOf(),
    var products: Array<ItemType> = arrayOf()
) {

};
@Serializable
data class Player(val name: String, var inventory: Array<Item> = arrayOf(), var money: Double) {

};

@Serializable
class MarketFact{
    lateinit var product: ItemType
    lateinit var  objet: Item
    lateinit var  vendeur: Player
    lateinit var  acheteur: Player
    lateinit var  producer: Planet
    var prix:Double = 0.0
}
