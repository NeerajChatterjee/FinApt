package com.shrutislegion.finapt.Customer.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R

class CustomerHomeExpenseCategoryAdapter (val options: HashMap<String, Int>)
    : RecyclerView.Adapter<CustomerHomeExpenseCategoryAdapter.myViewHolder>(){


    // To override LinearLayoutManager by Wrapper, as it crashes the application sometimes
    inner class LinearLayoutManagerWrapper : LinearLayoutManager {
        constructor(context: Context?) : super(context) {}
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val expenseCategory: TextView = itemView.findViewById<TextView>(R.id.category)!!
        val totalExpense:TextView = itemView.findViewById<TextView>(R.id.totalExpense)!!
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val recentBillView = itemView.findViewById<RecyclerView>(R.id.recentBills)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_category, parent, false)
        return myViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemModel = options
        val keys = ArrayList<String>(options.keys)
        holder.expenseCategory.text = keys[position]
        holder.totalExpense.text = "₹${itemModel[keys[position]]}"
        val color = position + 1
//        holder.layoutPosition
        if (color%4 == 1) {
            holder.cardView.setCardBackgroundColor(holder.cardView.resources.getColor(R.color.purple_200 , null))
        }
        else if (color%4 == 2) {
            holder.cardView.setCardBackgroundColor(holder.cardView.resources.getColor(R.color.teal_200, null))
        }
        else if(color%4 == 3) {
            holder.cardView.setCardBackgroundColor(holder.cardView.resources.getColor(R.color.light_pink, null))
        }
        else {
            holder.cardView.setCardBackgroundColor(holder.cardView.resources.getColor(R.color.light_yellow, null))
        }
        lateinit var adapter: CustomerCategoryRecentBillAdapter
        holder.recentBillView.layoutManager = LinearLayoutManagerWrapper(holder.recentBillView.context, LinearLayoutManager.HORIZONTAL, false)
        val bills: ArrayList<BillInfo> = ArrayList<BillInfo>()
        adapter = CustomerCategoryRecentBillAdapter(bills)
        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(Firebase.auth.currentUser!!.uid).child(keys[position]).orderByChild("Date").limitToLast(5).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dss in snapshot.children){
                        val bill = dss.getValue<BillInfo>() as BillInfo
                        bills.add(bill)
                    }
                    bills.reverse()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        holder.recentBillView.adapter = adapter


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }

}