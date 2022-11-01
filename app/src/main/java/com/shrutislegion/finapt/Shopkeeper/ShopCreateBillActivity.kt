package com.shrutislegion.finapt.Shopkeeper

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.databinding.ActivityCustomerSignUpBinding
import com.shrutislegion.finapt.databinding.ActivityShopCreateBillBinding
import kotlinx.android.synthetic.main.activity_customer_create_profile.*
import kotlinx.android.synthetic.main.activity_shop_create_bill.*
import okhttp3.internal.Util.format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ShopCreateBillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopCreateBillBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_create_bill)

        binding = ActivityShopCreateBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items_category = listOf("Shopping", "Food", "Education", "Others")
        val adapter_category = ArrayAdapter(this, R.layout.list_item, items_category)
        (binding.textCategory as? AutoCompleteTextView)?.setAdapter(adapter_category)

        binding.textCategory.setOnItemClickListener { parent, view, position, id ->

            val item:String = parent.getItemAtPosition(position).toString()
            category = item

        }

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val timeStamp = SimpleDateFormat("dd/M/yyyy hh:mm:ss").parse(currentDate)!!.time
        date = "$timeStamp"
        items.add(0, "one")
        items.add(1, "one")
        items.add(2, "one")
        val auth = Firebase.auth
        val database = Firebase.database
        shopkeeperUid = auth.currentUser!!.uid
        val custReference = database.reference.child("Shopkeepers").child(auth.currentUser!!.uid)
        custReference.addListenerForSingleValueEvent(object : ValueEventListener {
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
                val key = database.reference.child("Bills").child(shopkeeperUid).push().key
                invoice = binding.invoiceNo.getText().toString().trim()
                totalAmount = binding.totalAmount.text.toString().trim()
                GSTIN = binding.shopkeeperGSTIn.text.toString().trim()
                val bill: BillInfo = BillInfo (
                    billID = key, pending = false, sentTo = shopkeeperUid, date = date, totalAmount = totalAmount, shopkeeperUid = shopkeeperUid,  category = category , invoice = invoice, GSTIN = GSTIN, items = items)

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