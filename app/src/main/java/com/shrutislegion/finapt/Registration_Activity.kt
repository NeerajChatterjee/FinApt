package com.shrutislegion.finapt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerSignUpActivity
import com.shrutislegion.finapt.Shopkeeper.ShopSignUpActivity
import kotlinx.android.synthetic.main.activity_registration.*

class Registration_Activity : AppCompatActivity() {
    var database = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        database = FirebaseDatabase.getInstance()
//        var myRef = database.getReference("message")
//        myRef.setValue("Value")
        shop.setOnClickListener {
            val intent = Intent(this, ShopSignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        cust.setOnClickListener {
            val intent = Intent(this, CustomerSignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}