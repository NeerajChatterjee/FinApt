// OTP Verification Activity for both Shopkeeper and Customer for phone number validation

package com.shrutislegion.finapt

import android.animation.Animator
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Modules.LoggedInUserInfo
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.Shopkeeper.ShopkeeperDashboard
import com.shrutislegion.finapt.databinding.ActivityVerifyOtpactivityBinding
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_shopkeeper_create_profile.*
import kotlinx.android.synthetic.main.activity_verify_otpactivity.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class VerifyOTPActivity : AppCompatActivity() {

    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var dialog: ProgressDialog
    var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otpactivity)

        val binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = intent.extras!!.getString("EXTRA_PHONE")
        verificationId = intent.extras!!.getString("EXTRA_VERIFICATIONID")
        val type = intent.extras!!.getString("EXTRA_TYPE")

        var customerInfo = CustomerInfo()
        var shopkeeperInfo = ShopkeeperInfo()

        if(type.equals("Customers")){
             customerInfo = intent.getSerializableExtra("EXTRA_INFO") as CustomerInfo
        }
        if(type.equals("Shopkeepers")){
            shopkeeperInfo = intent.getSerializableExtra("EXTRA_INFO") as ShopkeeperInfo
        }

        binding.textMobile.text = "+91-$phoneNumber"
        dialog = ProgressDialog(this)

        // open the soft keyboard
