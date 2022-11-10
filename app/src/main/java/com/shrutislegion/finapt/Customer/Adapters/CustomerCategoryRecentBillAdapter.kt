package com.shrutislegion.finapt.Customer.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class CustomerCategoryRecentBillAdapter {

}
//class CustomerCategoryRecentBillAdapter (val options: ArrayList<BillInfo>) : RecyclerView.Adapter<CustomerCategoryRecentBillAdapter.myViewHolder>() {
//
//    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        // creating viewHolder and getting all the required views by their Ids
//        var custName: TextView = itemView.findViewById(R.id.custName)
//        var number: TextView = itemView.findViewById(R.id.phoneNumber)
//        var category: TextView = itemView.findViewById(R.id.category)
//        var sentTime: TextView = itemView.findViewById(R.id.shopRecentSendTimeText)
//        var status: TextView = itemView.findViewById(R.id.status)
//        var totalAmount: TextView = itemView.findViewById(R.id.totalAmount)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
//
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_bills, parent, false)
//        return myViewHolder(view)
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
//        val itemModel = options[position]
//
//        val auth = Firebase.auth
//        var customerUid = ""
//        FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(itemModel.shopkeeperUid).addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//        holder.number.text = itemModel.sentTo
//            val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
//            val formatted = formatter.format(Date(itemModel.date.toLong()))
//            holder.sentTime.text = formatted
//
//            val ref = FirebaseDatabase.getInstance().reference.child("AllPhoneNumbers")
//                .child(itemModel.sentTo)
//            ref.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        customerUid = snapshot.getValue<String>() as String
//
//                        val newRef = FirebaseDatabase.getInstance().reference.child("Customers")
//                            .child(customerUid)
//                        newRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                if (snapshot.exists()) {
//                                    val value = snapshot.getValue<CustomerInfo>()!!
//                                    holder.custName.text = value.name
//                                }
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                                Log.e("tag", error.message)
//                            }
////
//                        })
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("tag", error.message)
//                }
//
//            })
//
//        if (itemModel.pending == true) {
//            holder.status.text = holder.status.context.getString(R.string.pending)
//            holder.status.setTextColor(Color.RED)
//        } else {
//            //Toast.makeText(holder.status.context, itemModel.accepted.toString(), Toast.LENGTH_SHORT).show()
//            if (itemModel.accepted == true) {
//                holder.status.text = holder.status.context.getString(R.string.accepted)
//                holder.status.setTextColor(Color.GREEN)
//            } else {
//                holder.status.text = holder.status.context.getString(R.string.rejected)
//                holder.status.setTextColor(Color.RED)
//
//            }
//        }
//        holder.category.text = itemModel.category
//        holder.totalAmount.text = itemModel.totalAmount
//    }
//
//    // return the number of the items in the list
//    override fun getItemCount(): Int {
//        return options.size
//    }
//}