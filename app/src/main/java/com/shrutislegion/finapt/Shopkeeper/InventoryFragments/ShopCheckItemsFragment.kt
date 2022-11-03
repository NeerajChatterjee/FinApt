package com.shrutislegion.finapt.Shopkeeper.InventoryFragments

import android.content.ClipData.Item
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Adapters.CustomerPendingRequestAdapter
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.inventoryViewAdapter
import kotlinx.android.synthetic.main.activity_shop_update_inventory.*
import kotlinx.android.synthetic.main.fragment_customer_pending_req.view.*
import kotlinx.android.synthetic.main.fragment_shop_check_items.*
import kotlinx.android.synthetic.main.fragment_shop_check_items.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopCheckItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopCheckItemsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var itemList: ArrayList<ItemInfo>
    lateinit var adapter: inventoryViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop_check_items, container, false)
//        var linearLayoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, true)
//        linearLayoutManager.stackFromEnd = true
//
//        view.inventoryView.layoutManager = linearLayoutManager
//        view.inventoryView.isNestedScrollingEnabled = false
        view.inventoryView.layoutManager = LinearLayoutManager(view.context)
        val auth = Firebase.auth
        itemList = ArrayList<ItemInfo>()
        var ref = FirebaseDatabase.getInstance().reference
            .child("All Items").child(auth.currentUser!!.uid)
        if(ref != null) {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        val value = (dss.getValue<ItemInfo>() as ItemInfo?)!!
                        //Toast.makeText(context, "value " + item.toString(), Toast.LENGTH_LONG).show()
                        itemList.add(value)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        // This will pass the ArrayList to our Adapter
        adapter = inventoryViewAdapter(itemList)
        // Setting the Adapter with the recyclerview
        view.inventoryView.adapter = adapter
        //Toast.makeText(context, "itemList is" + itemList.toString(), Toast.LENGTH_LONG).show()


        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopCheckItemsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopCheckItemsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}