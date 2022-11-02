// Adapter For Customer Pending Request Recycler View

package com.shrutislegion.finapt.Customer.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.imageview.ShapeableImageView
import com.shrutislegion.finapt.Customer.Modules.CustomerPendingRequestDetails
import com.shrutislegion.finapt.R

//class CustomerPendingRequestAdapter (options: FirebaseRecyclerOptions<CustomerPendingRequestDetails>)
//    : FirebaseRecyclerAdapter<CustomerPendingRequestDetails, CustomerPendingRequestAdapter.myViewHolder>(options)
class CustomerPendingRequestAdapter (val options: ArrayList<CustomerPendingRequestDetails>)
    : RecyclerView.Adapter<CustomerPendingRequestAdapter.myViewHolder>()   {

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val shopName = itemView.findViewById<TextView>(R.id.shopName)
        val category = itemView.findViewById<TextView>(R.id.category)
        val totalAmount = itemView.findViewById<TextView>(R.id.totalAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(com.google.firebase.database.R.layout.item_customer_pending_request, parent, false)
//        return myViewHolder(view)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_pending_request, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemmodel = options[position]
        holder.shopName.text = itemmodel.shopName
        holder.category.text = itemmodel.category
        holder.totalAmount.text = "12345"
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