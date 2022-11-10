package com.shrutislegion.finapt.Shopkeeper

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.AppCompat
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.ShopBillHistoryAdapter
import com.shrutislegion.finapt.databinding.ActivityShopBillsHistoryBinding

class ShopBillsHistoryActivity : AppCompat() {
    lateinit var binding: ActivityShopBillsHistoryBinding
    lateinit var bills: ArrayList<BillInfo>
    lateinit var adapter: ShopBillHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_bills_history)

        binding = ActivityShopBillsHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bills = ArrayList<BillInfo>()
        binding.billsHistory!!.layoutManager = LinearLayoutManager(this@ShopBillsHistoryActivity)

        val auth = Firebase.auth
        val database = Firebase.database
        val ref = FirebaseDatabase.getInstance().reference.child("Bills").child(auth.currentUser!!.uid)
            .orderByChild("date").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        val value = (dss.getValue<BillInfo>() as BillInfo?)!!
                        bills.add(value)
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
        binding.billsHistory!!.adapter = adapter
    }
}