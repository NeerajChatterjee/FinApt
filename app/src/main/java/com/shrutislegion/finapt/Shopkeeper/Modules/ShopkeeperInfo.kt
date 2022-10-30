package com.shrutislegion.finapt.Shopkeeper.Modules

data class ShopkeeperInfo(
    var mail:String? = null,
    var password:String = "",
    var id:String? = null,
    var name: String = "",
    var gender: String = "",
    var shopName:String = "",
    var gstIn:String = "",
    var profilePic:String = "",
    var phone:String = "",
    var phoneVerified: Boolean = false,
    var emailVerified: Boolean = false,
    var state: String = "",
    var city: String = "",
    var pincode: String = "",
    var address: String = "",
    var idToken: String = ""
) : java.io.Serializable
