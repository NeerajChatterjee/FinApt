package com.shrutislegion.finapt.Shopkeeper

import android.animation.Animator
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.VerifyOTPActivity
import com.shrutislegion.finapt.databinding.ActivityShopkeeperCreateProfileBinding
import kotlinx.android.synthetic.main.activity_shopkeeper_create_profile.*
import org.json.JSONException
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class ShopkeeperCreateProfileActivity : AppCompatActivity() {

    lateinit var mRequestQueue: RequestQueue
    lateinit var dialog: ProgressDialog
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val id: String = ""
    var name: String = ""
    var gender: String = ""
    var shopName:String = ""
    var state: String = ""
    var city: String = ""
    var pincode: String = ""
    var address: String = ""
    var phoneNumber: String = ""
    var mail: String? = null
    var password: String = ""
    var profilePic: String = ""
    var gstin: String = ""
    var phoneVerified: Boolean = false
    var emailVerified: Boolean = false
    var idToken: String = ""
    lateinit var currUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_create_profile)

        val binding: ActivityShopkeeperCreateProfileBinding = ActivityShopkeeperCreateProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mRequestQueue = Volley.newRequestQueue(this)
        dialog = ProgressDialog(this)

        // Dialog
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle(getString(R.string.verifying_pincode))
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        currUser = FirebaseAuth.getInstance().currentUser!!.uid

        val items_gender = listOf("Male", "Female", "Others")
        val items_state = listOf("Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal")

        val adapter_gender = ArrayAdapter(this, R.layout.list_item, items_gender)
        (binding.autoCompleteTxtGender as? AutoCompleteTextView)?.setAdapter(adapter_gender)

        binding.autoCompleteTxtGender.setOnItemClickListener { parent, view, position, id ->

            val item:String = parent.getItemAtPosition(position).toString()
            gender = item

        }

        val adapter_state = ArrayAdapter(this, R.layout.list_item, items_state)
        (binding.autoCompleteTxtState as? AutoCompleteTextView)?.setAdapter(adapter_state)

        binding.autoCompleteTxtState.setOnItemClickListener { parent, view, position, id ->

            val item:String = parent.getItemAtPosition(position).toString()
            state = item

        }

        binding.shopkeeperPinCode.doOnTextChanged { text, start, before, count ->

            if(text!!.length == 6){

                dialog.show()

                val pinCode = binding.shopkeeperPinCode.text
                mRequestQueue.cache.clear()

                val url = "https://api.postalpincode.in/pincode/$pinCode"
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show()

                val queue = Volley.newRequestQueue(this)

                val jsonArrayRequest = JsonArrayRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        try {
                            // we are getting data of post office in the form of JSON file.
                            if (response.getJSONObject(0).getString("Status").equals("Error")) {
                                // validating if the response status is success or failure.
                                binding.shopkeeperPinCodeLayout.isErrorEnabled = true
                                binding.shopkeeperPinCodeLayout.error = "Incorrect Pincode"
                                dialog.dismiss()
                            }
                            else {
                                // if the status is success we are calling this method
                                // in which we are getting data from post office array
                                val obj = response.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0)

                                // inside our json array we are getting district name,
                                // state and country from our data.
                                val stateFromApi = obj.getString("State")

                                if(stateFromApi.equals(state)){
                                    // Toast.makeText(this, "Success: $city", Toast.LENGTH_SHORT).show()
                                    city = obj.getString("District")

                                    // after getting all data we are updating the End Icon
                                    updateEndIcon(binding)
                                    dialog.dismiss()
                                }
                                else{

                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle(getString(R.string.error))
                                    builder.setMessage(getString(R.string.state_not_matched))
                                    builder.setIcon(R.drawable.ic_baseline_error_24)

                                    builder.setPositiveButton("OK"){dialogInterface, which ->

                                    }

                                    // Create the AlertDialog
                                    val alertDialog: AlertDialog = builder.create()
                                    // Set other dialog properties
                                    alertDialog.setCancelable(false)
                                    dialog.dismiss()
                                    alertDialog.show()

                                }

                            }
                        } catch (e: JSONException) {
                            // if we gets any error then it
                            // will be printed in log cat.
                            e.printStackTrace()
                            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                            binding.shopkeeperPinCodeLayout.isErrorEnabled = true
                            binding.shopkeeperPinCodeLayout.error = getString(R.string.incorrect_pincode)
                            dialog.dismiss()
                        }
                    },
                    { e ->
                        e.printStackTrace()
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        binding.shopkeeperAddress.setText("$e")
                        binding.shopkeeperPinCodeLayout.isErrorEnabled = true
                        binding.shopkeeperPinCodeLayout.error = getString(R.string.incorrect_pincode)
                        dialog.dismiss()
                    }
                )

                queue.add(jsonArrayRequest)

            }
            else{
                binding.shopkeeperPinCodeLayout.setEndIconDrawable(R.drawable.ic_baseline_cancel_24)
                binding.shopkeeperPinCodeLayout.endIconContentDescription = getString(R.string.wrong_pincode)

                val states = arrayOf(
                    intArrayOf(android.R.attr.state_enabled), // enabled
                )

                val colors = intArrayOf(
                    Color.RED
                )

                val myList = ColorStateList(states, colors)

                binding.shopkeeperPinCodeLayout.setEndIconTintList(myList)
            }
        }

        binding.shopkeeperPhoneNumber.doOnTextChanged{ text, start, before, count ->

            if(text!!.length == 10){

                // check if the number is unique
                val dialogPhone = ProgressDialog(this)

                // Dialog
                dialogPhone.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                dialogPhone.setTitle(getString(R.string.validating_number))
                dialogPhone.setMessage(getString(R.string.please_wait))
                dialogPhone.setCancelable(false)
                dialogPhone.setCanceledOnTouchOutside(false)

                dialogPhone.show()

                FirebaseDatabase.getInstance().reference.child("AllPhoneNumbers")
                    .addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                if(snapshot.child("$text").exists()){
                                    // alert box show

                                    binding.shopkeeperPhoneNumberLayout.endIconContentDescription = "Already Exists"

                                    val builder = AlertDialog.Builder(this@ShopkeeperCreateProfileActivity)
                                    builder.setTitle(getString(R.string.error))
                                    builder.setMessage(getString(R.string.try_again_with_another_number))
                                    builder.setIcon(R.drawable.ic_baseline_error_24)

                                    builder.setPositiveButton("OK"){dialogInterface, which ->

                                    }

                                    // Create the AlertDialog
                                    val alertDialog: AlertDialog = builder.create()
                                    // Set other dialog properties
                                    alertDialog.setCancelable(false)
                                    dialogPhone.dismiss()
                                    if(!this@ShopkeeperCreateProfileActivity.isFinishing){
                                        alertDialog.show()
                                    }
                                }
                                else{
                                    updatePhoneUI(binding)
                                    dialogPhone.dismiss()
                                }
                            }
                            else{
                                updatePhoneUI(binding)
                                dialogPhone.dismiss()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

            }
            else{

                binding.shopkeeperPhoneNumberLayout.endIconContentDescription = "Incorrect Number"
                binding.shopkeeperPhoneNumberLayout.setEndIconDrawable(R.drawable.ic_baseline_cancel_24)

                val states = arrayOf(
                    intArrayOf(android.R.attr.state_enabled), // enabled
                )

                val colors = intArrayOf(
                    Color.RED
                )

                val myList = ColorStateList(states, colors)

                binding.shopkeeperPhoneNumberLayout.setEndIconTintList(myList)
            }

        }

        val shopkeeperUserReference = FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(currUser)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    mail = dataSnapshot.child("mail").value.toString()
                    password = dataSnapshot.child("password").value.toString()
                    emailVerified = dataSnapshot.child("emailVerified").value as Boolean
                    phoneVerified = dataSnapshot.child("phoneVerified").value as Boolean
                    profilePic = dataSnapshot.child("profilePic").value.toString()
                    idToken = dataSnapshot.child("idToken").value.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        shopkeeperUserReference.addValueEventListener(postListener)

        binding.createProfileSubmit.setOnClickListener {

            if(binding.shopkeeperName.text.toString().trim().isEmpty()
                || gender.trim().isEmpty()
                || state.trim().isEmpty()
                || pincode.trim().isEmpty()
                || binding.shopkeeperName.text.toString().trim().isEmpty()
                || binding.shopkeeperAddress.text.toString().trim().isEmpty()
                || (binding.shopkeeperPhoneNumber.text.toString().trim().length <= 9)
                || (binding.shopkeeperPhoneNumber.text.toString().trim().length >= 11)
                || (binding.shopkeeperPhoneNumberLayout.endIconContentDescription != "Unique Number")
            ){
                if(binding.shopkeeperName.text.toString().trim().isEmpty()){
                    binding.shopkeeperNameLayout.requestFocus()
                }
                else if(gender.trim().isEmpty()){
                    binding.shopkeeperGenderLayout.requestFocus()
                }
                else if(state.trim().isEmpty()){
                    binding.shopkeeperStateLayout.requestFocus()
                }
                else if(binding.shopkeeperName.text.toString().trim().isEmpty()){
                    binding.shopkeeperName.requestFocus()
                }
                else if(binding.shopkeeperPinCodeLayout.endIconContentDescription != "Correct Pincode"){
                    binding.shopkeeperPinCodeLayout.requestFocus()
                }
                else if(binding.shopkeeperAddress.text.toString().trim().isEmpty()){
                    binding.shopkeeperAddress.requestFocus()
                }
                else if((binding.shopkeeperPhoneNumber.text.toString().trim().length <= 9)
                    || (binding.shopkeeperPhoneNumber.text.toString().trim().length >= 11)
                    || (binding.shopkeeperPhoneNumberLayout.endIconContentDescription != "Unique Number")){
                    binding.shopkeeperPhoneNumberLayout.requestFocus()
                }
                Toast.makeText(this, getString(R.string.please_fill_all_details), Toast.LENGTH_SHORT).show()
            }
            else{
                name = binding.shopkeeperName.text.toString().trim()
                address = binding.shopkeeperAddress.text.toString().trim()
                phoneNumber = binding.shopkeeperPhoneNumber.text.toString().trim()
                shopName = binding.shopkeeperName.text.toString().trim()
                gstin = binding.shopkeeperGSTIn.text.toString().trim()

                val info: ShopkeeperInfo = ShopkeeperInfo(gender = gender, shopName = shopName, gstIn = gstin ,state = state, city = city, address = address, phone = phoneNumber, name = name, pincode = pincode, id = currUser, mail = mail, password = password, profilePic = profilePic, emailVerified = emailVerified, phoneVerified = phoneVerified, idToken = idToken)


                // Open Verify OTP Activity and use shared resources
                sendOTP(binding, info)

            }

        }

    }

    private fun sendOTP(binding: ActivityShopkeeperCreateProfileBinding, info: ShopkeeperInfo) {
        val auth = FirebaseAuth.getInstance()

        val dialogSubmit = ProgressDialog(this)

        // Dialog
        dialogSubmit.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialogSubmit.setTitle(getString(R.string.sending_otp))
        dialogSubmit.setMessage(getString(R.string.please_wait))
        dialogSubmit.setCancelable(false)
        dialogSubmit.setCanceledOnTouchOutside(false)

        dialogSubmit.show()

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
                    Log.e("tag", e.message!!)
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("tag", e.message!!)
                }

                // Show a message and update the UI
                dialogSubmit.dismiss()
                Toast.makeText(this@ShopkeeperCreateProfileActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later

                dialogSubmit.dismiss()

                val intent = Intent(this@ShopkeeperCreateProfileActivity, VerifyOTPActivity::class.java)
                intent.putExtra("EXTRA_INFO", info as java.io.Serializable)
                intent.putExtra("EXTRA_VERIFICATIONID", verificationId)
                intent.putExtra("EXTRA_TYPE", "Shopkeepers")
                intent.putExtra("EXTRA_PHONE", info.phone)
                startActivity(intent)
                finish()

            }
        }

        val phoneNum = "+91" + binding.shopkeeperPhoneNumber.text!!.trim().toString()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNum)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun updatePhoneUI(binding: ActivityShopkeeperCreateProfileBinding) {
        binding.shopkeeperPhoneNumberLayout.endIconContentDescription = "Unique Number"
        binding.shopkeeperPhoneNumberLayout.setEndIconDrawable(R.drawable.ic_baseline_check_circle_24)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
        )

        val colors = intArrayOf(
            Color.GREEN
        )

        val myList = ColorStateList(states, colors)

        binding.shopkeeperPhoneNumberLayout.setEndIconTintList(myList)
    }

    private fun updateEndIcon(binding: ActivityShopkeeperCreateProfileBinding) {
        binding.shopkeeperPinCodeLayout.setEndIconDrawable(R.drawable.ic_baseline_check_circle_24)
        binding.shopkeeperPinCodeLayout.endIconContentDescription = "Correct Pincode"
        binding.shopkeeperPinCodeLayout.isErrorEnabled = false
        binding.shopkeeperPinCodeLayout.error = ""

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
        )

        val colors = intArrayOf(
            Color.GREEN
        )

        val myList = ColorStateList(states, colors)

        binding.shopkeeperPinCodeLayout.setEndIconTintList(myList)
        pincode = binding.shopkeeperPinCode.text.toString()
    }
}