package com.shrutislegion.finapt.Customer

import android.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.shrutislegion.finapt.Customer.DashboardFragments.*
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.RegistrationActivity
import kotlinx.android.synthetic.main.activity_customer_dashboard.*

@Suppress("DEPRECATION")
class CustomerDashboard : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var bottomNav: ChipNavigationBar

    companion object {
        const val EXTRA_FRAGMENT = "name_extra"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)
        auth = Firebase.auth

        bottomNav = findViewById(R.id.bottom_nav)

        var frag = intent.getStringExtra(CustomerDashboard.EXTRA_FRAGMENT)

        val intent = Intent("finish_activity")
        sendBroadcast(intent)

        if(frag == "1") {
            bottomNav.setItemSelected(R.id.customerHome, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Track"
        }
        else if(frag == "2"){
            bottomNav.setItemSelected(R.id.customerPendingRequest,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPendingReqFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "3"){
            bottomNav.setItemSelected(R.id.customerPastBills,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPastBillsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "4"){
            bottomNav.setItemSelected(R.id.customerChat,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerChatFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "5"){
            bottomNav.setItemSelected(R.id.customerProfile,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerProfileFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Requests"
        }
        // By default the home page should be selected on opening the app
        else if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.customerHome,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Track"
        }

        // Listener on the bottomNav, and selecting the fragment according to their ids
        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it){
                R.id.customerHome ->{
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Track"
                }
                R.id.customerPendingRequest -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPendingReqFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Requests"
                }
                R.id.customerPastBills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPastBillsFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Requests"
                }
                R.id.customerChat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerChatFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Chats"
                }
                R.id.customerProfile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerProfileFragment()).commitAllowingStateLoss()
                    supportActionBar!!.title = "Profile"
                }
            }

        }
        signOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@CustomerDashboard, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}