package com.shrutislegion.finapt.Shopkeeper

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.RegistrationActivity
import com.shrutislegion.finapt.Shopkeeper.DashboardFragments.*
import com.shrutislegion.finapt.ShowAllUsersFragment
import com.shrutislegion.finapt.databinding.ActivityShopkeeperDashboardBinding
import com.shrutislegion.finapt.databinding.FragmentShopChatBinding


class ShopkeeperDashboard : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var bottomNav: ChipNavigationBar

    var registrationActivityObject: RegistrationActivity = RegistrationActivity()

    companion object {
        const val EXTRA_FRAGMENT = "name_extra"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_dashboard)
        auth = Firebase.auth

        val binding: ActivityShopkeeperDashboardBinding = ActivityShopkeeperDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if(registrationActivityObject.registrationActivity != null){
//            registrationActivityObject.registrationActivity!!.finish()
//        }

        val intent = Intent("finish_activity")
        sendBroadcast(intent)

        bottomNav = findViewById(R.id.bottom_nav)

        binding.messageShopFragmentFAB.setOnClickListener {

            //  show all the users excluding the current signed in user
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.shop_fragment_container, ShowAllUsersFragment())
            transaction.addToBackStack(null)
            transaction.commit()

            binding.messageShopFragmentFAB.visibility = View.GONE
        }

        var frag = intent.getStringExtra(ShopkeeperDashboard.EXTRA_FRAGMENT)

        if(frag == "1") {
            bottomNav.setItemSelected(R.id.shopHome, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Home"
        }
        else if(frag == "2"){
            bottomNav.setItemSelected(R.id.shopPendingRequest,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPendingReqFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "3"){
            bottomNav.setItemSelected(R.id.shopPastBills,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPastBillsFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Past Bills"
        }
        else if(frag == "4"){
            bottomNav.setItemSelected(R.id.shopChat,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopChatFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.VISIBLE
            supportActionBar!!.title = "Chat"
        }
        else if(frag == "5"){
            bottomNav.setItemSelected(R.id.shopProfile,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Profile"
        }
        // By default the home page should be selected on opening the app
        else if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.shopHome,true)
            supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
            binding.messageShopFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Home"
        }

        // Listener on the bottomNav, and selecting the fragment according to their ids
        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it){
                R.id.shopHome ->{
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopHomeFragment()).commitAllowingStateLoss()
                    binding.messageShopFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Home"
                }
                R.id.shopPendingRequest -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPendingReqFragment()).commitAllowingStateLoss()
                    binding.messageShopFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Requests"
                }
                R.id.shopPastBills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopPastBillsFragment()).commitAllowingStateLoss()
                    binding.messageShopFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Bills"
                }
                R.id.shopChat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopChatFragment()).commitAllowingStateLoss()
                    binding.messageShopFragmentFAB.visibility = View.VISIBLE
                    supportActionBar!!.title = "Chats"
                }
                R.id.shopProfile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.shop_fragment_container, ShopProfileFragment()).commitAllowingStateLoss()
                    binding.messageShopFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Profile"
                }
            }
        }
    }
}