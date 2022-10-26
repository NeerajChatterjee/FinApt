package com.shrutislegion.finapt.Customer

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.SignInActivity
import com.shrutislegion.finapt.databinding.ActivityCustomerSignUpBinding

@Suppress("DEPRECATION")
class CustomerSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerSignUpBinding
    // Initialize Firebase Auth
    var auth: FirebaseAuth = Firebase.auth
    var database = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_sign_up)
        binding = ActivityCustomerSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        var dialog = ProgressDialog(this)
        // Creating a dialog while the user is being added to the Firebase storage
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Creating Account")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        binding.Signup.setOnClickListener {
            dialog.show()
            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful()){
                    dialog.dismiss()
                    val id = it.result.user?.uid
                    val user = Firebase.auth.currentUser
                    val customer: CustomerInfo = CustomerInfo(binding.email.text.toString(), binding.password.text.toString(), user!!.uid)

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email sent.")
                                database.getReference().child("Customers").child(id.toString()).setValue(customer)
                                Toast.makeText(this, "Registered Successfully, Please Verify Your Account and login!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                }
                else {
                    dialog.dismiss()
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

//        if(auth.currentUser != null) {
//            val intent = Intent(this, CustomerDashboard::class.java)
//            startActivity(intent)
//            finish()
//        }

    }
}