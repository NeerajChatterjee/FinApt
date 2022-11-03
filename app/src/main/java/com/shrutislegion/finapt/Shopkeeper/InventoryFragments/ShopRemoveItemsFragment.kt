package com.shrutislegion.finapt.Shopkeeper.InventoryFragments

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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.InventoryRemoveAdapter
import com.shrutislegion.finapt.Shopkeeper.Adapters.inventoryViewAdapter
import kotlinx.android.synthetic.main.fragment_shop_check_items.view.*
import kotlinx.android.synthetic.main.fragment_shop_remove_items.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopRemoveItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopRemoveItemsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var itemList: ArrayList<ItemInfo>
    lateinit var adapter: InventoryRemoveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop_remove_items, container, false)
        view.removeView.layoutManager = LinearLayoutManager(view.context)
        val auth = Firebase.auth
        itemList = ArrayList<ItemInfo>()
        val ref = FirebaseDatabase.getInstance().reference
            .child("All Items").child(auth.currentUser!!.uid)
        if(ref != null) {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
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
        adapter = InventoryRemoveAdapter(itemList)
        // Setting the Adapter with the recyclerview
        view.removeView.adapter = adapter
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
         * @return A new instance of fragment ShopRemoveItemsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopRemoveItemsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}