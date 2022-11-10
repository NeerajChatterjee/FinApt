package com.shrutislegion.finapt.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.Modules.ItemInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard
import com.shrutislegion.finapt.databinding.ActivityCustomerAddExpenseBinding
import java.util.*
import kotlin.collections.ArrayList

class CustomerAddExpenseActivity : AppCompatActivity() {

    // store the bill in the "All Expenses" node
    lateinit var auth: FirebaseAuth
    val billID: String = ""
    val pending: Boolean = false
    var accepted: Boolean = true
    var sentTo: String = ""
    var date: String = ""
    var totalAmount: String = ""
    var shopkeeperUid: String = ""
    var category: String = ""
    var invoice: String = ""
    var GSTIN: String = ""
    var items = ArrayList<ItemInfo>()
    var expense = BillInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_add_expense)

        val binding: ActivityCustomerAddExpenseBinding = ActivityCustomerAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val itemsCategory = listOf(
            getString(R.string.clothing),
            getString(R.string.food_beverages),
            getString(R.string.medical_healthcare),
            getString(R.string.education),
            getString(R.string.housing),
            getString(R.string.groceries),
            getString(R.string.personal),
            getString(R.string.entertainment),
            getString(R.string.transportation),
            getString(R.string.others)
        )

        val adapterCategory = ArrayAdapter(this, R.layout.list_item, itemsCategory)

        binding.addExpenseTextCategory.setAdapter(adapterCategory)

        binding.addExpenseTextCategory.setOnItemClickListener { parent, view, position, id ->

            val item:String = parent.getItemAtPosition(position).toString()
            category = item

        }

        binding.addExpenseSubmitButton.setOnClickListener {

            if(binding.addExpenseInvoiceNo.text.toString().trim().isEmpty()
                || category.trim().isEmpty()
                || binding.addExpenseTotalAmount.text.toString().trim().isEmpty()){
                Toast.makeText(this, getString(R.string.please_fill_all_details), Toast.LENGTH_SHORT).show()
            }
            else if(binding.addExpenseTotalAmount.text.toString().toInt() <= 0){
                Toast.makeText(this@CustomerAddExpenseActivity, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
            else{
                invoice = binding.addExpenseInvoiceNo.text.toString().trim()
                totalAmount = binding.addExpenseTotalAmount.text.toString().trim()

                FirebaseDatabase.getInstance().reference.child("Customers")
                    .child(auth.currentUser!!.uid)
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                val customerInfoModel = snapshot.getValue<CustomerInfo>()!!

                                sentTo = customerInfoModel.phone
                                date = Calendar.getInstance().timeInMillis.toString()
                                shopkeeperUid = auth.currentUser!!.uid
                                GSTIN = binding.addExpenseRemarks.text.toString() // treat it as remarks here

                                val key = FirebaseDatabase.getInstance().reference.child("All Expenses")
                                    .child(auth.currentUser!!.uid).push().key

                                expense = BillInfo(key.toString(), pending, accepted, sentTo, date, totalAmount, shopkeeperUid, category, invoice, GSTIN, items)

                                uploadExpense(expense)

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }

        }

    }

    private fun uploadExpense(expense: BillInfo) {
        FirebaseDatabase.getInstance().reference.child("All Expenses")
            .child(auth.currentUser!!.uid)
            .child(expense.billID.toString())
            .setValue(expense).addOnSuccessListener {
                Toast.makeText(this, getString(R.string.expense_added_successfully), Toast.LENGTH_SHORT).show()
            }

        FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories")
            .child(auth.currentUser!!.uid)
            .child(expense.category)
            .child(expense.billID.toString())
            .setValue(expense)

        startActivity(Intent(this, CustomerDashboard::class.java))
        finish()
    }
}