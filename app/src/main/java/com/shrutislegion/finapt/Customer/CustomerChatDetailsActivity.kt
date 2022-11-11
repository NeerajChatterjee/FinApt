package com.shrutislegion.finapt.Customer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.shrutislegion.finapt.Adapters.ChatDetailsAdapter
import com.shrutislegion.finapt.AppCompat
import com.shrutislegion.finapt.Modules.ChatMessageInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.databinding.ActivityCustomerChatDetailsBinding
import java.util.*
import kotlin.collections.ArrayList

class CustomerChatDetailsActivity : AppCompat() {

    var receiverId: String? = null
    var storeMessage: ArrayList<ChatMessageInfo> = ArrayList<ChatMessageInfo>()
    lateinit var adapter: ChatDetailsAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.hide()

        setContentView(R.layout.activity_customer_chat_details)

        val binding: ActivityCustomerChatDetailsBinding = ActivityCustomerChatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To get the shared intent data
        var userName = intent.getStringExtra("EXTRA_USERNAME")
        var userEmail = intent.getStringExtra("EXTRA_USEREMAIL")
        var userLastMsg = intent.getStringExtra("EXTRA_USERLASTMSG")
        var userImgUrl = intent.getStringExtra("EXTRA_USERIMGURL")
        receiverId = intent.getStringExtra("EXTRA_RECEIVERID")

        Glide.with(this)
            .load(userImgUrl)
            .override(600, 400)
            .placeholder(R.drawable.ic_account)
            .into(binding.customerChatUserProfileImage)

        binding.customerChatUserName.text = userName.toString()

        adapter = ChatDetailsAdapter(storeMessage, this)
        binding.customerChatDetailsRV.adapter = adapter

        binding.customerChatDetailsRV.scrollToPosition(storeMessage.size - 1)

        val linearLayoutManager = LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false)
        binding.customerChatDetailsRV.layoutManager = linearLayoutManager

        val senderId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("Chats")
            .child("$senderId,$receiverId")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        storeMessage.clear()

                        for(dss in snapshot.children){
                            val messageModel: ChatMessageInfo = dss.getValue<ChatMessageInfo>()!!
                            storeMessage.add(messageModel)
                        }
                        binding.customerChatDetailsRV.scrollToPosition(storeMessage.size - 1)
                        adapter.notifyDataSetChanged()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CustomerChatDetailsActivity, "Unable to load chats", Toast.LENGTH_SHORT).show()
                }

            })

        binding.customerChatDetailsSendButton.setOnClickListener {

            val msg = binding.customerChatDetailsMessage.text.toString()

            if(msg.trim().isNotEmpty()){
                val msgData = ChatMessageInfo(senderId+"", receiverId+"", msg+"", Date().time)

                binding.customerChatDetailsMessage.setText("")

                FirebaseDatabase.getInstance().reference
                    .child("Chats")
                    .child("$senderId,$receiverId")
                    .push()
                    .setValue(msgData).addOnSuccessListener {

                        FirebaseDatabase.getInstance().reference
                            .child("Chats")
                            .child("$receiverId,$senderId")
                            .push()
                            .setValue(msgData).addOnSuccessListener {
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@CustomerChatDetailsActivity, "Message not sent", Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        }

        binding.customerChatUserPhoneCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            var contactNumber = ""

            FirebaseDatabase.getInstance().reference.child("AllUsers").child(receiverId!!).child("phone")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            contactNumber = snapshot.value.toString()
                            callIntent.data = Uri.parse("tel:${contactNumber}")
                            startActivity(callIntent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@CustomerChatDetailsActivity, "Unable to place call", Toast.LENGTH_SHORT).show()
                    }

                })

        }

        binding.customerBackArrowButton.setOnClickListener {
            finish()
        }

    }
}