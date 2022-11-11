package com.shrutislegion.finapt.Customer.DashboardFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Adapters.CustomerPastBillsAdapter
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.ShopBillHistoryAdapter
import com.shrutislegion.finapt.Shopkeeper.DashboardFragments.ShopPastBillsFragment
import com.shrutislegion.finapt.databinding.FragmentCustomerPastBillsBinding
import kotlinx.android.synthetic.main.fragment_customer_past_bills.view.*
import kotlinx.android.synthetic.main.fragment_shop_past_bills.view.*

class CustomerPastBillsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bills: ArrayList<BillInfo>
    lateinit var adapter: CustomerPastBillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCustomerPastBillsBinding = FragmentCustomerPastBillsBinding.inflate(inflater, container, false)
        bills = ArrayList<BillInfo>()

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerPastBillsReqNestedScrollView.visibility = View.VISIBLE

        }, 2000)

        binding.pastBillsView.layoutManager = LinearLayoutManager(context)

        val auth = Firebase.auth
        FirebaseDatabase.getInstance().reference.child("All Expenses").child(auth.currentUser!!.uid)
            .orderByChild("date")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        bills.clear()
                        for (dss in snapshot.children) {
                            val value = dss.getValue<BillInfo>()!!
                            if (value.accepted == true) bills.add(value)
                        }
                        bills.reverse()
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("tag", error.message)
                }
            })
        // This will pass the ArrayList to our Adapter
        adapter = CustomerPastBillsAdapter(bills)
        // Setting the Adapter with the recyclerview
        binding.pastBillsView!!.adapter =  adapter

        return binding.root
    }
}