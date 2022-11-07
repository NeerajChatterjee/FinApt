// User gets directed here after creating profile, it contains the Bottom Navigation

package com.shrutislegion.finapt.Customer

import android.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.shrutislegion.finapt.Customer.DashboardFragments.*
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.RegistrationActivity
import com.shrutislegion.finapt.ShowAllUsersFragment
import com.shrutislegion.finapt.databinding.ActivityCustomerDashboardBinding
import kotlinx.android.synthetic.main.activity_customer_dashboard.*

@Suppress("DEPRECATION", "UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
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

        val binding: ActivityCustomerDashboardBinding = ActivityCustomerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = findViewById(R.id.bottom_nav)

        binding.messageCustomerFragmentFAB!!.setOnClickListener {

            //  show all the users excluding the current signed in user
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.customer_fragment_container, ShowAllUsersFragment())
            transaction.addToBackStack(null)
            transaction.commit()

            binding.messageCustomerFragmentFAB.visibility = View.GONE
        }

        val frag = intent.getStringExtra(CustomerDashboard.EXTRA_FRAGMENT)

        val intent = Intent("finish_activity")
        sendBroadcast(intent)

        if(frag == "1") {
            bottomNav.setItemSelected(R.id.customerHome, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Home"
        }
        else if(frag == "2"){
            bottomNav.setItemSelected(R.id.customerPendingRequest,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPendingReqFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Requests"
        }
        else if(frag == "3"){
            bottomNav.setItemSelected(R.id.customerPastBills,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPastBillsFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Past Bills"
        }
        else if(frag == "4"){
            bottomNav.setItemSelected(R.id.customerChat,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerChatFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.VISIBLE
            supportActionBar!!.title = "Chats"
        }
        else if(frag == "5"){
            bottomNav.setItemSelected(R.id.customerProfile,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerProfileFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Profile"
        }
        // By default the home page should be selected on opening the app
        else if(savedInstanceState==null){
            bottomNav.setItemSelected(R.id.customerHome,true)
            supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
            binding.messageCustomerFragmentFAB.visibility = View.GONE
            supportActionBar!!.title = "Home"
        }

        // Listener on the bottomNav, and selecting the fragment according to their ids
        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it){
                R.id.customerHome ->{
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerHomeFragment()).commitAllowingStateLoss()
                    binding.messageCustomerFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Home"
                }
                R.id.customerPendingRequest -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPendingReqFragment()).commitAllowingStateLoss()
                    binding.messageCustomerFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Requests"
                }
                R.id.customerPastBills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerPastBillsFragment()).commitAllowingStateLoss()
                    binding.messageCustomerFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Bills"
                }
                R.id.customerChat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerChatFragment()).commitAllowingStateLoss()
                    binding.messageCustomerFragmentFAB.visibility = View.VISIBLE
                    supportActionBar!!.title = "Chats"
                }
                R.id.customerProfile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.customer_fragment_container, CustomerProfileFragment()).commitAllowingStateLoss()
                    binding.messageCustomerFragmentFAB.visibility = View.GONE
                    supportActionBar!!.title = "Profile"
                }
            }

        }
    }
}