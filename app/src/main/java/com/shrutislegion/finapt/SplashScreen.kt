// Launcher Activity that displays for 2.5 seconds while we check the current user.
package com.shrutislegion.finapt

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Customer.CustomerCreateProfileActivity
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard
import kotlinx.android.synthetic.main.activity_splash_screen.*

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

        // hinding Action Bar
        supportActionBar!!.hide()

        // Adding Animations
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.topanim)
        val rightAnim = AnimationUtils.loadAnimation(this, R.anim.rightanim)
        val leftAnim = AnimationUtils.loadAnimation(this, R.anim.leftanim)
        val image = findViewById<ShapeableImageView>(R.id.lightening)
        image.setAnimation(topAnim)
        finText.visibility = View.GONE
        aptText.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            finText.visibility = View.VISIBLE
            finText.setAnimation(rightAnim)
            aptText.visibility = View.VISIBLE
            aptText.setAnimation(leftAnim)
        },500)


        // Check if the user is null and direct it to Registration Activity
        if(user == null){
            Toast.makeText(
                this@SplashScreen,
                "null",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            Toast.makeText(
                this@SplashScreen,
                auth.currentUser!!.uid,
                Toast.LENGTH_SHORT
            ).show()
        }

        // If the user is not null, and if user is email verified then direct it to the respective DashBoards.
        if (user != null && user.isEmailVerified) {

            val id = auth.currentUser!!.uid
            val custReference = database.reference.child("Customers")
            Toast.makeText(this@SplashScreen, id, Toast.LENGTH_SHORT).show()
            // Read from the database
            custReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(id).exists()) {
                        isCust = true
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
                        isShop = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }

        Handler(Looper.getMainLooper()).postDelayed({

            // pass the value player to the next activity and then open the Home activity according to the boolean value
            if (isCust) {

                FirebaseDatabase.getInstance().reference.child("Customers").child(Firebase.auth.currentUser!!.uid).child("phoneVerified")
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                if(snapshot.value == true){
                                    Toast.makeText(
                                        this@SplashScreen,
                                        "Signed In as Customers",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@SplashScreen, CustomerDashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Firebase.auth.signOut()
                                    val intent = Intent(this@SplashScreen, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            else{
                                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            val intent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    })

            }
            else if (isShop) {
                FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(Firebase.auth.currentUser!!.uid).child("phoneVerified")
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                if(snapshot.value == true){
                                    Toast.makeText(
                                        this@SplashScreen,
                                        "Signed In as Shopkeepers",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@SplashScreen, ShopkeeperDashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Firebase.auth.signOut()
                                    val intent = Intent(this@SplashScreen, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            else{
                                Firebase.auth.signOut()
                                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Firebase.auth.signOut()
                            val intent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    })

            }
            else {
                Firebase.auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },2500)
    }


    // Add on function
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