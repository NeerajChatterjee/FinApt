package com.shrutislegion.finapt.Customer.DashboardFragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Adapters.CustomerHomeExpenseCategoryAdapter
import com.shrutislegion.finapt.Customer.CustomerAddExpenseActivity
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Customer.PieChartActivity
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.databinding.FragmentCustomerHomeBinding
import kotlinx.android.synthetic.main.activity_shop_chat_details.view.*
import kotlinx.android.synthetic.main.fragment_customer_home.*
import kotlinx.android.synthetic.main.fragment_customer_home.view.*
import kotlinx.android.synthetic.main.item_home_category.*
import kotlinx.android.synthetic.main.item_home_category.view.*

class CustomerHomeFragment : Fragment() {
    lateinit var adapter: CustomerHomeExpenseCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCustomerHomeBinding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        val auth = Firebase.auth
        binding.pieChart!!.setOnClickListener {
            val intent = Intent(context, PieChartActivity::class.java)
            startActivity(intent)
        }
        FirebaseDatabase.getInstance().reference.child("Customers").child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.nameText.text = snapshot.getValue<CustomerInfo>()!!.name
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })

        val map = HashMap<String, Int>()
        val ref = FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)

        binding.expenseCategoryView.layoutManager = LinearLayoutManager(context)

        ref.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //val value = snapshot.key
                    //Toast.makeText(view.context, value.toString(), Toast.LENGTH_SHORT).show()
                    for (dss in snapshot.children) {
                        val expense = dss.key
                        var total = 0
                        for (values in dss.children) {
                            val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()
                            total += amount
                        }
                        map[expense.toString()] = total
                    }
                    adapter.notifyDataSetChanged()
                }
                //Toast.makeText(view.context, map.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })

        binding.customerAddSelfExpFAB.setOnClickListener {

            startActivity(Intent(context, CustomerAddExpenseActivity::class.java))

        }

        adapter = CustomerHomeExpenseCategoryAdapter(map)
        binding.expenseCategoryView.adapter = adapter

        return binding.root
    }
}