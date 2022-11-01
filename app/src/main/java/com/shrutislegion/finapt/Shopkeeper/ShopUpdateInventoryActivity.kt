package com.shrutislegion.finapt.Shopkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shrutislegion.finapt.R
import com.shrutislegion.finapt.Shopkeeper.InventoryFragments.ShopAddItemsFragment
import com.shrutislegion.finapt.Shopkeeper.InventoryFragments.ShopCheckItemsFragment
import com.shrutislegion.finapt.Shopkeeper.InventoryFragments.ShopEditItemsFragment
import com.shrutislegion.finapt.Shopkeeper.InventoryFragments.ShopRemoveItemsFragment
import com.shrutislegion.finapt.databinding.ActivityShopUpdateInventoryBinding

class ShopUpdateInventoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_update_inventory)

        val binding: ActivityShopUpdateInventoryBinding = ActivityShopUpdateInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager.beginTransaction()

        binding.checkItemsCardView.setOnClickListener {
            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopCheckItemsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Check Items"
        }

        binding.addItemsCardView.setOnClickListener {
            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopAddItemsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Add Items"
        }

        binding.editItemsCardView.setOnClickListener {
            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopEditItemsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Edit Items"
        }

        binding.removeItemsCardView.setOnClickListener {
            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopRemoveItemsFragment()).commitAllowingStateLoss()
            supportActionBar!!.title = "Remove Items"
        }

    }
}