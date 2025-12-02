package com.example.souqseller.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.souqseller.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.souqseller.activities.adapters.CategoriesPagerAdapter
import com.example.souqseller.activities.viewModel.StoreManagementViewModel
import com.example.souqseller.databinding.FragmentManagementBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText


class ManagementFragment : Fragment() {
    private lateinit var binding: FragmentManagementBinding
    private lateinit var viewModel: StoreManagementViewModel
    private var sellerId: Int = 0

    private lateinit var pagerAdapter: CategoriesPagerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[StoreManagementViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("souq_prefs", AppCompatActivity.MODE_PRIVATE)
        sellerId = prefs.getInt("SELLER_ID", 0)

        viewModel.getStoreCategories(sellerId)
        observeCategoriesLiveData()

        binding.btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }
    }


    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val input = dialogView.findViewById<TextInputEditText>(R.id.etCategoryName)
        val btnAdd = dialogView.findViewById<TextView>(R.id.btnAddCategoryDialog)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancelDialog)

        btnAdd.setOnClickListener {

            val name = input.text.toString().trim()
            if (name.isEmpty()) {
                input.error = "أدخل اسم الفئة"
                return@setOnClickListener
            }

            viewModel.addStoreCategory(sellerId, name)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }


    private fun observeCategoriesLiveData() {
        viewModel.getStoreCategoriesLiveData().observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) return@observe

            pagerAdapter = CategoriesPagerAdapter(requireActivity(), list, sellerId)
            binding.viewPagerCategories.adapter = pagerAdapter

            binding.tabLayoutCategories.removeAllTabs()

            TabLayoutMediator(
                binding.tabLayoutCategories,
                binding.viewPagerCategories
            ) { tab, pos ->
                tab.text = list[pos].name
            }.attach()


            binding.tabLayoutCategories.post {
                val tabStrip = binding.tabLayoutCategories.getChildAt(0) as ViewGroup
                for (i in 0 until tabStrip.childCount) {
                    val tabView = tabStrip.getChildAt(i)
                    val params = tabView.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(20, 0, 20, 0)
                    tabView.layoutParams = params
                    tabView.setPadding(40, 20, 40, 20)
                }
            }
        }
    }


}
