package com.shrutislegion.finapt

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard
import com.shrutislegion.finapt.databinding.ActivitySignInBinding

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth
        val database = Firebase.database

        val dialog = ProgressDialog(this)
        // Creating a dialog while the user is being Signed In
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Signing In")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        binding.SignIn.setOnClickListener {
            dialog.show()
            auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                dialog.dismiss()
                if(it.isSuccessful) {
                    var found: Boolean = false
                    if (auth.currentUser!!.isEmailVerified) {
                        val id = auth.currentUser!!.uid
                        val custReference = database.reference.child("Customers")
                        Toast.makeText(this@SignInActivity, id, Toast.LENGTH_SHORT).show()

                        custReference.child(auth.currentUser!!.uid).child("emailVerified").setValue(true)

                        // Read from the database
                        checkLoginType(id)

                    } else {
                        Toast.makeText(this, "Please Verify Your Email Address", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkLoginType(id: String){

        val database = Firebase.database
        val custReference = database.reference.child("Customers")
        custReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Signed In as Customers",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@SignInActivity, CustomerDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

        val shopReference = database.reference.child("Shopkeepers")
        shopReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Signed In as Shopkeepers",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@SignInActivity, ShopkeeperDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}