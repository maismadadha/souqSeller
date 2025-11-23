package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.OrderDetailsAdapter
import com.example.souqseller.activities.viewModel.OrdersViewModel
import com.example.souqseller.databinding.ActivityOrderDetailsBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var viewModel: OrdersViewModel
    private lateinit var adapter: OrderDetailsAdapter

    var orderId: Int = 0
    private val dateFormatter =
        DateTimeFormatter.ofPattern("d MMMM", Locale("ar"))
    private val timeFormatter =
        DateTimeFormatter.ofPattern("h:mm a", Locale("ar"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_details)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
123456

        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

        adapter = OrderDetailsAdapter(emptyList())
        binding.rvCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCart.adapter = adapter

        orderId = intent.getIntExtra("order_id", 0)
        viewModel.getOrderDetails(orderId)
        observeOrderDetails()


        binding.back.setOnClickListener {
            finish()
        }


    }

    private fun observeOrderDetails() {
        viewModel.observeOrderDetailsLiveData().observe(this) { order ->
            binding.customerName.text = order.customer_name ?: "زبون"
            binding.orderNumber.text = "#${order.id}"

            binding.totalPrice.text = order.total_price
            binding.note.text = order.note ?: "لا يوجد ملاحظات"

            val zoned = ZonedDateTime.parse(order.created_at)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))

            binding.orderDate.text = dateFormatter.format(zoned)
            binding.orderTime.text = timeFormatter.format(zoned)
            // المنتجات
            adapter.setItems(order.items)

        }
    }
}