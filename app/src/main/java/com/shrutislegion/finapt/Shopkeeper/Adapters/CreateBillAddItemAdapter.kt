package com.shrutislegion.finapt.Shopkeeper.Adapters

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import kotlinx.android.synthetic.main.activity_shop_create_bill.view.*

class CreateBillAddItemAdapter(val options: ArrayList<String>, private var onItemClicked: (Int, ItemInfo) -> Unit)
    : RecyclerView.Adapter<CreateBillAddItemAdapter.myViewHolder>() {
    var totalAmount: Int = 0
    var temp_qtn: Int? = null
    var pos: Int = 0
    lateinit var selectedList: ArrayList<ItemInfo>

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating viewHolder and getting all the required views by their Ids
        val itemName = itemView.findViewById<AutoCompleteTextView>(R.id.itemName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val quantity = itemView.findViewById<EditText>(R.id.quantity)
        val quantityLayout: TextInputLayout = itemView.findViewById<TextInputLayout>(R.id.quantityLayout)
        val add = itemView.findViewById<Button>(R.id.add)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_create_bill_add_item, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateBillAddItemAdapter.myViewHolder, position: Int) {
        // creating auth and database instances
        val auth = Firebase.auth
        val database = Firebase.database
        val shopkeeperUid = auth.currentUser!!.uid
        val itemList: ArrayList<ItemInfo>
        itemList = ArrayList<ItemInfo>()
        val list = ArrayList<String>()
        var ref = FirebaseDatabase.getInstance().reference
            .child("All Items").child(auth.currentUser!!.uid)
        if(ref != null) {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dss in snapshot.children) {
                        val value = (dss.getValue<ItemInfo>() as ItemInfo?)!!
                        //Toast.makeText(holder.itemName.context, value.toString(), Toast.LENGTH_LONG).show()
                        itemList.add(value)
                        list.add(value.itemName.toString())
                    }
                    //adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
        val adapter_item_list= ArrayAdapter(holder.itemName.context, R.layout.list_item, list)
        val selectedList = ArrayList<ItemInfo>()
        (holder.itemName as? AutoCompleteTextView)?.setAdapter(adapter_item_list)
        var temp: Int = 0;
        holder.itemName.setOnItemClickListener { parent, view, position, id ->

            val item: String = parent.getItemAtPosition(position).toString()
            holder.price.text = itemList[position].itemPrice.toString()
            temp = itemList[position].itemPrice!!.toInt()
            pos = position
        }
        var temp_qtn = 0
        holder.quantity.doOnTextChanged { text, start, before, count ->
            //temp_qtn = Integer.parseInt(holder.quantity.text.toString())
            if(holder.quantity.text.toString() != "") {
                temp_qtn = Integer.parseInt(holder.quantity.text.toString())
                if(temp_qtn > itemList[pos].itemQuantity!!.toInt() ) {
                    holder.quantityLayout.isErrorEnabled = true
                    holder.quantity.error = "Quantity not available"
                    Toast.makeText(holder.quantity.context, "The Selected Items is out of stock, the current quantity is " + itemList[pos].itemQuantity.toString(), Toast.LENGTH_SHORT).show()
                }
                else if(temp_qtn == 0){
                    holder.quantityLayout.isErrorEnabled = true
                    holder.quantity.error = "Quantity should be greater than 0"
                }
                else{
                    holder.quantityLayout.isErrorEnabled = false
                }
            }
        }

        holder.add.setOnClickListener {
            holder.add.setBackgroundColor(android.graphics.Color.parseColor("#661212"))
            if(holder.itemName.text == null || holder.price.text == null || holder.quantity.text == null ||  holder.quantity.text.toString() == "") {
                Toast.makeText(holder.itemName.context, "Please add all the required details first", Toast.LENGTH_SHORT).show()
            }
            else if(holder.quantityLayout.isErrorEnabled){
                Toast.makeText(holder.itemName.context, "Error Error Everywhere.. Please update quantity!", Toast.LENGTH_SHORT).show()
            }
            else {
//                if(holder.add.text == "Add") {
                    totalAmount += (temp * temp_qtn)
                    Toast.makeText(holder.itemName.context, totalAmount.toString(), Toast.LENGTH_SHORT).show()
                    holder.add.setBackgroundColor(android.graphics.Color.BLACK)
                    holder.add.text = holder.itemName.context.getString(R.string.added)
                    holder.quantity.isEnabled = false
                    var item_detail_add: ItemInfo = itemList[pos]
                    item_detail_add.itemQuantity = temp_qtn
                    selectedList.add(itemList[pos])
//                }
//                else {
//                    totalAmount -= (temp * temp_qtn)
//                    Toast.makeText(holder.itemName.context, totalAmount.toString(), Toast.LENGTH_SHORT).show()
//                    holder.add.setBackgroundColor(android.graphics.Color.parseColor("#661212"))
//                    holder.quantity.text = null
//                    holder.quantity.isEnabled = true
//                    holder.add.setText("Add")
////                    Toast.makeText(holder.itemName.context, pos.toString(), Toast.LENGTH_SHORT).show()
//                    selectedList.removeAt(position)
//                }
                //this.getList(selectedList)
                this.onItemClicked(totalAmount.toInt(), item_detail_add)
                notifyDataSetChanged()
            }

        }

    }

    override fun getItemCount(): Int {
        return options.size
    }

}