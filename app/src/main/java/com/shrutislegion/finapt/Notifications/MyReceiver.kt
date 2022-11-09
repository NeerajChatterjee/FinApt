package com.shrutislegion.finapt.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard

class MyReceiver: BroadcastReceiver() {

    var itemNames = ArrayList<String>()
    var itemQuantities = ArrayList<String>()
    lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        auth = Firebase.auth
        createNotificationChannel(context)

        if(auth.currentUser == null){
            return
        }

        getCurrentItemsInfo()

        Handler(Looper.getMainLooper()).postDelayed({
            if(itemNames.isNotEmpty()) {

                val title = context.getString(R.string.inventory_alerts)
                var message = "\n"

                for (i in itemNames.indices) {
                    message += "${itemNames[i]} has ${itemQuantities[i]} units\n"
                }

                sendNotification(context, title, message)
            }
        }, 2000)

    }

    private fun getCurrentItemsInfo() {
        FirebaseDatabase.getInstance().reference.child("All Items").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(item in snapshot.children){
                        val iteratedItemInfo = item.getValue<ItemInfo>()!!
                        if(iteratedItemInfo.itemQuantity!! <= 5){
                            itemNames.add(iteratedItemInfo.itemName!!)
                            itemQuantities.add(iteratedItemInfo.itemQuantity!!.toString())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })
    }

    private fun sendNotification(context: Context, title: String, message: String) {

        // Toast.makeText(context, "Sending Notification", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, ShopkeeperDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.lightening)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.items_are_going_out_of_stocks))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)){
            notify(notificationId, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val name = "Notification Channel"
        val desc = "A description of Channel"

        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = desc

        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}