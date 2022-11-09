// Adapter For Customer Pending Request Recycler View

package com.shrutislegion.finapt.Customer.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import kotlinx.android.synthetic.main.item_customer_select_category.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
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
        val sentTime: TextView = itemView.findViewById<TextView>(R.id.customerPRSendTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_pending_request, parent, false)
        return myViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val itemModel = options[position]
        val ref = FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(itemModel.shopkeeperUid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    holder.shopName.text = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!.shopName
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })
        holder.category.text = itemModel.category
        holder.totalAmount.text = itemModel.totalAmount
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
        val formatted = formatter.format(Date(itemModel.date.toLong()))
        holder.sentTime.text = formatted

        holder.accept.setOnClickListener {

            val dialogPlus = DialogPlus.newDialog(holder.accept.context)
                .setContentHolder(ViewHolder(R.layout.item_customer_select_category))
                .setExpanded(true, 1500)
                .setCancelable(true)
                .create()

            val newView = dialogPlus.holderView

            newView.selectCategoryRadioGroup.clearCheck()
            dialogPlus.show()

            newView.customerCategoryAddToExpensesButton.setOnClickListener {

                val selectedId = newView.selectCategoryRadioGroup.checkedRadioButtonId

                if(selectedId == -1){
                    Toast.makeText(holder.accept.context, holder.accept.context.getString(R.string.please_select_category_to_add), Toast.LENGTH_SHORT).show()
                }
                else{
                    val selectedRadioButton: RadioButton = newView.selectCategoryRadioGroup.findViewById(selectedId)

                    itemModel.accepted = true
                    itemModel.pending = false

                    FirebaseDatabase.getInstance().reference.child("Bills").child(itemModel.shopkeeperUid.toString()).child(itemModel.billID.toString()).setValue(itemModel).addOnSuccessListener {
                        Toast.makeText(holder.accept.context, "Accepted and added in expenses", Toast.LENGTH_SHORT).show()
                    }

                    itemModel.category = selectedRadioButton.text.toString()

                    FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child(itemModel.category)
                        .child(itemModel.billID.toString())
                        .setValue(itemModel).addOnSuccessListener {
                            Log.e("tag", "Successfully added in expenses with categories")
                        }

                    FirebaseDatabase.getInstance().reference.child("All Expenses")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child(itemModel.billID.toString())
                        .setValue(itemModel).addOnSuccessListener {
                            Log.e("tag", "Successfully added in all expenses")
                        }

                    FirebaseDatabase.getInstance().reference.child("Customer Pending Requests").child(Firebase.auth.currentUser!!.uid)
                        .child(itemModel.billID.toString()).removeValue().addOnSuccessListener {
                            Log.e("tag", "Successfully removed from pending requests")
                        }

                    dialogPlus.dismiss()

                }

            }
        }

        holder.reject.setOnClickListener {

            itemModel.accepted = false
            itemModel.pending = false
            FirebaseDatabase.getInstance().reference.child("Bills").child(itemModel.shopkeeperUid.toString()).child(itemModel.billID.toString()).setValue(itemModel).addOnSuccessListener {
                Toast.makeText(holder.accept.context, "Rejected", Toast.LENGTH_SHORT).show()
            }

            FirebaseDatabase.getInstance().reference.child("Customer Pending Requests").child(Firebase.auth.currentUser!!.uid)
                .child(itemModel.billID.toString()).removeValue().addOnSuccessListener {
                    Log.e("tag", "Successfully removed from pending requests")
                }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return options.size
    }
}