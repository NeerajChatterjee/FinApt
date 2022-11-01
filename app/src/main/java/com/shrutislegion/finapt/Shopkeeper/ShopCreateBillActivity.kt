package com.shrutislegion.finapt.Shopkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.databinding.ActivityShopCreateBillBinding

class ShopCreateBillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_create_bill)

        val binding: ActivityShopCreateBillBinding = ActivityShopCreateBillBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
//        - Bills
//            - Shopkeeper uid#1
//                    - Bill key#1
//                        - senTo: String
//                        - rest bill details
//                    - Bill key#2
//            - Shopkeeper uid#2
//                    - Bill key#1
//                        - senTo: String
//                        - rest details
