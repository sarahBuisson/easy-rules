package spaceMarket.model

data class ItemType(val name: String)

data class Item(val type: ItemType, val quantity: Int = 1)

data class Planet(val name:String,val inventory:MutableList<Item> = ArrayList<Item>());

data class Player(val name:String,val inventory:MutableList<Item> = ArrayList<Item>());
