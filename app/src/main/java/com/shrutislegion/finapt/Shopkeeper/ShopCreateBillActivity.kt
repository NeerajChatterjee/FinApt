// Shopkeeper Creates Bill for own Reference

package com.shrutislegion.finapt.Shopkeeper

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.Notifications.channelId
import com.shrutislegion.finapt.Notifications.notificationId
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.CreateBillAddItemAdapter
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.SplashScreen
import com.shrutislegion.finapt.databinding.ActivityShopCreateBillBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class ShopCreateBillActivity : AppCompatActivity() {

    // Initialising required Variables
    private lateinit var binding: ActivityShopCreateBillBinding
    lateinit var shopkeeper: ShopkeeperInfo
    lateinit var auth: FirebaseAuth
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_create_bill)

        binding = ActivityShopCreateBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        createNotificationChannel()

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
        // getting current date
        date = Calendar.getInstance().timeInMillis.toString()

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
            }

        })


        binding.addItemView?.adapter = adapter
        val dialog = ProgressDialog(this)
        // Creating a dialog while the data is uploading
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle(getString(R.string.creating_bill))
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        binding.submitBillDetails.setOnClickListener {
            if (binding.invoiceNo.text == null || category == "" || binding.totalAmount.text == null) {
                Toast.makeText(this, "Please Enter All The Required Details", Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.show()
                // generating random key
                val key = database.reference.child("Bills").child(shopkeeperUid).push().key
                // getting data from user
                invoice = binding.invoiceNo.text.toString().trim()
                totalAmount = binding.totalAmount.text.toString().trim()
                GSTIN = binding.shopkeeperGSTIn.text.toString().trim()

                val bill: BillInfo = BillInfo (
                    billID = key, pending = false, accepted = true, sentTo = shopkeeperUid, date = date, totalAmount = totalAmount, shopkeeperUid = shopkeeperUid,  category = category , invoice = invoice, GSTIN = GSTIN, items = itemList)
                // uploading data to Firebase Database
                database.reference.child("Bills").child(shopkeeperUid).child(key!!).setValue(bill).addOnCompleteListener {
                    if (it.isSuccessful) {
                        dialog.dismiss()

                        updateInventory(bill)

                        checkInventoryAndNotify()

                        Toast.makeText(this, "Bill Created", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ShopkeeperDashboard::class.java))
                        finish()
                    }
                    else {
                        dialog.dismiss()
                        Toast.makeText(this, "Some Error Occurred Please Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun updateInventory(bill: BillInfo) {
        val itemsBought = bill.items

        val reference = FirebaseDatabase.getInstance().reference.child("All Items").child(auth.currentUser!!.uid)

        for(item in itemsBought!!){

            var previousQuantity: Int = 0

            reference.child(item.itemID!!)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        previousQuantity = snapshot.getValue<ItemInfo>()!!.itemQuantity!!
                        Toast.makeText(this@ShopCreateBillActivity, previousQuantity.toString(), Toast.LENGTH_SHORT).show()
                        val updatedItemInfo = ItemInfo(item.itemID, item.itemName, item.itemPrice, previousQuantity - item.itemQuantity!!)

                        reference.child(item.itemID).setValue(updatedItemInfo)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

        }

    }

    private fun checkInventoryAndNotify() {

        val itemNames = ArrayList<String>()
        val itemQuantities = ArrayList<String>()

        FirebaseDatabase.getInstance().reference.child("All Items").child(auth.currentUser!!.uid)
            .addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val itemChanged =  snapshot.getValue<ItemInfo>()!!
                    if(itemChanged.itemQuantity!! <= 5){
                        itemNames.add(itemChanged.itemName!!)
                        itemQuantities.add(itemChanged.itemQuantity!!.toString())
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        Handler(Looper.getMainLooper()).postDelayed({
            if(itemNames.isNotEmpty()) {

                val title = getString(R.string.inventory_alerts)
                var message = ""

                for (i in 0 until itemNames.size) {
                    message += "${itemNames[i]} has ${itemQuantities[i]} units\n"
                }

                sendNotification(title, message)

                itemNames.clear()
                itemQuantities.clear()
            }
        }, 2000)

    }

    private fun sendNotification(title: String, message: String) {

        val intent = Intent(this, SplashScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.lightening)
            .setContentTitle(title)
            .setContentText(getString(R.string.items_are_going_out_of_stocks))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A description of Channel"

        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = desc

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}


// After pressing accept in the pending requests, add the BillInfo in this
//- ExpensesWithCategories
//    - Customer UID #1
//        - Clothing
//            - Bill Info #1 (Accepted ones only) -> Bill Info will have date updated category
//            - Bill Info #2 (Accepted ones only)
//        - Housing
//            - Bill Info #1 (Accepted ones only) -> Bill Info will have date and updated category
//            - Bill Info #2 (Accepted ones only)
//    - Customer UID #2
//
//- All Expenses
//    - Customer UID #1
//        - Bill Info #1 (Accepted ones only) -> Bill Info will have date and updated category
//        - Bill Info #2 (Accepted ones only)
