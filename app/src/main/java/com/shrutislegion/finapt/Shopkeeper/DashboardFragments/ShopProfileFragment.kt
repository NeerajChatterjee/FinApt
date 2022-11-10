// This Fragment Displays the Profile Details of Shopkeeper fetch from the Firebase Database.

package com.shrutislegion.finapt.Shopkeeper.DashboardFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.databinding.FragmentShopProfileBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class ShopProfileFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var user: ShopkeeperInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentShopProfileBinding =
            FragmentShopProfileBinding.inflate(inflater, container, false)

        // getting auth and database instances
        auth = Firebase.auth
        val database = Firebase.database
        val shopkeeperReference = database.reference.child("Shopkeepers").child(auth.currentUser!!.uid)
        // Check the data of the current user in database
        shopkeeperReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // fetching the user data from database
                    user = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!
                    // setting the data to the respective views in the layout
                    binding.shopkeeperName.text = user.name
                    binding.shopName.setText(user.shopName)
                    binding.gstIn.setText(user.gstIn)
                    binding.mobileNumber.setText(user.phone)
                    binding.shopkeeperEmail.setText(user.mail)
                    binding.gender.setText(user.gender)
                    binding.shopkeeperAddress.setText(user.address)
                    binding.state.setText(user.state)
                    binding.pinCode.setText(user.pincode)
                    if(user.profilePic != ""){
                        // setting Profile Pic using Glide Library
                        context?.let { Glide.with(it).load(user.profilePic).into(binding.profilePic) }
                        //Toast.makeText(context, user.profilePic, Toast.LENGTH_SHORT)
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
            requireActivity().finish()
        }

        return binding.root
    }
}

//            val transaction = requireFragmentManager().beginTransaction()
//
//            transaction.addToBackStack(null)
//            transaction.replace(R.id.fragment_shopProfile, ShopChatFragment()).commit()