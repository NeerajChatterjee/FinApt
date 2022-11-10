// It allows user to choose the type of registration and redirects user to the chosen Activity

package com.shrutislegion.finapt

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerSignUpActivity
import com.shrutislegion.finapt.Shopkeeper.ShopSignUpActivity
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompat() {
    var database = Firebase.database
    var registrationActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registration)
        database = FirebaseDatabase.getInstance()


        // finish this activity from User Dashboard
        finishActivityFromDashboard()

//        var myRef = database.getReference("message")
//        myRef.setValue("Value")

        // Adding Animations
        val topAnim = AnimationUtils.loadAnimation(this , R.anim.topanim)
        val rightAnim = AnimationUtils.loadAnimation(this, R.anim.rightanim)
        val leftAnim = AnimationUtils.loadAnimation(this, R.anim.leftanim)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottomanimation)

        // Setting Animations
        profileAnim.animation = topAnim
        track.animation = topAnim
        welcome.animation = topAnim
        shop.animation = rightAnim
        cust.animation = leftAnim
        signIn.animation = bottomAnim

        shop.setOnClickListener {
            val intent = Intent(this, ShopSignUpActivity::class.java)
            startActivity(intent)
        }

        cust.setOnClickListener {
            val intent = Intent(this, CustomerSignUpActivity::class.java)
            startActivity(intent)
        }

        signIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun finishActivityFromDashboard() {
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, intent: Intent) {
                val action = intent.action
                if (action == "finish_activity") {
                    finish()
                    // DO WHATEVER YOU WANT.
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("finish_activity"))
    }
}