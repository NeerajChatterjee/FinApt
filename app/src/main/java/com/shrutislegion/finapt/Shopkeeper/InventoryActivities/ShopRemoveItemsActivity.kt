package com.shrutislegion.finapt.Shopkeeper.InventoryActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.shrutislegion.finapt.databinding.ActivityShopRemoveItemsBinding

class ShopRemoveItemsActivity : AppCompatActivity() {

    lateinit var view: ActivityShopRemoveItemsBinding
    lateinit var itemList: ArrayList<ItemInfo>
    lateinit var adapter: InventoryRemoveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_remove_items)

        view = ActivityShopRemoveItemsBinding.inflate(layoutInflater)
        setContentView(view.root)

        view.removeView.layoutManager = LinearLayoutManager(this)
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

    }
}