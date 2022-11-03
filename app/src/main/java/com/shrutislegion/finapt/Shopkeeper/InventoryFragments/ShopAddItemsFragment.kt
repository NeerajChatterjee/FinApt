package com.shrutislegion.finapt.Shopkeeper.InventoryFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import kotlinx.android.synthetic.main.fragment_shop_add_items.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopAddItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopAddItemsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_shop_add_items, container, false)

        view.addItem.setOnClickListener {
            if (view.itemName.text.toString() == "" || view.price.text.toString() == "" || view.quantity.text.toString() == "") {
                Toast.makeText(context, "Please fill all the required details.", Toast.LENGTH_SHORT).show()
            }
            else {
                val auth = Firebase.auth
                val database = Firebase.database
                val reference = database.reference
                val key = reference.child("AllItems").child(auth.currentUser!!.uid).push().key
                // getting data from user
                val itemInfo: ItemInfo = ItemInfo(
                    itemID = key!!,
                    itemName = view.itemName.text.toString(),
                    itemPrice = Integer.parseInt(view.price.text.toString()),
                    itemQuantity = Integer.parseInt((view.quantity.text.toString()))
                )
                // uploading data on Firebase
                reference.child("All Items").child(auth.currentUser!!.uid).child(key).setValue(itemInfo).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Item Added Successfully", Toast.LENGTH_LONG).show()
                        view.itemName.setText(null)
                        view.price.setText(null)
                        view.quantity.setText(null)
                    }
                    else {
                        Toast.makeText(context, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopAddItemsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopAddItemsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}