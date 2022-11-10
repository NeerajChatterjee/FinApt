package com.shrutislegion.finapt.Customer.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R

class CustomerHomeExpenseCategoryAdapter (val options: HashMap<String, Int>)
    : RecyclerView.Adapter<CustomerHomeExpenseCategoryAdapter.myViewHolder>(){


    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val expenseCategory: TextView = itemView.findViewById<TextView>(R.id.category)!!
        val totalExpense:TextView = itemView.findViewById<TextView>(R.id.totalExpense)!!
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_category, parent, false)
        return myViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemModel = options
        val keys = ArrayList<String>(options.keys)
        holder.expenseCategory.text = keys[position]
        holder.totalExpense.text = itemModel[keys[position]].toString()
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


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }

}