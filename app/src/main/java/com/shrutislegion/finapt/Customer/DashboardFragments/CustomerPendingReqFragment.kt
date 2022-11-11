// This Fragment Displays the Pending Request sent by the Shopkeeper to the logged in customer.

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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Adapters.CustomerPendingRequestAdapter
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.databinding.FragmentCustomerPendingReqBinding
import kotlinx.android.synthetic.main.fragment_customer_pending_req.view.*

class CustomerPendingReqFragment : Fragment() {

    lateinit var adapter: CustomerPendingRequestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCustomerPendingReqBinding = FragmentCustomerPendingReqBinding.inflate(inflater, container, false)

        //var linearLayoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, true)
        binding.customerPendingRequestView.layoutManager = LinearLayoutManager(context)

        val list = ArrayList<BillInfo>()

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerPendingReqNestedScrollView.visibility = View.VISIBLE

        }, 2000)

        val auth = Firebase.auth
        val ref = FirebaseDatabase.getInstance().reference.child("Customer Pending Requests").child(auth.currentUser!!.uid)

        ref.orderByChild("date").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if (snapshot.exists()) {
                    for (dss in snapshot.children){
                        list.add((dss.getValue<BillInfo>())!!)
                        // Toast.makeText(view.context, list.toString(), Toast.LENGTH_SHORT).show()
                    }
                    list.reverse()
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })
        // This will pass the ArrayList to our Adapter
        adapter = CustomerPendingRequestAdapter(list)

        // Setting the Adapter with the recyclerview
        binding.customerPendingRequestView.adapter = adapter

        return binding.root
    }
}