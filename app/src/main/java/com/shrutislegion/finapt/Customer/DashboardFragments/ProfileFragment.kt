package com.shrutislegion.finapt.Customer.DashboardFragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.shrutislegion.finapt.Customer.CustomerDashboard
import com.shrutislegion.finapt.VerifyOTPActivity
import com.shrutislegion.finapt.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth: FirebaseAuth
    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        val view: View = layoutInflater.inflate(R.layout.dialog_loading, null)

        val binding:FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val currUser = FirebaseAuth.getInstance().currentUser!!.uid

        dialog = ProgressDialog(activity)

        // Dialog

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Sending OTP")
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

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
                dialog.dismiss()
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later

                dialog.dismiss()

                val intent: Intent = Intent(activity, VerifyOTPActivity::class.java)
                Toast.makeText(activity, verificationId, Toast.LENGTH_SHORT).show()
                intent.putExtra("EXTRA_ID", currUser)
                intent.putExtra("EXTRA_PHONE", cPhoneNumber.text.toString())
                intent.putExtra("EXTRA_VERIFICATIONID", verificationId)

                startActivity(intent)

            }
        }

        binding.cPhoneVerify.setOnClickListener {



            if(cPhoneVerify.text.toString() == "Verify?"){

                dialog.show()

                // Phone number verify through OTP
                val phoneNumber = cPhoneNumber.text.toString()
                val phoneNum = "+91$phoneNumber"
                Toast.makeText(activity, phoneNum, Toast.LENGTH_SHORT).show()

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNum)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this.requireActivity())                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            }

        }


        return binding.root
    }
}