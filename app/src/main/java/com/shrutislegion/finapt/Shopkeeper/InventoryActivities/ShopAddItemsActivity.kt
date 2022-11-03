package com.shrutislegion.finapt.Shopkeeper.InventoryActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.databinding.ActivityShopAddItemsBinding

class ShopAddItemsActivity : AppCompatActivity() {

    lateinit var view: ActivityShopAddItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_add_items)

        view = ActivityShopAddItemsBinding.inflate(layoutInflater)
        setContentView(view.root)

        view.addItem.setOnClickListener {
            if (view.itemName.text.toString() == "" || view.price.text.toString() == "" || view.quantity.text.toString() == "") {
                Toast.makeText(this, "Please fill all the required details.", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
                        view.itemName.setText(null)
                        view.price.setText(null)
                        view.quantity.setText(null)
                    }
                    else {
                        Toast.makeText(this, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}