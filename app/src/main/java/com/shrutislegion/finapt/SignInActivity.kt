package com.shrutislegion.finapt

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Customer.CustomerCreateProfileActivity
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperCreateProfileActivity
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

        val dialog = ProgressDialog(this)
        // Creating a dialog while the user is being Signed In
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle(getString(R.string.signing_in))
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        binding.SignIn.setOnClickListener {

            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            dialog.show()
            auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                dialog.dismiss()
                if(it.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        val id = auth.currentUser!!.uid
                        Toast.makeText(this@SignInActivity, id, Toast.LENGTH_SHORT).show()

                        // Read from the database
                        binding.signInConstraintLayout.visibility = View.GONE
                        binding.loadingAnimation.setAnimation(R.raw.loading)
                        binding.loadingAnimation.visibility = View.VISIBLE
                        binding.loadingAnimation.playAnimation()
                        checkLoginType(id, binding)

                    } else {
                        auth.signOut()
                        Toast.makeText(this, getString(R.string.please_verify_your_email), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkLoginType(id: String, binding: ActivitySignInBinding){

        val database = Firebase.database
        val custReference = database.reference.child("Customers")
        custReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {

                    custReference.child(Firebase.auth.currentUser!!.uid).child("emailVerified").setValue(true)

                    custReference.child(Firebase.auth.currentUser!!.uid).child("phoneVerified")
                        .addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){

                                    // if phoneVerified is true then show the CustomerDashboard
                                    if(snapshot.value == true){
                                        Toast.makeText(
                                            this@SignInActivity,
                                            getString(R.string.signed_in_as_customer),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.loadingAnimation.cancelAnimation()
                                        binding.loadingAnimation.visibility = View.GONE
                                        val intent =
                                            Intent(this@SignInActivity, CustomerDashboard::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    // if phoneVerified is false then show CreateProfile Activity
                                    else{
                                        Toast.makeText(
                                            this@SignInActivity,
                                            getString(R.string.create_your_profile),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.loadingAnimation.cancelAnimation()
                                        binding.loadingAnimation.visibility = View.GONE
                                        val intent =
                                            Intent(this@SignInActivity, CustomerCreateProfileActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.signInConstraintLayout.visibility = View.VISIBLE
                                Log.e("tag", error.message)
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                binding.loadingAnimation.cancelAnimation()
                binding.loadingAnimation.visibility = View.GONE
                binding.signInConstraintLayout.visibility = View.VISIBLE
            }

        })

        val shopReference = database.reference.child("Shopkeepers")
        shopReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()) {

                    shopReference.child(Firebase.auth.currentUser!!.uid).child("emailVerified").setValue(true)

                    shopReference.child(Firebase.auth.currentUser!!.uid).child("phoneVerified")
                        .addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){

                                    // if phoneVerified is true then show the ShopkeeperDashboard
                                    if(snapshot.value == true){
                                        Toast.makeText(
                                            this@SignInActivity,
                                            getString(R.string.signed_in_as_shopkeeper),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.loadingAnimation.cancelAnimation()
                                        binding.loadingAnimation.visibility = View.GONE
                                        val intent =
                                            Intent(this@SignInActivity, ShopkeeperDashboard::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    // if phoneVerified is false then show CreateProfile Activity
                                    else{
                                        Toast.makeText(
                                            this@SignInActivity,
                                            getString(R.string.create_your_profile),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.loadingAnimation.cancelAnimation()
                                        binding.loadingAnimation.visibility = View.GONE
                                        val intent =
                                            Intent(this@SignInActivity, ShopkeeperCreateProfileActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("tag", error.message)
                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.signInConstraintLayout.visibility = View.VISIBLE
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                binding.loadingAnimation.cancelAnimation()
                binding.loadingAnimation.visibility = View.GONE
                binding.signInConstraintLayout.visibility = View.VISIBLE
            }
        })
    }
}