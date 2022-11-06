package com.shrutislegion.finapt.Shopkeeper.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shrutislegion.finapt.Customer.Adapters.CustomerPendingRequestAdapter
import com.shrutislegion.finapt.Customer.Modules.CustomerPendingRequestDetails
import com.shrutislegion.finapt.R

class RecentBillsAdapter (val options: ArrayList<String>) : RecyclerView.Adapter<RecentBillsAdapter.myViewHolder>() {

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val name = itemView.findViewById<TextView>(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(com.google.firebase.database.R.layout.item_customer_pending_request, parent, false)
//        return myViewHolder(view)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_bills, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemmodel = options[position]
        holder.name.text = "name"
    }
//    (
//        holder: myViewHolder,
//        position: Int,
//        model: CustomerPendingRequestDetails
//    ) {
//        holder.shopName.setText(model.shopName)
//        holder.category.setText(model.category)
//        holder.totalAmount.setText(model.totalAmount)
//    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}