package com.shrutislegion.finapt

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.databinding.ActivityCustomerSignUpBinding
import com.shrutislegion.finapt.databinding.ActivitySignInBinding

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

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
                    if(auth.currentUser!!.isEmailVerified) {
                        Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CustomerDashboard::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Please Verify Your Email Address", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        if(auth.currentUser != null) {
            if(auth.currentUser!!.isEmailVerified) {
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CustomerDashboard::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Please Verify Your Email Address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}