// This Fragment Displays the Profile Details of Shopkeeper fetch from the Firebase Database.

package com.shrutislegion.finapt.Customer.DashboardFragments

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.MessageFormat.format
import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.LanguageManager
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.RegistrationActivity
import com.shrutislegion.finapt.databinding.FragmentCustomerProfileBinding
import kotlinx.android.synthetic.main.activity_customer_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.Util.format
import okhttp3.internal.http.HttpDate.format
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class CustomerProfileFragment : Fragment() {
    lateinit var user: CustomerInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentCustomerProfileBinding = FragmentCustomerProfileBinding.inflate(inflater, container, false)

        // getting auth and database instances
        val auth = Firebase.auth
        val database = Firebase.database
        val custReference = database.reference.child("Customers").child(auth.currentUser!!.uid)
        // finding user in database
        custReference.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // fetching user data from the database
                    user = (snapshot.getValue<CustomerInfo>() as CustomerInfo?)!!
                    // setting user data to the respective views in layout
                    binding.customerName.setText(user.name)
                    binding.mobileNumber.setText(user.phone)
                    binding.customerEmail.setText(user.mail)
                    binding.gender.setText(user.gender)
                    val dob: String = SimpleDateFormat("dd/MM/yyyy").format(Date(user.dob.toLong()))
                    binding.dob.setText(dob)
                    binding.customerAddress.setText(user.address)
                    binding.state.setText(user.state)
                    binding.pinCode.setText(user.pincode)
                    if(user.profilePic == ""){
                        binding.profilePic.visibility = View.GONE
                    }
                    else{
                        // Setting profile pic using Glide Library
                        context?.let { Glide.with(it).load(user.profilePic).into(binding.profilePic) }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val lang = LanguageManager(requireContext())
        binding.hindi.setOnClickListener {
            lang.updateResources("hi")
            hindi.setTextColor(requireActivity().resources.getColor(R.color.color_primary))
            requireActivity().recreate()
        }

        binding.english.setOnClickListener {
            lang.updateResources("en")
            english.setTextColor(requireActivity().resources.getColor(R.color.color_primary))
            requireActivity().recreate()
        }

        // implementing logout button for the user
        binding.signOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, RegistrationActivity::class.java)
            startActivity(intent)
            requireActivity().finish();
        }


        return binding.root
    }
}