package com.shrutislegion.finapt.Customer.DashboardFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class CustomerHomeFragment : Fragment() {

    lateinit var adapter: CustomerHomeExpenseCategoryAdapter
    lateinit var map: HashMap<String, Int>
    lateinit var binding: FragmentCustomerHomeBinding
    lateinit var auth: FirebaseAuth

    // To override LinearLayoutManager by Wrapper, as it crashes the application sometimes
    inner class LinearLayoutManagerWrapper : LinearLayoutManager {
        constructor(context: Context?) : super(context) {}
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.pieChart.setOnClickListener {
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

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerHomeConstraintLayout.visibility = View.VISIBLE
            binding.customerAddSelfExpFAB.visibility = View.VISIBLE

        }
        , 2000)

        map = HashMap<String, Int>()

        val ref = FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)

        binding.expenseCategoryView.layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
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
                Toast.makeText(context, map.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })

        binding.customerAddSelfExpFAB.setOnClickListener {

            startActivity(Intent(context, CustomerAddExpenseActivity::class.java))

        }

        adapter = CustomerHomeExpenseCategoryAdapter("All Time", map)
        binding.expenseCategoryView.adapter = adapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.customer_menu_overflow, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.today -> {
                binding.progressBarCustomerHome.visibility = View.VISIBLE
                binding.customerHomeConstraintLayout.visibility = View.GONE
                binding.customerAddSelfExpFAB.visibility = View.GONE
                updateByToday()
            }

            R.id.weekly -> {
                binding.progressBarCustomerHome.visibility = View.VISIBLE
                binding.customerHomeConstraintLayout.visibility = View.GONE
                binding.customerAddSelfExpFAB.visibility = View.GONE
                updateByWeekly()
            }
            R.id.monthly -> {
                binding.progressBarCustomerHome.visibility = View.VISIBLE
                binding.customerHomeConstraintLayout.visibility = View.GONE
                binding.customerAddSelfExpFAB.visibility = View.GONE
                updateByMonthly()
            }
            R.id.allTime -> {
                binding.progressBarCustomerHome.visibility = View.VISIBLE
                binding.customerHomeConstraintLayout.visibility = View.GONE
                binding.customerAddSelfExpFAB.visibility = View.GONE
                updateByAllTime()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateByAllTime() {

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerHomeConstraintLayout.visibility = View.VISIBLE
            binding.customerAddSelfExpFAB.visibility = View.VISIBLE

        },2000)

        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        map.clear()

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
                    // Toast.makeText(context, map.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("tag", error.message)
                }

            })

        adapter = CustomerHomeExpenseCategoryAdapter("All Time", map)
        binding.expenseCategoryView.adapter = adapter
    }

    private fun updateByMonthly() {

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerHomeConstraintLayout.visibility = View.VISIBLE
            binding.customerAddSelfExpFAB.visibility = View.VISIBLE

        },2000)

        val calendar = Calendar.getInstance()
        //rollback 30 days
        calendar.add(Calendar.DAY_OF_YEAR, -30)

        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        map.clear()

                        for (dss in snapshot.children) {
                            val expense = dss.key
                            var total = 0
                            for (values in dss.children) {
                                val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()

                                val sentDateLong = (values.getValue<BillInfo>() as BillInfo).date.toLong()

                                if(sentDateLong >= calendar.timeInMillis){
                                    total += amount
                                }
                            }

                            map[expense.toString()] = total
                        }
                        adapter.notifyDataSetChanged()
                    }
                    // Toast.makeText(context, map.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("tag", error.message)
                }

            })

        adapter = CustomerHomeExpenseCategoryAdapter("Monthly", map)
        binding.expenseCategoryView.adapter = adapter


    }

    @SuppressLint("SimpleDateFormat")
    private fun updateByWeekly() {

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerHomeConstraintLayout.visibility = View.VISIBLE
            binding.customerAddSelfExpFAB.visibility = View.VISIBLE

        },2000)

        val calendar = Calendar.getInstance()
        //rollback 7 days
        calendar.add(Calendar.DAY_OF_YEAR, -7)

        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        map.clear()

                        for (dss in snapshot.children) {
                            val expense = dss.key
                            var total = 0
                            for (values in dss.children) {
                                val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()

                                val sentDateLong = (values.getValue<BillInfo>() as BillInfo).date.toLong()

                                if(sentDateLong >= calendar.timeInMillis){
                                    total += amount
                                }
                            }

                            map[expense.toString()] = total
                        }
                        adapter.notifyDataSetChanged()
                    }
                    // Toast.makeText(context, map.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("tag", error.message)
                }

            })

        adapter = CustomerHomeExpenseCategoryAdapter("Weekly", map)
        binding.expenseCategoryView.adapter = adapter

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateByToday() {

        Handler(Looper.getMainLooper()).postDelayed({

            binding.progressBarCustomerHome.visibility = View.GONE
            binding.customerHomeConstraintLayout.visibility = View.VISIBLE
            binding.customerAddSelfExpFAB.visibility = View.VISIBLE

        },2000)

        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date(Calendar.getInstance().timeInMillis))

        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        map.clear()

                        for (dss in snapshot.children) {
                            val expense = dss.key
                            var total = 0
                            for (values in dss.children) {
                                val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()

                                val sentDateLong = (values.getValue<BillInfo>() as BillInfo).date.toLong()
                                val sentDate = SimpleDateFormat("dd/MM/yyyy").format(Date(sentDateLong))

                                if(sentDate == currentDate){
                                    total += amount
                                }
                            }

                            map[expense.toString()] = total
                        }
                        adapter.notifyDataSetChanged()
                    }
                    // Toast.makeText(context, map.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("tag", error.message)
                }

            })

        adapter = CustomerHomeExpenseCategoryAdapter("Today", map)
        binding.expenseCategoryView.adapter = adapter
    }
}