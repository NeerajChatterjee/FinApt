package com.shrutislegion.finapt.Shopkeeper

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.SignInActivity
import com.shrutislegion.finapt.databinding.ActivityShopSignUpBinding
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_shop_sign_up.*
import kotlinx.android.synthetic.main.activity_shop_sign_up.view.*

@Suppress("DEPRECATION")
class ShopSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopSignUpBinding
    // Initialize Firebase Auth
    var auth: FirebaseAuth = Firebase.auth
    var database = Firebase.database
    // For Google SignIn
    val RC_SIGN_IN = 60
    private val TAG = "ShopSignUpActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_sign_up)

        binding = ActivityShopSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        var dialog = ProgressDialog(this)
        // Creating a dialog while the user is being added to the Firebase storage
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Creating Account")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        binding.signUpButton.setOnClickListener {
            dialog.show()
            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    dialog.dismiss()
                    val id = it.result.user?.uid
                    val user = Firebase.auth.currentUser
                    val shopkeeper: ShopkeeperInfo = ShopkeeperInfo(binding.email.text.toString(), binding.password.text.toString(), user!!.uid)

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(ContentValues.TAG, "Email sent.")
                                database.getReference().child("Shopkeepers").child(id.toString()).setValue(shopkeeper)
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
        //google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.googleSignUp.setOnClickListener {
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
        val googleCredential = GoogleAuthProvider.getCredential(idToken, null)
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            val shopkeeper: ShopkeeperInfo = ShopkeeperInfo()
                            if (user != null) {
                                shopkeeper.id = user.uid
                                shopkeeper.name = user.displayName.toString()
                                shopkeeper.profilePic = user.photoUrl.toString()
                                database.getReference().child("Shopkeepers").child(user.uid)
                                    .setValue(shopkeeper)
                            }
                            val intent = Intent(this, ShopkeeperDashboard::class.java)
                            startActivity(intent)
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            //updateUI(null)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }
}