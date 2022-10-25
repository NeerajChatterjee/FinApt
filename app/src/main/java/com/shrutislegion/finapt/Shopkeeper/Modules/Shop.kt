package com.shrutislegion.finapt.Shopkeeper.Modules

import android.annotation.SuppressLint

class Shop {

    private var profilepic: String  = ""
    private var shopkeeperName: String = ""
    private var shopName: String  = ""
    private var mail: String  = ""
    private var password: String  = ""
    private var shopId: String  = ""
    private var phone: String  = ""
    private var gstin: String = ""

    @SuppressLint("NotConstructor")
    constructor(profilepic: String, userName: String, mail: String, password: String, userId: String, lastMessage: String){
        this.profilepic = profilepic
        this.shopName = userName
        this.mail = mail
        this.password = password
        this.shopId = userId
        this.phone = lastMessage
        this.gstin = lastMessage
    }
    constructor()
    // SignUp Constructor
    constructor(userName: String, mail: String, password: String){
        this.shopkeeperName = userName
        this.mail = mail
        this.password = password
    }

    fun setProfilePic(profilepic: String) {
        this.profilepic = profilepic
    }
    fun getProfilePic(): String {
        return this.profilepic
    }

    fun setUserName(shopkeeperName: String) {
        this.shopkeeperName = shopkeeperName
    }
    fun getUserName(): String {
        return this.shopkeeperName
    }

    fun setShopName(shopName: String) {
        this.shopName = shopName
    }
    fun getShopName(): String {
        return this.shopName
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
        this.shopId = userId
    }
    fun getUserId(): String {
        return this.shopId
    }

    fun setPhone(lastMessage: String) {
        this.phone = lastMessage
    }
    fun getPhone(): String {
        return this.phone
    }
    fun setGstin(gstin: String) {
        this.gstin = gstin
    }
    fun getGstin(): String {
        return this.gstin
    }
}