// Adapter For Customer Pending Request Recycler View

package com.shrutislegion.finapt.Customer.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo

//class CustomerPendingRequestAdapter (options: FirebaseRecyclerOptions<CustomerPendingRequestDetails>)
//    : FirebaseRecyclerAdapter<CustomerPendingRequestDetails, CustomerPendingRequestAdapter.myViewHolder>(options)
class CustomerPendingRequestAdapter(val options: ArrayList<BillInfo>)
    : RecyclerView.Adapter<CustomerPendingRequestAdapter.myViewHolder>()   {

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val shopName = itemView.findViewById<TextView>(R.id.shopName)
        val category = itemView.findViewById<TextView>(R.id.category)
        val totalAmount = itemView.findViewById<TextView>(R.id.totalAmount)
        val accept = itemView.findViewById<Button>(R.id.acceptReq)
        val reject = itemView.findViewById<Button>(R.id.rejectReq)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_pending_request, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemmodel = options[position]
        val ref = FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(itemmodel.shopkeeperUid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    holder.shopName.text = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!.shopName
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        holder.category.text = itemmodel.category
        holder.totalAmount.text = itemmodel.totalAmount

        holder.accept.setOnClickListener {
            itemmodel.accepted = true
            itemmodel.pending = false
            FirebaseDatabase.getInstance().reference.child("Bills").child(itemmodel.shopkeeperUid.toString()).child(itemmodel.billID.toString()).setValue(itemmodel).addOnSuccessListener {
                Toast.makeText(holder.accept.context, "Accepted", Toast.LENGTH_SHORT).show()
            }
        }
        holder.reject.setOnClickListener {
            itemmodel.accepted = false
            itemmodel.pending = false
            FirebaseDatabase.getInstance().reference.child("Bills").child(itemmodel.shopkeeperUid.toString()).child(itemmodel.billID.toString()).setValue(itemmodel).addOnSuccessListener {
                Toast.makeText(holder.accept.context, "Rejected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}