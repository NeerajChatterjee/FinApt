// Data Class for Storing Bill Information

package com.shrutislegion.finapt.Modules

data class BillInfo (
    var billID: String? = null,
    var pending: Boolean? = true,
    var sentTo: String = "",
    var date: String = "",
    var totalAmount: String = "",
    var shopkeeperUid: String = "",
    var category: String = "",
    var invoice: String = "",
    var GSTIN: String? = null,
    var items: ArrayList<String>
)