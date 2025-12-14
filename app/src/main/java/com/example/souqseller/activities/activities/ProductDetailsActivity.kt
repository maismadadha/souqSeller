package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.ProductImagesAdapter
import com.example.souqseller.activities.adapters.ProductOptionsAdapterForManager
import com.example.souqseller.activities.pojo.ProductImages
import com.example.souqseller.activities.pojo.ProductOptions
import com.example.souqseller.activities.viewModel.ProductViewModel
import com.example.souqseller.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var viewModel: ProductViewModel

    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra("productId", 0)

        // back
        binding.back.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // استدعاء الـ APIs
        viewModel.getProductById(productId)
        viewModel.getProductImages(productId)
        observeImagesLiveData()
        viewModel.getProductOptions(productId)

        // مراقبة البيانات
        observeData()
    }

    private fun observeImagesLiveData() {
viewModel.getLiveImages().observe(this){images->
    val adapter=ProductImagesAdapter(images)
    binding.rvProductImages.adapter=adapter
    binding.rvProductImages.layoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(binding.rvProductImages)
    binding.indicator.attachToRecyclerView(binding.rvProductImages, snapHelper)
    adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)

}
    }

    private fun observeData() {
        viewModel.getLiveProductById().observe(this) { product ->
            if (product != null) {
                binding.productName.text = product.name
                binding.productName2.text = product.name
                binding.productDescription.text = product.description ?: ""
                binding.productPrice.text = product.price ?: "0"
            }
        }


        viewModel.getLiveOptions().observe(this) { options ->
            if (options != null) {
                setupOptionsRecycler(options)
            }
        }
    }



    private fun setupOptionsRecycler(options: ProductOptions) {

        val adapter = ProductOptionsAdapterForManager()
        adapter.submitList(options)

        binding.rvOptions.adapter = adapter
        binding.rvOptions.layoutManager = LinearLayoutManager(this)
        binding.rvOptions.itemAnimator = null
    }


}