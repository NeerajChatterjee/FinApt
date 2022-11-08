// Shopkeeper Creates Bill for own Reference

package com.shrutislegion.finapt.Shopkeeper

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.CreateBillAddItemAdapter
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.databinding.ActivityShopCreateBillBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class ShopCreateBillActivity : AppCompatActivity() {

    // Initialising required Variables
    private lateinit var binding: ActivityShopCreateBillBinding
    lateinit var shopkeeper: ShopkeeperInfo
    val billID: String = ""
    val pending: Boolean = false
    val accepted: Boolean = true
    var sentTo: String = ""
    var date: String = ""
    var totalAmount: String = ""
    var shopkeeperUid: String = ""
    var category: String = ""
    var invoice: String = ""
    var GSTIN: String = ""
    var items = ArrayList<String>()
    lateinit var itemList: ArrayList<ItemInfo>
    lateinit var adapter: CreateBillAddItemAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_create_bill)

        binding = ActivityShopCreateBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items_category = listOf("Shopping", "Food", "Education", "Others")
        val adapter_category = ArrayAdapter(this, R.layout.list_item, items_category)
        (binding.textCategory as? AutoCompleteTextView)?.setAdapter(adapter_category)

        // adding items to the category dropdown
        binding.textCategory.setOnItemClickListener { parent, view, position, id ->

            val item:String = parent.getItemAtPosition(position).toString()
            category = item

        }

        // ADD VIEW THROUGH RECYCLER VIEW
        itemList = ArrayList<ItemInfo>()
        binding.addItemView?.layoutManager = LinearLayoutManager(this)
        val option = ArrayList<String>()
        option.add("view")
        val adapter = CreateBillAddItemAdapter(option)
        { it, selectedList ->
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            binding.totalAmount.setText(it.toString())
            itemList.add(selectedList)
            //Toast.makeText(this, itemList.toString(), Toast.LENGTH_SHORT).show()

        }

        binding.addMoreItems!!.setOnClickListener {
            option.add("view")
            adapter.notifyDataSetChanged()
        }
        binding.removeItems!!.setOnClickListener {
            if(option.size >1) {
                option.removeAt(option.size - 1)
                adapter.notifyDataSetChanged()
            }
            else {
                Toast.makeText(this, "At least one Item is required.", Toast.LENGTH_SHORT).show()
            }
        }


        // getting current date
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val timeStamp = SimpleDateFormat("dd/M/yyyy hh:mm:ss").parse(currentDate)!!.time
        date = "$timeStamp"
        items.add(0, "one")
        items.add(1, "one")
        items.add(2, "one")
        // creating auth and database instances
        val auth = Firebase.auth
        val database = Firebase.database
        shopkeeperUid = auth.currentUser!!.uid
        val ref = database.reference.child("Shopkeepers").child(auth.currentUser!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    shopkeeper = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!
                    sentTo = shopkeeperUid
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        binding.addItemView?.adapter = adapter
        var dialog = ProgressDialog(this)
        // Creating a dialog while the data is uploading
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Creating Bill")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        binding.submitBillDetails.setOnClickListener {
            if (binding.invoiceNo == null || category == "" || binding.totalAmount == null) {
                Toast.makeText(this, "Please Enter All The Required Details", Toast.LENGTH_SHORT)
            }
            else {
                dialog.show()
                // generating random key
                val key = database.reference.child("Bills").child(shopkeeperUid).push().key
                // getting data from user
                invoice = binding.invoiceNo.getText().toString().trim()
                totalAmount = binding.totalAmount.text.toString().trim()
                GSTIN = binding.shopkeeperGSTIn.text.toString().trim()
                Toast.makeText(this, itemList.toString(),Toast.LENGTH_SHORT).show()
                val bill: BillInfo = BillInfo (
                    billID = key, pending = false, accepted = true, sentTo = shopkeeperUid, date = date, totalAmount = totalAmount, shopkeeperUid = shopkeeperUid,  category = category , invoice = invoice, GSTIN = GSTIN, items = itemList)
                // uploading data to Firebase Database
                database.reference.child("Bills").child(shopkeeperUid).child(key!!).setValue(bill).addOnCompleteListener {
                    if (it.isSuccessful) {
                        dialog.dismiss()
                        Toast.makeText(this, "Bill Created", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        dialog.dismiss()
                        Toast.makeText(this, "Some Error Occurred Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}
//        - Bills
//            - Shopkeeper uid#1
//                    - Bill key#1
//                        - senTo: String
//                        - rest bill details
//                    - Bill key#2
//            - Shopkeeper uid#2
//                    - Bill key#1
//                        - senTo: String
//                        - rest details