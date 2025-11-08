package com.example.souqseller.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.R
import com.example.souqseller.activities.activities.OrderDetailsActivity
import com.example.souqseller.activities.adapters.NewOrdersAdapter
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.databinding.FragmentNewOrdersBinding

class NewOrdersFragment : Fragment() {

    private lateinit var binding: FragmentNewOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentNewOrdersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NewOrdersAdapter(object : OnClick{
            override fun onClick(position: Int) {
                val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
                startActivity(intent)
            }
        })
        binding.rvNewOrders.adapter = adapter
        binding.rvNewOrders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }


}