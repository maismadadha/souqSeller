package com.example.souqseller.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.souqseller.R
import com.example.souqseller.activities.activities.AccountInformationActivity
import com.example.souqseller.activities.activities.AddressesActivity
import com.example.souqseller.activities.activities.StoreCategoriesActivity
import com.example.souqseller.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.storeInformation.setOnClickListener {
            val intent = Intent(requireContext(), AccountInformationActivity::class.java)
            startActivity(intent)
        }

        binding.storeAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressesActivity::class.java)
            startActivity(intent)
        }

        binding.storeCategories.setOnClickListener {
            val intent = Intent(requireContext(), StoreCategoriesActivity::class.java)
            startActivity(intent)
        }

    }

}