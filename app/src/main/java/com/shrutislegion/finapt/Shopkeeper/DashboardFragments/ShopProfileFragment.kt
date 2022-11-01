package com.shrutislegion.finapt.Shopkeeper.DashboardFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.RegistrationActivity
import com.shrutislegion.finapt.databinding.FragmentShopProfileBinding

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