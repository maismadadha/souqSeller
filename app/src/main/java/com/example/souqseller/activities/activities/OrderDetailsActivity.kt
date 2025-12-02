package com.example.souqseller.activities.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.activities.adapters.OrderDetailsAdapter
import com.example.souqseller.activities.pojo.OrderResponse
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

    private var orderId: Int = 0
    private var currentStatus: String = ""

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ar"))
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale("ar"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

        adapter = OrderDetailsAdapter(emptyList())
        binding.rvCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCart.adapter = adapter

        orderId = intent.getIntExtra("order_id", 0)

        viewModel.getOrderDetails(orderId)
        observeOrderDetails()
        observeError()
        viewModel.observeStatusUpdated().observe(this) { updated ->
            if (updated == true) {
                finish()   // ← هيك بس لما تتغير الحالة يطلعك
            }}

        binding.back.setOnClickListener { finish() }
    }

    private fun observeOrderDetails() {
        viewModel.observeOrderDetailsLiveData().observe(this) { order ->
            currentStatus = order.status ?: ""

            loadOrderToUI(order)
            setupStatusButton()

        }
    }

    private fun observeError() {
        viewModel.observeErrorLiveData().observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadOrderToUI(order: OrderResponse) {

        binding.customerName.text = order.customer_name ?: "زبون"
        binding.orderNumber.text = "#${order.id}"
        binding.totalPrice.text = order.total_price
        binding.note.text = order.note ?: "لا يوجد ملاحظات"

        if (!order.created_at.isNullOrBlank()) {
            val zoned = ZonedDateTime.parse(order.created_at)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))

            binding.orderDate.text = dateFormatter.format(zoned)
            binding.orderTime.text = timeFormatter.format(zoned)

        } else {
            binding.orderDate.text = "—"
            binding.orderTime.text = "—"
        }

        adapter.setItems(order.items)
    }


    private fun setupStatusButton() {
        when (currentStatus) {

            "CONFIRMED" -> {
                binding.statusButton.text = "بدء التجهيز"
                binding.statusButton.isEnabled = true
                binding.cancelButton.visibility = View.VISIBLE

                binding.statusButton.setOnClickListener {
                    showDialog("بدء تجهيز الطلب", "هل تريد بدء تجهيز الطلب؟") {
                        viewModel.updateOrderStatus(orderId, "PREPARING")
                    }
                }

                binding.cancelButton.setOnClickListener {
                    showDialog("إلغاء الطلب", "هل تريد إلغاء الطلب؟") {
                        viewModel.updateOrderStatus(orderId, "CANCELLED")
                    }
                }
            }

            "PREPARING" -> {
                binding.statusButton.text = "جاهز للتوصيل"
                binding.statusButton.isEnabled = true
                binding.cancelButton.visibility = View.GONE

                binding.statusButton.setOnClickListener {
                    showDialog("جاهز للتوصيل", "هل تريد جعل الطلب جاهز للتوصيل؟") {
                        viewModel.updateOrderStatus(orderId, "OUT_FOR_DELIVERY")
                    }
                }
            }

            "OUT_FOR_DELIVERY" -> {
                binding.statusButton.text = "استلمها المندوب"
                binding.statusButton.isEnabled = true
                binding.cancelButton.visibility = View.GONE

                binding.statusButton.setOnClickListener {
                    showDialog("تسليم المندوب", "هل تم استلام الطلب من قبل المندوب؟") {
                        viewModel.updateOrderStatus(orderId, "DELIVERED")
                    }
                }
            }

            "DELIVERED" -> {
                binding.statusButton.text = "تم التسليم"
                binding.statusButton.isEnabled = false
                binding.cancelButton.visibility = View.GONE
            }

            "CANCELLED" -> {
                binding.statusButton.text = "تم إلغاء الطلب"
                binding.statusButton.isEnabled = false
                binding.cancelButton.visibility = View.GONE
            }

            else -> {
                binding.statusButton.text = "غير معروف"
                binding.statusButton.isEnabled = false
                binding.cancelButton.visibility = View.GONE
            }
        }
    }

    private fun showDialog(title: String, msg: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("نعم") { _, _ -> onConfirm() }
            .setNegativeButton("لا", null)
            .show()
    }
}
