package com.shrutislegion.finapt.Customer.DashboardFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import kotlinx.android.synthetic.main.activity_shop_chat_details.view.*
import kotlinx.android.synthetic.main.fragment_customer_home.*
import kotlinx.android.synthetic.main.fragment_customer_home.view.*
import kotlinx.android.synthetic.main.item_home_category.*
import kotlinx.android.synthetic.main.item_home_category.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var adapter: CustomerHomeExpenseCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customer_home, container, false)
        val auth = Firebase.auth
        val snapshot = FirebaseDatabase.getInstance().reference.child("Customers").child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null){
                    view.nameText.text = snapshot.getValue<CustomerInfo>()!!.name
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val map = HashMap<String, Int>()
        val ref = FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(auth.currentUser!!.uid)
        view.expenseCategoryView.layoutManager = LinearLayoutManager(view.context)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //val value = snapshot.key
                    //Toast.makeText(view.context, value.toString(), Toast.LENGTH_SHORT).show()
                    for (dss in snapshot.children) {
                        val expense = dss.key
                        var total = 0
                        for (values in dss.children) {
                            val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()
                            total = total + amount
                        }
                        map[expense.toString()] = total
                    }
                    adapter.notifyDataSetChanged()
                }
                //Toast.makeText(view.context, map.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        adapter = CustomerHomeExpenseCategoryAdapter(map)
        view.expenseCategoryView.adapter = adapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}