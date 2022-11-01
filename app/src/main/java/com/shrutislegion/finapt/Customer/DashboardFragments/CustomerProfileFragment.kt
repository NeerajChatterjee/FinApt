package com.shrutislegion.finapt.Customer.DashboardFragments

import android.icu.text.MessageFormat.format
import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.shrutislegion.finapt.databinding.FragmentCustomerProfileBinding
import okhttp3.internal.Util.format
import okhttp3.internal.http.HttpDate.format
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        val auth = Firebase.auth
        val database = Firebase.database
        val custReference = database.reference.child("Customers").child(auth.currentUser!!.uid)
        // check if the user is already signed i
        custReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = (snapshot.getValue<CustomerInfo>() as CustomerInfo?)!!
                    binding.customerName.setText(user.name)
                    binding.mobileNumber.setText(user.phone)
                    binding.customerEmail.setText(user.mail)
                    binding.gender.setText(user.gender)
                    val dob: String = SimpleDateFormat("dd/MM/yyyy").format(Date(user.dob.toLong()))
                    binding.dob.setText(dob)
                    binding.customerAddress.setText(user.address)
                    binding.state.setText(user.state)
                    binding.pinCode.setText(user.pincode)
                    if(user.profilePic != ""){
                        context?.let { Glide.with(it).load(user.profilePic).into(binding.profilePic) }
                        //Toast.makeText(context, user.profilePic, Toast.LENGTH_SHORT)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        return binding.root
    }
}