package com.shrutislegion.finapt

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard

class SplashScreen : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var isCust: Boolean = false
        var isShop: Boolean = false
        val user = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        if (user != null) {
            if (auth.currentUser!!.isEmailVerified) {
                val id = auth.currentUser!!.uid
                val custReference = database.reference.child("Customers")
                Toast.makeText(this@SplashScreen, id, Toast.LENGTH_SHORT).show()
                // Read from the database
                custReference.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(id).exists()) {
                            Toast.makeText(
                                this@SplashScreen,
                                "Signed In as Customers",
                                Toast.LENGTH_SHORT
                            ).show()
                            isCust = true
//                            val intent =
//                                Intent(this@SplashScreen, CustomerDashboard::class.java)
//                            startActivity(intent)
//                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    }

                })
                val shopReference = database.reference.child("Shopkeepers")
                shopReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(id).exists()) {
                            Toast.makeText(
                                this@SplashScreen,
                                "Signed In as Shopkeepers",
                                Toast.LENGTH_SHORT
                            ).show()
                            isShop = true
//                            val intent =
//                                Intent(this@SplashScreen, ShopkeeperDashboard::class.java)
//                            startActivity(intent)
//                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    }
                })
            }
        else {
                Toast.makeText(this, "Please Verify Your Email Address", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({

            // pass the value player to the next activity and then open the Home activity according to the boolean value
            if (isCust) {
                val intent = Intent(this@SplashScreen, CustomerDashboard::class.java)
                startActivity(intent)
                finish()
            }
            else if (isShop) {
                val intent = Intent(this@SplashScreen, ShopkeeperDashboard::class.java)
                startActivity(intent)
                finish()

            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                //intent.putExtra(MainActivity.EXTRA_LOGINTYPE, "$player")
                startActivity(intent)
                finish()
            }

        },2500)
    }

    fun checkLoginType(id: String){

        val database = Firebase.database
        val custReference = database.reference.child("Customers")
        custReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {
                    Toast.makeText(
                        this@SplashScreen,
                        "Signed In as Customers",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@SplashScreen, CustomerDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

        val shopReference = database.reference.child("Shopkeepers")
        shopReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {
                    Toast.makeText(
                        this@SplashScreen,
                        "Signed In as Shopkeepers",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@SplashScreen, ShopkeeperDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }
}