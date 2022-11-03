package com.shrutislegion.finapt.Shopkeeper.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R

class InventoryRemoveAdapter (val options: ArrayList<ItemInfo>) : RecyclerView.Adapter<InventoryRemoveAdapter.myViewHolder>(){

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating viewHolder and getting all the required views by their Ids
        // shopName, category, totalAmount, isAccepted, phone, billID, shopkeeperUID, timeStampBillSend
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val remove = itemView.findViewById<ShapeableImageView>(R.id.remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_remove_items, parent, false)
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
        holder.remove.setOnClickListener {

            FirebaseDatabase.getInstance().reference
                .child("All Items")
                .child(auth.currentUser!!.uid)
                .child(options[position].itemID.toString()).removeValue()
                .addOnCompleteListener {
                    Toast.makeText(
                        holder.itemName.context,
                        "Item removed!",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyItemRemoved(position)
                }



//            var key = model.itemID
//            var ref = FirebaseDatabase.getInstance().reference
//                .child("All Items")
//                .child(auth.currentUser!!.uid)
//            if(ref != null) {
//                ref.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//
//                        for (dss in snapshot.children) {
//                            val value = (dss.getValue<ItemInfo>() as ItemInfo?)!!
//                            //Toast.makeText(context, "value " + item.toString(), Toast.LENGTH_LONG).show()
//                            if (value.itemID == key) {
//                                ref.child(key!!).removeValue().addOnCompleteListener {
//                                    if (it.isSuccessful) {
//                                        Toast.makeText(
//                                            holder.itemName.context,
//                                            "Item Removed Successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show();
//                                        notifyDataSetChanged()
//                                       // notifyItemRemoved(position)
//                                       // notifyItemRangeChanged(position, 1);
//
//                                        //notifyDataSetChanged()
////                                        val intent = Intent(holder.itemName.context, ShopRemoveItemsFragment::class.java)
////                                        // Start the Shared activity with the transition
////                                        holder.itemName.context.startActivity(intent)
//                                    } else {
//                                        Toast.makeText(
//                                            holder.itemName.context,
//                                            "Try Again",
//                                            Toast.LENGTH_SHORT
//                                        ).show();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                })
//            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}