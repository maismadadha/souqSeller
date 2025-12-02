package com.example.souqseller.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.activities.activities.AddProductActivity
import com.example.souqseller.activities.adapters.ProductsManagerAdapter
import com.example.souqseller.activities.pojo.Product
import com.example.souqseller.activities.viewModel.StoreManagementViewModel
import com.example.souqseller.databinding.FragmentProductsManagerBinding

class ProductsManagerFragment : Fragment() {

    companion object {
        private const val ARG_CATEGORY_ID = "category_id"
        private const val ARG_STORE_ID = "store_id"

        fun newInstance(categoryId: Int, storeId: Int): ProductsManagerFragment {
            return ProductsManagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CATEGORY_ID, categoryId)
                    putInt(ARG_STORE_ID, storeId)
                }
            }
        }
    }

    private var categoryId = 0
    private var storeId = 0

    private lateinit var binding: FragmentProductsManagerBinding
    private lateinit var viewModel: StoreManagementViewModel
    private lateinit var adapter: ProductsManagerAdapter
    private val list = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt(ARG_CATEGORY_ID) ?: 0
        storeId = arguments?.getInt(ARG_STORE_ID) ?: 0
        viewModel = ViewModelProvider(requireActivity())[StoreManagementViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductsManagerAdapter(list)
        binding.rvProducts.apply {
            adapter = this@ProductsManagerFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        observeProductsLiveData()


        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            intent.putExtra("storeId", storeId)
            intent.putExtra("categoryId", categoryId)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategoryProducts(categoryId)
    }

    private fun observeProductsLiveData() {
        viewModel.getLiveCategoryProducts().observe(viewLifecycleOwner) { products ->
            list.clear()
            list.addAll(products)
            adapter.notifyDataSetChanged()
        }
    }
}
