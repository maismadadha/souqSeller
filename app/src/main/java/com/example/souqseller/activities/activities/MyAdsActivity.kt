package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.souqseller.activities.adapters.MyAdsAdapter
import com.example.souqseller.activities.viewModel.SliderAdViewModel
import com.example.souqseller.databinding.ActivityMyAdsBinding

class MyAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAdsBinding
    private lateinit var adapter: MyAdsAdapter
    private lateinit var viewModel: SliderAdViewModel

    private var storeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMyAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val prefs = getSharedPreferences("souq_prefs", MODE_PRIVATE)
        storeId = prefs.getInt("SELLER_ID", 0)

        viewModel = ViewModelProvider(this)[SliderAdViewModel::class.java]

        binding.back.setOnClickListener { finish() }

        setupRecyclerView()
        observeViewModel()


        viewModel.getSliderAds(storeId)
    }


    private fun setupRecyclerView() {
        adapter = MyAdsAdapter(emptyList())

        binding.rvSlider.adapter = adapter
        binding.rvSlider.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }


    private fun observeViewModel() {

        viewModel.getSliderAdsLive().observe(this) { ads ->
            adapter.setItems(ads)
        }

        viewModel.getError().observe(this) {

        }
    }
}
