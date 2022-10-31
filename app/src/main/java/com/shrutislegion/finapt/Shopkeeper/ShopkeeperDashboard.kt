package com.shrutislegion.finapt.Shopkeeper

import android.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.DashboardFragments.*

class ShopkeeperDashboard : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var bottomNav: ChipNavigationBar

    companion object {
        const val EXTRA_FRAGMENT = "name_extra"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_dashboard)
        auth = Firebase.auth

        bottomNav = findViewById(R.id.bottom_nav)

        var frag = intent.getStringExtra(ShopkeeperDashboard.EXTRA_FRAGMENT)

        if(frag == "1") {
            bottomNav.setItemSelected(R.id.shopHome, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Home"
        }
        else if(frag == "2"){
            bottomNav.setItemSelected(R.id.shopPendingRequest,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPendingReqFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "3"){
            bottomNav.setItemSelected(R.id.shopPastBills,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPastBillsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Past Bills"
        }
        else if(frag == "4"){
            bottomNav.setItemSelected(R.id.shopChat,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopChatFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Chat"
        }
        else if(frag == "5"){
            bottomNav.setItemSelected(R.id.shopProfile,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Profile"
        }
        // By default the home page should be selected on opening the app
        else if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.shopHome,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Home"
        }

        // Listener on the bottomNav, and selecting the fragment according to their ids
        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it){
                R.id.shopHome ->{
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Home"
                }
                R.id.shopPendingRequest -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPendingReqFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Requests"
                }
                R.id.shopPastBills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPastBillsFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Bills"
                }
                R.id.shopChat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopChatFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Chats"
                }
                R.id.shopProfile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Profile"
                }
            }
        }
    }
}