package com.shrutislegion.finapt.Customer.Modules

data class CustomerInfo (
    var mail: String? = null,
    var password: String? = null,
    var id: String? = null,
    var name: String = "",
    var gender: String = "",
    var dob: String = "",
    var profilePic: String = "",
    var phone: String = "",
    var phoneVerified: Boolean = false,
    var emailVerified: Boolean = false,
)
