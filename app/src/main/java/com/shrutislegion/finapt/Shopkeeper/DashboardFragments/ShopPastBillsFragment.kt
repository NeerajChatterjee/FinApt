package com.shrutislegion.finapt.Shopkeeper.DashboardFragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.ShopBillHistoryAdapter
import kotlinx.android.synthetic.main.fragment_shop_past_bills.view.*
import kotlinx.android.synthetic.main.fragment_shop_pending_req.view.*

class ShopPastBillsFragment : Fragment() {

    lateinit var bills: ArrayList<BillInfo>
    lateinit var adapter: ShopBillHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop_past_bills, container, false)
        bills = ArrayList<BillInfo>()
        view.acceptedReqView!!.layoutManager = LinearLayoutManager(view.context)

        val auth = Firebase.auth
        val database = Firebase.database
        val ref = FirebaseDatabase.getInstance().reference.child("Bills").child(auth.currentUser!!.uid)
            .orderByChild("date").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        val value = (dss.getValue<BillInfo>() as BillInfo?)!!
                        if(value.pending == false) bills.add(value)
                    }
                    bills.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        // This will pass the ArrayList to our Adapter
        adapter = ShopBillHistoryAdapter(bills)
        // Setting the Adapter with the recyclerview
        view.acceptedReqView!!.adapter =  adapter

        return view
    }
}