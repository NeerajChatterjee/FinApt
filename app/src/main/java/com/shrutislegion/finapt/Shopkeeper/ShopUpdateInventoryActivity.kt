package com.shrutislegion.finapt.Shopkeeper

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
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

        val fragmentLayout = FrameLayout(this)
        // set the layout params to fill the activity
        fragmentLayout.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )


        binding.checkItemsCardView.setOnClickListener {
            // set an id to the layout
            fragmentLayout.id = R.id.shop_updateInventory_fragment_container // some positive integer

            // set the layout as Activity content
            setContentView(fragmentLayout)
            fragmentManager
                .add(R.id.shop_updateInventory_fragment_container, ShopCheckItemsFragment()).commit()
            supportActionBar!!.title = "Check Items"
        }

        binding.addItemsCardView.setOnClickListener {
            // set an id to the layout
            fragmentLayout.id = R.id.shop_updateInventory_fragment_container // some positive integer

            // set the layout as Activity content
            setContentView(fragmentLayout)
            fragmentManager.add(R.id.shop_updateInventory_fragment_container, ShopAddItemsFragment()).commit()
            supportActionBar!!.title = "Add Items"
        }

        binding.editItemsCardView.setOnClickListener {
            // set an id to the layout
            fragmentLayout.id = R.id.shop_updateInventory_fragment_container // some positive integer

            // set the layout as Activity content
            setContentView(fragmentLayout)
            fragmentManager.add(R.id.shop_updateInventory_fragment_container, ShopEditItemsFragment()).commit()
            supportActionBar!!.title = "Edit Items"
        }

        binding.removeItemsCardView.setOnClickListener {
            // set an id to the layout
            fragmentLayout.id = R.id.shop_updateInventory_fragment_container // some positive integer

            // set the layout as Activity content
            setContentView(fragmentLayout)
            fragmentManager.replace(R.id.shop_updateInventory_fragment_container, ShopRemoveItemsFragment()).commit()
            supportActionBar!!.title = "Remove Items"
        }

    }
}