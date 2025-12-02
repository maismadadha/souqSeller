package com.example.souqseller.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.OrdersViewPagerAdapter
import com.example.souqseller.databinding.FragmentOrderBinding
import com.google.android.material.tabs.TabLayoutMediator


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrdersViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        val tabTitles =arrayOf("جديدة","قيد التجهيز","جاهزة","منتهية")

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            tab.text = tabTitles[position]
        }.attach()

      }




}
