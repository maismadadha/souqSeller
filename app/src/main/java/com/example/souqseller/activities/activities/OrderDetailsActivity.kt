package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.OrderDetailsAdapter
import com.example.souqseller.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_details)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCart.adapter= OrderDetailsAdapter()
        binding.rvCart.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



    }
}