package com.shrutislegion.finapt.Shopkeeper

import android.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Customer.DashboardFragments.*
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Registration_Activity
import com.shrutislegion.finapt.Shopkeeper.DashboardFragments.*
import kotlinx.android.synthetic.main.activity_customer_dashboard.*

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

        var frag = intent.getStringExtra(CustomerDashboard.EXTRA_FRAGMENT)

        if(frag == "1") {
            bottomNav.setItemSelected(R.id.home, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Home"
        }
        else if(frag == "2"){
            bottomNav.setItemSelected(R.id.pendingRequest,true)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PendingRequestFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "3"){
            bottomNav.setItemSelected(R.id.pastBills,true)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PastBillsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Past Bills"
        }
        else if(frag == "4"){
            bottomNav.setItemSelected(R.id.shopChat,true)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShopChatFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Chat"
        }
        else if(frag == "5"){
            bottomNav.setItemSelected(R.id.shopProfile,true)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Profile"
        }
        // By default the home page should be selected on opening the app
        else if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.track,true)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TrackFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Track"
        }

        // Listener on the bottomNav, and selecting the fragment according to their ids
        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it){
                R.id.home ->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Home"
                }
                R.id.pendingRequest -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PendingRequestFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Requests"
                }
                R.id.pastBills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PastBillsFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Past Bills"
                }
                R.id.chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShopChatFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Chats"
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Profile"
                }
            }

            signOut.setOnClickListener {
                auth.signOut()
                val intent = Intent(this@ShopkeeperDashboard, Registration_Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}