package com.shrutislegion.finapt.Shopkeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Registration_Activity
import kotlinx.android.synthetic.main.activity_customer_dashboard.*

class ShopkeeperDashboard : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_dashboard)
        auth = Firebase.auth
        signOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@ShopkeeperDashboard, Registration_Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}