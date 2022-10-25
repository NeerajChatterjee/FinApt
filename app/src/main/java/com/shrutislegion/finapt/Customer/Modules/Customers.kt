package com.shrutislegion.finapt.Customer.Modules

import android.annotation.SuppressLint

class Customers {
    //String profilepic, userName, mail, password, userId, lastMessage;

    private var profilepic: String  = ""
    private var custName: String  = ""
    private var mail: String  = ""
    private var password: String  = ""
    private var custId: String  = ""
    private var phone: String  = ""
    @SuppressLint("NotConstructor")
    constructor(profilepic: String, userName: String, mail: String, password: String, userId: String, lastMessage: String){
        this.profilepic = profilepic
        this.custName = userName
        this.mail = mail
        this.password = password
        this.custId = userId
        this.phone = lastMessage
    }
    constructor()
    // SignUp Constructor
    constructor(userName: String, mail: String, password: String){
        this.custName = userName
        this.mail = mail
        this.password = password
    }

    fun setProfilePic(profilepic: String) {
        this.profilepic = profilepic
    }
    fun getProfilePic(): String {
        return this.profilepic
    }

    fun setUserName(userName: String) {
        this.custName = userName
    }
    fun getUserName(): String {
        return this.custName
    }

    fun setMail(mail: String) {
        this.mail = mail
    }
    fun getMail(): String {
        return this.mail
    }

    fun setPassword(password: String) {
        this.password = password
    }
    fun getPassword(): String {
        return this.password
    }

    fun setUserId(userId: String) {
        this.custId = userId
    }
    fun getUserId(): String {
        return this.custId
    }

    fun setPhone(lastMessage: String) {
        this.phone = lastMessage
    }
    fun getPhone(): String {
        return this.phone
    }
}