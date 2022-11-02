// This Fragment Displays the Pending Request sent by the Shopkeeper to the logged in customer.

package com.shrutislegion.finapt.Customer.DashboardFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.shrutislegion.finapt.Customer.Adapters.CustomerPendingRequestAdapter
import com.shrutislegion.finapt.Customer.Modules.CustomerInfo
import com.shrutislegion.finapt.Customer.Modules.CustomerPendingRequestDetails
import com.shrutislegion.finapt.R
import kotlinx.android.synthetic.main.fragment_customer_pending_req.*
import kotlinx.android.synthetic.main.fragment_customer_pending_req.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerPendingReqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerPendingReqFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customer_pending_req, container, false)
        var customerPendingRequestView = view.findViewById<RecyclerView>(R.id.customerPendingRequestView)

        //var linearLayoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, true)
        view.customerPendingRequestView.layoutManager = LinearLayoutManager(view.context)

        val list = ArrayList<CustomerPendingRequestDetails>()

        val req: CustomerPendingRequestDetails = CustomerPendingRequestDetails(
        "ShopName",
        "category",
        12
        )
        for (i in 1..20) {
            list.add(CustomerPendingRequestDetails("Shopname", "category",12))
        }
        // This will pass the ArrayList to our Adapter
        val adapter = CustomerPendingRequestAdapter(list)

        // Setting the Adapter with the recyclerview
        customerPendingRequestView.adapter = adapter
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerPendingReqFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerPendingReqFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}