package com.shrutislegion.finapt.Shopkeeper.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import kotlinx.android.synthetic.main.item_edit_dialogue.view.*


class InventoryEditAdapter (val options: ArrayList<ItemInfo>) : RecyclerView.Adapter<InventoryEditAdapter.myViewHolder>(){

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val edit = itemView.findViewById<ShapeableImageView>(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_inventory, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder
                (
        holder: myViewHolder,
        position: Int
    ) {
        val model: ItemInfo = options[position]
        holder.itemName.text = model.itemName
        holder.price.text = model.itemPrice.toString()
        val auth = Firebase.auth
        holder.edit.setOnClickListener {
            //Toast.makeText(holder.itemName.context, "Clicked", Toast.LENGTH_SHORT).show()
            val dialogPlus = DialogPlus.newDialog ( holder.edit.context!! )
                .setContentHolder(ViewHolder(com.shrutislegion.finapt.R.layout.item_edit_dialogue))
                .setExpanded(true, 1100)
                .create()
            val newView = dialogPlus!!.holderView!!
            val iname = newView.findViewById<EditText>(R.id.iname)
            val iprice = newView.findViewById<EditText>(R.id.iprice)
            val iquantity = newView.findViewById<EditText>(R.id.iquantity)
            iname.setText(model.itemName)
            iprice.setText(model.itemPrice.toString())
            iquantity.setText(model.itemQuantity.toString())

            dialogPlus.show()

            newView.update.setOnClickListener {
                val value: ItemInfo = ItemInfo(model.itemID, iname.text.toString(), Integer.parseInt(iprice.text.toString()), Integer.parseInt(iquantity.text.toString()))
                FirebaseDatabase.getInstance().reference
                    .child("All Items").child(auth.currentUser!!.uid)
                    .child(options[position].itemID.toString())
                    .setValue(value).addOnSuccessListener {
                        dialogPlus.dismiss()
                        Toast.makeText(holder.itemName.context, "Item Details Updated.", Toast.LENGTH_SHORT).show()
                        options[position] = value
                        notifyItemChanged(position)
                    }
            }

        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}