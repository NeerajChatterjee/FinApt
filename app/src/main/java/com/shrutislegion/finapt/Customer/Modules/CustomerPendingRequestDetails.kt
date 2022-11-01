package com.shrutislegion.finapt.Customer.Modules

data class CustomerPendingRequestDetails(
    var shopName: String? = null,
    var category: String? = null,
    var totalAmount: Int = 0,
    var isAccepted: Boolean? = false,
    var phone: String?=null,
    var billID: String?=null,
    var description: String?=null,
    var shopkeeperUID: String?=null,
    var timeStampBillSend: String?=null,
)