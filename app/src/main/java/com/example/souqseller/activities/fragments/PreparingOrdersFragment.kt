package com.example.souqseller.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.NewOrdersAdapter
import com.example.souqseller.activities.adapters.PreparingOrdersAdapter
import com.example.souqseller.databinding.FragmentPreparingOrdersBinding


class PreparingOrdersFragment : Fragment() {

    private lateinit var binding: FragmentPreparingOrdersBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreparingOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PreparingOrdersAdapter()
        binding.rvPreparingOrders.adapter = adapter
        binding.rvPreparingOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

}