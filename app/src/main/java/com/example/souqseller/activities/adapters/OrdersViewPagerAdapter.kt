package com.example.souqseller.activities.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.souqseller.activities.fragments.DoneOrdersFragment
import com.example.souqseller.activities.fragments.NewOrdersFragment
import com.example.souqseller.activities.fragments.PreparingOrdersFragment

class OrdersViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewOrdersFragment()
            1 -> PreparingOrdersFragment()
            2 -> DoneOrdersFragment()
            else -> NewOrdersFragment()
        }
    }

    override fun getItemCount(): Int =3
}