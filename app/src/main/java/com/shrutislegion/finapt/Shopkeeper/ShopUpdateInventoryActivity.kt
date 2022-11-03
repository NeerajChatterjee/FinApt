package com.shrutislegion.finapt.Shopkeeper

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.InventoryActivities.*
import com.shrutislegion.finapt.databinding.ActivityShopUpdateInventoryBinding


class ShopUpdateInventoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_update_inventory)

        val binding: ActivityShopUpdateInventoryBinding = ActivityShopUpdateInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager.beginTransaction()

        val fragmentLayout = FrameLayout(this)
        // set the layout params to fill the activity
        fragmentLayout.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )


        binding.checkItemsCardView.setOnClickListener {
            val intent = Intent(this, ShopCheckItemsActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addItemsCardView.setOnClickListener {
            val intent = Intent(this, ShopAddItemsActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.editItemsCardView.setOnClickListener {
            val intent = Intent(this, ShopEditItemsActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.removeItemsCardView.setOnClickListener {
            val intent = Intent(this, ShopRemoveItemsActivity::class.java)
            startActivity(intent)
            finish()
//            // set an id to the layout
//            fragmentLayout.id = R.id.shop_updateInventory_fragment_container // some positive integer
//
//            // set the layout as Activity content
//            setContentView(fragmentLayout)
//            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopRemoveItemsFragment()).commit()
//            supportActionBar!!.title = "Remove Items"
        }

    }
}