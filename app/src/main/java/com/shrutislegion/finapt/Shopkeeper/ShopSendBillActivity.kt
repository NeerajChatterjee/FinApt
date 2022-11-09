package com.shrutislegion.finapt.Shopkeeper

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.CreateBillAddItemAdapter
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.databinding.ActivityShopSendBillBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShopSendBillActivity : AppCompatActivity() {
    // Initialising required Variables
    private lateinit var binding: ActivityShopSendBillBinding
    lateinit var shopkeeper: ShopkeeperInfo
    val billID: String = ""
    val pending: Boolean = false
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
    var sentToUid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_send_bill)

        binding = ActivityShopSendBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items_category = listOf("Shopping", "Food", "Education", "Others")
        val adapter_category = ArrayAdapter(this, R.layout.list_item, items_category)
        (binding.textCategory as? AutoCompleteTextView)?.setAdapter(adapter_category)

        // adding items to the category dropdown
        binding.textCategory!!.setOnItemClickListener { parent, view, position, id ->

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
            binding.totalAmount!!.setText(it.toString())
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

        binding.addItemView?.adapter = adapter

        // getting current date
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val timeStamp = SimpleDateFormat("dd/M/yyyy hh:mm:ss").parse(currentDate)!!.time
        date = "$timeStamp"

        binding.customerPhoneNumber!!.doOnTextChanged{ text, start, before, count ->

            if(text!!.length == 10){

                // check if the number is unique
                val dialogPhone = ProgressDialog(this)

                // Dialog
                dialogPhone.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                dialogPhone.setTitle(getString(R.string.validating_number))
                dialogPhone.setMessage(getString(R.string.please_wait))
                dialogPhone.setCancelable(false)
                dialogPhone.setCanceledOnTouchOutside(false)

                dialogPhone.show()

                FirebaseDatabase.getInstance().reference.child("AllPhoneNumbers")
                    .addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                if(snapshot.child("$text").exists()){
                                    val value = snapshot.child("$text").value.toString()
                                    // Toast.makeText(this@ShopSendBillActivity, value.toString(), Toast.LENGTH_SHORT).show()
                                    FirebaseDatabase.getInstance().reference.child("Customers").addValueEventListener(object: ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if(snapshot.exists()) {
                                                if(snapshot.child(value).exists()) {
                                                    sentTo = "$text"
                                                    sentToUid = value
                                                    updatePhoneUI(binding)
                                                    dialogPhone.dismiss()
                                                }
                                                else {
                                                    // alert box show

                                                    binding.customerPhoneNumberLayout!!.endIconContentDescription = "Customer Doesn't Exists"

                                                    val builder = AlertDialog.Builder(this@ShopSendBillActivity)
                                                    builder.setTitle(getString(R.string.error))
                                                    builder.setMessage(getString(R.string.try_again_with_another_number))
                                                    builder.setIcon(R.drawable.ic_baseline_error_24)

                                                    builder.setPositiveButton("OK"){dialogInterface, which ->

                                                    }

                                                    // Create the AlertDialog
                                                    val alertDialog: AlertDialog = builder.create()
                                                    // Set other dialog properties
                                                    alertDialog.setCancelable(false)
                                                    dialogPhone.dismiss()
                                                    if(!this@ShopSendBillActivity.isFinishing){
                                                        alertDialog.show()
                                                    }
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })

                                }
                                else{
                                    // alert box show

                                    binding.customerPhoneNumberLayout!!.endIconContentDescription = "Customer Doesn't Exists"

                                    val builder = AlertDialog.Builder(this@ShopSendBillActivity)
                                    builder.setTitle(getString(R.string.error))
                                    builder.setMessage(getString(R.string.customer_doesnt_exists))
                                    builder.setIcon(R.drawable.ic_baseline_error_24)

                                    builder.setPositiveButton("OK"){dialogInterface, which ->

                                    }

                                    // Create the AlertDialog
                                    val alertDialog: AlertDialog = builder.create()
                                    // Set other dialog properties
                                    alertDialog.setCancelable(false)
                                    dialogPhone.dismiss()
                                    if(!this@ShopSendBillActivity.isFinishing){
                                        alertDialog.show()
                                    }
                                }
                            }
                            else{
                                // alert box show

                                binding.customerPhoneNumberLayout!!.endIconContentDescription = "Customer Doesn't Exists"

                                val builder = AlertDialog.Builder(this@ShopSendBillActivity)
                                builder.setTitle(getString(R.string.error))
                                builder.setMessage(getString(R.string.customer_doesnt_exists))
                                builder.setIcon(R.drawable.ic_baseline_error_24)

                                builder.setPositiveButton("OK"){dialogInterface, which ->

                                }

                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                dialogPhone.dismiss()
                                if(!this@ShopSendBillActivity.isFinishing){
                                    alertDialog.show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

            }
            else{

                binding.customerPhoneNumberLayout!!.endIconContentDescription = "Incorrect Number"
                binding.customerPhoneNumberLayout!!.setEndIconDrawable(R.drawable.ic_baseline_cancel_24)

                val states = arrayOf(
                    intArrayOf(android.R.attr.state_enabled), // enabled
                )

                val colors = intArrayOf(
                    Color.RED
                )

                val myList = ColorStateList(states, colors)

                binding.customerPhoneNumberLayout!!.setEndIconTintList(myList)
            }

        }
        // creating auth and database instances
        val auth = Firebase.auth
        val database = Firebase.database
        shopkeeperUid = auth.currentUser!!.uid

        var dialog = ProgressDialog(this)
        // Creating a dialog while the data is uploading
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Sending Bill")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        binding.send!!.setOnClickListener {
            if (binding.invoiceNo == null || category == "" || binding.totalAmount == null) {
                Toast.makeText(this, "Please Enter All The Required Details", Toast.LENGTH_SHORT)
            }
            else {
                dialog.show()
                // generating random key
                val key = database.reference.child("Bills").child(shopkeeperUid).push().key
                // getting data from user
                invoice = binding.invoiceNo!!.getText().toString().trim()
                totalAmount = binding.totalAmount!!.text.toString().trim()
                GSTIN = binding.shopkeeperGSTIn!!.text.toString().trim()
                Toast.makeText(this, itemList.toString(),Toast.LENGTH_SHORT).show()
                val bill: BillInfo = BillInfo (
                    billID = key, pending = true, false, sentTo = sentTo, date = date, totalAmount = totalAmount, shopkeeperUid = shopkeeperUid,  category = category , invoice = invoice, GSTIN = GSTIN, items = itemList)
                // uploading data to Firebase Database
                database.reference.child("Bills").child(shopkeeperUid).child(key!!).setValue(bill).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // uploading data to Customer Pending Requests node Firebase Database
                        database.reference.child("Customer Pending Requests").child(sentToUid).child(key!!).setValue(bill).addOnCompleteListener {
                            if (it.isSuccessful) {
                                dialog.dismiss()
                                Toast.makeText(this, "Bill Sent", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                dialog.dismiss()
                                Toast.makeText(this, "Some Error Occurred Please Try Again", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.dismiss()
                        //Toast.makeText(this, "Bill Sent", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        dialog.dismiss()
                        Toast.makeText(this, "Some Error Occurred Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


    }

    private fun updatePhoneUI(binding: ActivityShopSendBillBinding) {
        binding.customerPhoneNumberLayout!!.endIconContentDescription = "Unique Number"
        binding.customerPhoneNumberLayout.setEndIconDrawable(R.drawable.ic_baseline_check_circle_24)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
        )

        val colors = intArrayOf(
            Color.GREEN
        )

        val myList = ColorStateList(states, colors)

        binding.customerPhoneNumberLayout.setEndIconTintList(myList)
    }
}