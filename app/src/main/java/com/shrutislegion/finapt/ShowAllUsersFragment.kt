package com.shrutislegion.finapt

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.LoggedInUserInfo
import com.shrutislegion.finapt.Shopkeeper.Adapters.ShopChatUserFragmentAdapter
import com.shrutislegion.finapt.databinding.FragmentShowAllUsersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowAllUsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class ShowAllUsersFragment : Fragment() {

    var storeUsers: ArrayList<LoggedInUserInfo> = ArrayList<LoggedInUserInfo>()
    lateinit var adapter: ShopChatUserFragmentAdapter
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // To override LinearLayoutManager by Wrapper, as it crashes the application sometimes
    inner class LinearLayoutManagerWrapper : LinearLayoutManager {
        constructor(context: Context?) : super(context) {}
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentShowAllUsersBinding = FragmentShowAllUsersBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        val linearLayoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)

        binding.chatAllUsersFragmentRV.layoutManager = linearLayoutManager
        binding.chatAllUsersFragmentRV.isNestedScrollingEnabled = false

        FirebaseDatabase.getInstance().reference.child("Logged In Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(i in snapshot.children){
                            val user:LoggedInUserInfo = i.getValue<LoggedInUserInfo>()!!
                            if(!user.id.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                                storeUsers.add(user)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, getString(R.string.unable_to_fetch), Toast.LENGTH_SHORT).show()
                }

            })

        Handler(Looper.getMainLooper()).postDelayed({

            adapter = ShopChatUserFragmentAdapter(storeUsers, container!!.context)
            binding.chatAllUsersFragmentRV.adapter = adapter

            adapter.notifyDataSetChanged()

            binding.progressBarChatAllUsers.visibility = View.GONE
            binding.chatAllUsersFragmentRV.visibility = View.VISIBLE

        }, 2000)

        return binding.root
    }
}