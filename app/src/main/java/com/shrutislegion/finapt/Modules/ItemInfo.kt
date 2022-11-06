package com.shrutislegion.finapt.Modules

data class ItemInfo (
    val itemID: String? = "",
    val itemName: String? = "",
    val itemPrice: Int? = 0,
    var itemQuantity: Int? = 0,
)