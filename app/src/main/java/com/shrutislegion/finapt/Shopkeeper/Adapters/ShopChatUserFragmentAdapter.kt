package com.shrutislegion.finapt.Shopkeeper.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.LoggedInUserInfo
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.ShopChatDetailsActivity


class ShopChatUserFragmentAdapter(var storeUsers: ArrayList<LoggedInUserInfo>, val context: Context): RecyclerView.Adapter<ShopChatUserFragmentAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        var chatUserProfileImage: ImageView = itemView.findViewById<ImageView>(R.id.shopChatUserProfileImage)
        var chatUserName: TextView = itemView.findViewById<TextView>(R.id.shopChatUserName)
        var chatUserLastMessage: TextView = itemView.findViewById<TextView>(R.id.shopChatUserLastMessage)
        var pChatCardView: CardView = itemView.findViewById<CardView>(R.id.shopChatCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_chatdetails_shop, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model =storeUsers[position]

        Glide.with(holder.chatUserLastMessage.context)
            .load(model.photoUrl)
            .override(600, 400)
            .placeholder(R.drawable.ic_account)
            .into(holder.chatUserProfileImage)

        holder.chatUserName.text = model.name.toString()

        FirebaseDatabase.getInstance().reference
            .child("Chats")
            .child(FirebaseAuth.getInstance().currentUser!!.uid + "," + model.id)
            .orderByChild("messageTime")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        for(messageInfo in snapshot.children){
                            holder.chatUserLastMessage.text = messageInfo.child("message").value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        holder.pChatCardView.setOnClickListener {

            val intent = Intent(holder.pChatCardView.context, ShopChatDetailsActivity::class.java)

            intent.putExtra("EXTRA_USERNAME", model.name.toString())
            intent.putExtra("EXTRA_USEREMAIL", model.mail.toString())
            intent.putExtra("EXTRA_USERLASTMSG", model.lastMessage.toString())
            intent.putExtra("EXTRA_USERIMGURL", model.photoUrl.toString())
            intent.putExtra("EXTRA_RECEIVERID", model.id.toString())


            holder.pChatCardView.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return storeUsers.size
    }
}