package com.example.souqseller.activities.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.souqseller.activities.fragments.ProductsManagerFragment
import com.example.souqseller.activities.pojo.StoreCategoriesItem

class CategoriesPagerAdapter(
    activity: FragmentActivity,
    private var categories: List<StoreCategoriesItem>,
    private val storeId: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        val category = categories[position]
        return ProductsManagerFragment.newInstance(category.id, storeId)
    }



}
