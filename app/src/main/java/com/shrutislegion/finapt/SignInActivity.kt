// For Customer and Shopkeeper Sign in - 1) via email and password 2) via Google Sign in

package com.shrutislegion.finapt

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Customer.CustomerCreateProfileActivity
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperCreateProfileActivity
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard
import com.shrutislegion.finapt.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.activity_sign_in.view.*

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {

    val RC_SIGN_IN = 1
    private val TAG = "SIGN_IN_ACTIVITY"
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth

        val dialog = ProgressDialog(this)
        // Creating a dialog while the user is being Signed In
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle(getString(R.string.signing_in))
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        binding.signInButton.setOnClickListener {

            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            if(binding.email.text == null || binding.email.text.toString().trim().isEmpty() || binding.email.text.toString().trim().length <= 10){
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(binding.password.text == null || binding.password.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.show()
                auth.signInWithEmailAndPassword(
                    binding.email.text.toString().trim(),
                    binding.password.text.toString().trim()
                ).addOnCompleteListener {
                    dialog.dismiss()
                    if (it.isSuccessful) {
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
                            Toast.makeText(
                                this,
                                getString(R.string.please_verify_your_email),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignIn.setOnClickListener {
            Firebase.auth.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.

                binding.signInConstraintLayout.visibility = View.GONE
                binding.loadingAnimation.setAnimation(R.raw.loading)
                binding.loadingAnimation.visibility = View.VISIBLE
                binding.loadingAnimation.playAnimation()

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                Firebase.auth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener { task ->

                        if(task.additionalUserInfo!!.isNewUser){
                            val user = Firebase.auth.currentUser!!
                            user.delete()
                                .addOnSuccessListener {

                                    Firebase.auth.signOut()
                                    googleSignInClient.signOut()
                                        .addOnCompleteListener {
                                            Toast.makeText(this, "Please register your account and then Login!", Toast.LENGTH_SHORT).show()

                                            binding.loadingAnimation.visibility = View.GONE
                                            binding.loadingAnimation.cancelAnimation()

                                            startActivity(Intent(this, RegistrationActivity::class.java))
                                            finish()
                                        }
                                        .addOnFailureListener {
                                            binding.signInConstraintLayout.visibility = View.VISIBLE
                                            binding.loadingAnimation.visibility = View.GONE
                                            binding.loadingAnimation.cancelAnimation()
                                        }
                                }
                        }
                        else{

                            val currentUser = Firebase.auth.currentUser!!

                            FirebaseDatabase.getInstance().reference.child("GoogleUsers")
                                .addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.child(currentUser.uid).exists()){
                                            checkLoginType(currentUser.uid, binding)
                                        }
                                        else{
                                            userSignOut()

                                            binding.signInConstraintLayout.visibility = View.VISIBLE
                                            binding.loadingAnimation.visibility = View.GONE
                                            binding.loadingAnimation.cancelAnimation()

                                            Toast.makeText(this@SignInActivity, "Please Login with mail and password!", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        userSignOut()

                                        binding.signInConstraintLayout.visibility = View.VISIBLE
                                        binding.loadingAnimation.visibility = View.GONE
                                        binding.loadingAnimation.cancelAnimation()

                                        Toast.makeText(this@SignInActivity, "Try Again", Toast.LENGTH_SHORT).show()
                                    }

                                })

                        }
                    }
                    .addOnFailureListener {
                        Log.e("tag", "${it.message}")

                        binding.signInConstraintLayout.visibility = View.VISIBLE
                        binding.loadingAnimation.visibility = View.GONE
                        binding.loadingAnimation.cancelAnimation()

                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
                binding.signInConstraintLayout.visibility = View.VISIBLE
                binding.loadingAnimation.visibility = View.GONE
                binding.loadingAnimation.cancelAnimation()
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun userSignOut() {
        Firebase.auth.signOut()
        googleSignInClient.signOut()
            .addOnCompleteListener {
                Log.e("SIGN_IN_ACTIVITY", "googleSignInClient logged out successfully..")
            }
    }

    private fun checkLoginType(id: String, binding: ActivitySignInBinding){

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

                                Firebase.auth.signOut()
                                googleSignInClient.signOut()

                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.signInConstraintLayout.visibility = View.VISIBLE
                                Log.e("tag", error.message)
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {

                Firebase.auth.signOut()
                googleSignInClient.signOut()

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

                                Firebase.auth.signOut()
                                googleSignInClient.signOut()

                                Log.e("tag", error.message)
                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.signInConstraintLayout.visibility = View.VISIBLE
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {

                Firebase.auth.signOut()
                googleSignInClient.signOut()

                Log.w(TAG, "Failed to read value.", error.toException())
                binding.loadingAnimation.cancelAnimation()
                binding.loadingAnimation.visibility = View.GONE
                binding.signInConstraintLayout.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        googleSignInClient.signOut()
    }
}