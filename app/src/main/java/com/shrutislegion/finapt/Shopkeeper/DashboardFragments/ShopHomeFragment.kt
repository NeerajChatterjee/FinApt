package com.shrutislegion.finapt.Shopkeeper.DashboardFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Customer.Adapters.CustomerPendingRequestAdapter
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Customer.Modules.CustomerPendingRequestDetails
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.Adapters.RecentBillsAdapter
import com.shrutislegion.finapt.Shopkeeper.Modules.ShopkeeperInfo
import com.shrutislegion.finapt.Shopkeeper.ShopCreateBillActivity
import com.shrutislegion.finapt.databinding.FragmentCustomerProfileBinding
import com.shrutislegion.finapt.databinding.FragmentShopHomeBinding
import kotlinx.android.synthetic.main.fragment_shop_home.*
import kotlinx.android.synthetic.main.fragment_shop_home.view.*
import com.shrutislegion.finapt.Shopkeeper.ShopBillsHistoryActivity
import com.shrutislegion.finapt.Shopkeeper.ShopSendBillActivity
import com.shrutislegion.finapt.Shopkeeper.ShopUpdateInventoryActivity
import kotlinx.android.synthetic.main.fragment_customer_pending_req.view.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view: View = inflater.inflate(R.layout.fragment_shop_home, container, false)

        val binding: FragmentShopHomeBinding = FragmentShopHomeBinding.inflate(inflater, container, false)

        // Adding Animations
        val topAnim = AnimationUtils.loadAnimation(context, R.anim.topanim)
        val rightAnim = AnimationUtils.loadAnimation(context, R.anim.rightanim)
        val leftAnim = AnimationUtils.loadAnimation(context, R.anim.leftanim)
        val bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottomanimation)

        // Setting Animations
        binding.helloText.animation = topAnim
        binding.nameText.animation = topAnim
        binding.createBillForOwnCardView.animation = rightAnim
        binding.sendBillToCusCardView.animation = leftAnim
        binding.updateInventoryCardView.animation = rightAnim
        binding.billHistoryCardView.animation = leftAnim
        binding.recentText.animation = bottomAnim
        binding.recentBills.animation = bottomAnim
        val auth = Firebase.auth
        val ref = FirebaseDatabase.getInstance().reference.child("Shopkeepers").child(auth.currentUser!!.uid)
        // finding user in the database
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // fetching user data from the database
                    val user: ShopkeeperInfo = (snapshot.getValue<ShopkeeperInfo>() as ShopkeeperInfo?)!!
                    // setting user data to the respective views in layout
                    binding.nameText.setText(user.name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.createBillForOwnCardView.setOnClickListener {
            val intent = Intent(context, ShopCreateBillActivity::class.java)
            startActivity(intent)
        }
        binding.sendBillToCusCardView.setOnClickListener {
            startActivity(Intent(context, ShopSendBillActivity::class.java))
        }

        binding.updateInventoryCardView.setOnClickListener {
            startActivity(Intent(context, ShopUpdateInventoryActivity:: class.java))
        }

        binding.billHistoryCardView.setOnClickListener {
            startActivity(Intent(context, ShopBillsHistoryActivity::class.java))
        }

        binding.recentBills.layoutManager = LinearLayoutManager(context)
        val list = ArrayList<String>()

        val req: String = "String"

        for (i in 1..5) {
            list.add("String")
        }
        // This will pass the ArrayList to our Adapter
        val adapter = RecentBillsAdapter(list)

        // Setting the Adapter with the recyclerview
        binding.recentBills.adapter = adapter
        return binding.root
    }
}