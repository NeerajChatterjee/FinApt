package com.shrutislegion.finapt.Shopkeeper.Modules

data class ShopkeeperInfo(
    var mail:String? = null,
    var password:String? = null,
    var id:String? = null,
    var shopkeeperName:String = "",
    var gender: String = "",
    var shopName:String = "",
    var gstIn:String = "",
    var location: String = "",
    var profilePic:String = "",
    var phone:String = "",
    val phoneVerified: Boolean = false,
    var emailVerified: Boolean = false,
)
