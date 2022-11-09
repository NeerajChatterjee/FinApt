package com.shrutislegion.finapt.CommonAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R

class  ViewBillItemDetailsAdapter (val options: ArrayList<ItemInfo>) : RecyclerView.Adapter<ViewBillItemDetailsAdapter.myViewHolder>(){
    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val quantity = itemView.findViewById<TextView>(R.id.quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_items_details, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder (
        holder: myViewHolder,
        position: Int
    ) {
        val model: ItemInfo = options[position]
        holder.itemName.text = model.itemName
        holder.price.text = model.itemPrice.toString()
        holder.quantity.text = model.itemQuantity.toString()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}