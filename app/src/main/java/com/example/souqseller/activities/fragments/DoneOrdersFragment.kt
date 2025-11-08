package com.example.souqseller.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.DoneOrdersAdapter
import com.example.souqseller.databinding.FragmentDoneOrdersBinding


class DoneOrdersFragment : Fragment() {

    private lateinit var binding: FragmentDoneOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DoneOrdersAdapter()
        binding.rvDoneOrders.adapter = adapter
        binding.rvDoneOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


    }

}