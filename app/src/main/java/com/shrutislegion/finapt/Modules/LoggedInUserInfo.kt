package com.shrutislegion.finapt.Modules

data class LoggedInUserInfo(

    var name: String? = null,
    var mail: String? = null,
    var id: String?= null,
    var photoUrl: String?= null,
    var lastMessage: String? = null,

): java.io.Serializable
