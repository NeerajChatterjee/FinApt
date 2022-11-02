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
import com.shrutislegion.finapt.RegistrationActivity
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.databinding.FragmentShopProfileBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        auth = Firebase.auth
        val database = Firebase.database
        val shopkeeperReference = database.reference.child("Shopkeepers").child(auth.currentUser!!.uid)
        // check if the user is already signed i
        shopkeeperReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!
                    binding.shopkeeperName.setText(user.name)
                    binding.shopName.setText(user.shopName)
                    binding.gstIn.setText(user.gstIn)
                    binding.mobileNumber.setText(user.phone)
                    binding.shopkeeperEmail.setText(user.mail)
                    binding.gender.setText(user.gender)
                    binding.shopkeeperAddress.setText(user.address)
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