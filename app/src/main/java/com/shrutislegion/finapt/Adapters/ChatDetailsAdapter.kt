package com.shrutislegion.finapt.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.shrutislegion.finapt.Modules.ChatMessageInfo
import com.shrutislegion.finapt.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailsAdapter(var storeMessage: ArrayList<ChatMessageInfo>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // These are used to identify the type of the user, i.e., Sender/Receiver and then change the layout accordingly
    var SENDER_VIEW_TYPE: Int = 1
    var RECEIVER_VIEW_TYPE:Int = 2

    // Sender ViewHolder used to bind the sender messages
    class SenderViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var senderMsgDetailText = itemView.findViewById<TextView>(R.id.senderMsgDetailText)
        var senderTimetext = itemView.findViewById<TextView>(R.id.senderTimeText)

    }

    // Receiver ViewHolder used to bind the receiver messages
    class ReceiverViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var receiverMsgDetailText = itemView.findViewById<TextView>(R.id.receiverMsgDetailText)
        var receiverTimeText = itemView.findViewById<TextView>(R.id.receiverTimeText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == SENDER_VIEW_TYPE){

            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_sender_msg, parent, false)
            SenderViewHolder(view)

        } else{

            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_receiver_msg, parent, false)
            ReceiverViewHolder(view)

        }
    }

    // Get the Item ViewType and return whether the current user is a sender/receiver
    override fun getItemViewType(position: Int): Int {

        if(storeMessage[position].senderId.equals(FirebaseAuth.getInstance().currentUser!!.uid)){

            return SENDER_VIEW_TYPE

        }
        else{

            return  RECEIVER_VIEW_TYPE

        }

    }

    // Set the fields with the suitable values and set the time by using SimpleDateFormat
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageInfo:ChatMessageInfo = storeMessage[position]

        if(holder.javaClass == SenderViewHolder::class.java){

            (holder as SenderViewHolder).senderMsgDetailText.text =  messageInfo.message
            val dateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a")
            (holder as SenderViewHolder).senderTimetext.text = dateFormat.format(Date(messageInfo.messageTime))

        }
        else{

            (holder as ReceiverViewHolder).receiverMsgDetailText.text = messageInfo.message
            val dateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a")
            (holder as ReceiverViewHolder).receiverTimeText.text = dateFormat.format(Date(messageInfo.messageTime))

        }
    }

    override fun getItemCount(): Int {
        return storeMessage.size
    }
}