//        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.toggleSoftInputFromWindow(
//            linearLayout.getApplicationWindowToken(),
//            InputMethodManager.SHOW_FORCED, 0
//        )


        // Dialog

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle(getString(R.string.verifying_otp))
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // callback
        val auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
                Toast.makeText(this@VerifyOTPActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(
                newVerificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later

                binding.verifyOtpButton.visibility = View.VISIBLE
                binding.verifyOtpProgressBar.visibility = View.GONE

                verificationId = newVerificationId
                Toast.makeText(this@VerifyOTPActivity, getString(R.string.new_otp_sent), Toast.LENGTH_SHORT).show()

            }
        }

        binding.resendOtpText.setOnClickListener {

            val phoneNum = "+91$phoneNumber"

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNum)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

        }

        binding.verifyOtpButton.setOnClickListener {

            if (binding.pinCodeView.text!!.length <= 5) {
                Toast.makeText(this, getString(R.string.please_enter_valid_code), Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()

                val codeEntered = binding.pinCodeView.text.toString()

                if (verificationId != null) {
                    binding.verifyOtpProgressBar.visibility = View.VISIBLE
                    binding.verifyOtpButton.visibility = View.INVISIBLE

                    val phoneAuthCredential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(verificationId!!, codeEntered)

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                auth.signOut()

//                                val data = Firebase.auth.currentUser!!.providerData
//
//                                for(info in data){
//                                    Toast.makeText(this, info.providerId, Toast.LENGTH_LONG).show()
//                                }

                                if(type == "Customers"){
                                    if(customerInfo.password.isEmpty()){

                                        val firebaseCredential = GoogleAuthProvider.getCredential(customerInfo.idToken, null)
                                        firebaseLogin(auth, type, customerInfo, shopkeeperInfo, binding, firebaseCredential)
                                    }
                                    else{
                                        // custom registration was made previously
                                        auth.signInWithEmailAndPassword(customerInfo.mail!!,
                                            customerInfo.password
                                        ).addOnCompleteListener {
                                            if(it.isSuccessful){
                                                saveDataAndLogin(type, customerInfo, shopkeeperInfo, binding)
                                            }
                                            else{
                                                Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
                                                auth.signOut()
                                                startActivity(Intent(this, SignInActivity::class.java))
                                                finish()
                                            }
                                        }
                                    }
                                }
                                else if(type == "Shopkeepers"){
                                    if(shopkeeperInfo.password.isEmpty()){
                                        val firebaseCredential = GoogleAuthProvider.getCredential(shopkeeperInfo.idToken, null)

                                        firebaseLogin(auth, type, customerInfo, shopkeeperInfo, binding, firebaseCredential)
                                    }
                                    else{
                                        // custom registration was made previously
                                        auth.signInWithEmailAndPassword(shopkeeperInfo.mail!!,
                                            shopkeeperInfo.password
                                        ).addOnCompleteListener {
                                            if(it.isSuccessful){
                                                saveDataAndLogin(type, customerInfo, shopkeeperInfo, binding)
                                            }
                                            else{
                                                Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
                                                auth.signOut()
                                                startActivity(Intent(this, SignInActivity::class.java))
                                                finish()
                                            }
                                        }
                                    }
                                }

                            } else {
                                dialog.dismiss()
                                binding.verifyOtpButton.visibility = View.VISIBLE
                                binding.verifyOtpProgressBar.visibility = View.GONE

                                Toast.makeText(this, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

    }

    private fun firebaseLogin(
        auth: FirebaseAuth,
        type: String,
        customerInfo: CustomerInfo,
        shopkeeperInfo: ShopkeeperInfo,
        binding: ActivityVerifyOtpactivityBinding,
        firebaseCredential: AuthCredential
    ) {
        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
            .addOnSuccessListener {
                saveDataAndLogin(type, customerInfo, shopkeeperInfo, binding)
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
                auth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
    }

    private fun saveDataAndLogin(type: String?, customerInfo: CustomerInfo, shopkeeperInfo: ShopkeeperInfo, binding: ActivityVerifyOtpactivityBinding) {
        if(type.equals("Customers")){

            customerInfo.phoneVerified = true

            FirebaseDatabase.getInstance().reference.child(type!!).child(
                customerInfo.id!!
            ).setValue(customerInfo)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.profile_created_successfully), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.profile_created_unsuccessfully), Toast.LENGTH_SHORT).show()
                }

            FirebaseDatabase.getInstance().reference.child("AllUsers").child(
                customerInfo.id!!
            ).setValue(customerInfo)

            FirebaseDatabase.getInstance().reference.child("AllPhoneNumbers").child(
                customerInfo.phone!!
            ).setValue(customerInfo.id!!)

            val loggedInUserInfo = LoggedInUserInfo(customerInfo.name, customerInfo.mail, customerInfo.id, customerInfo.profilePic, "")

            FirebaseDatabase.getInstance().reference.child("Logged In Users").child(customerInfo.id!!)
                .setValue(loggedInUserInfo)

            dialog.dismiss()

            showSuccessAnimation(type, binding)

        }
        else if(type.equals("Shopkeepers")){

             shopkeeperInfo.phoneVerified = true

            FirebaseDatabase.getInstance().reference.child(type!!).child(
                shopkeeperInfo.id!!
            ).setValue(shopkeeperInfo)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.profile_created_successfully), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.profile_created_unsuccessfully), Toast.LENGTH_SHORT).show()
                }

            FirebaseDatabase.getInstance().reference.child("AllUsers").child(
                shopkeeperInfo.id!!
            ).setValue(shopkeeperInfo)

            FirebaseDatabase.getInstance().reference.child("AllPhoneNumbers").child(
                shopkeeperInfo.phone!!
            ).setValue(shopkeeperInfo.id!!)

            val loggedInUserInfo = LoggedInUserInfo(shopkeeperInfo.name, shopkeeperInfo.mail, shopkeeperInfo.id, shopkeeperInfo.profilePic, "")

            FirebaseDatabase.getInstance().reference.child("Logged In Users").child(shopkeeperInfo.id!!)
                .setValue(loggedInUserInfo)

            dialog.dismiss()

            showSuccessAnimation(type, binding)
        }
    }

    private fun showSuccessAnimation(type: String?, binding: ActivityVerifyOtpactivityBinding) {

        binding.verifyOtpConstraintLayout.visibility = View.GONE
        binding.successAnimation.setAnimation(R.raw.success)
        binding.successAnimation.visibility = View.VISIBLE
        binding.successAnimation.playAnimation()

        successAnimation.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.successAnimation.cancelAnimation()
                binding.successAnimation.visibility = View.GONE

                if(type.equals("Shopkeepers")){
                    val intent = Intent(this@VerifyOTPActivity, ShopkeeperDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
                if(type.equals("Customers")){
                    val intent = Intent(this@VerifyOTPActivity, CustomerDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

}