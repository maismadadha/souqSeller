package com.example.souqseller.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.activities.activities.OrderDetailsActivity
import com.example.souqseller.activities.adapters.PreparingOrdersAdapter
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderResponse
import com.example.souqseller.activities.viewModel.OrdersViewModel
import com.example.souqseller.databinding.FragmentReadyOrdersBinding

class ReadyOrdersFragment : Fragment() {

    private lateinit var binding: FragmentReadyOrdersBinding
    private lateinit var viewModel: OrdersViewModel
    private var sellerId: Int = 0

    private val readyList = mutableListOf<OrderResponse>()
    private val outForDeliveryCash = mutableListOf<OrderResponse>()
    private val deliveredCash = mutableListOf<OrderResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs =
            requireContext().getSharedPreferences("souq_prefs", AppCompatActivity.MODE_PRIVATE)
        sellerId = prefs.getInt("SELLER_ID", 0)

        observeOrders()
        loadOrders()
    }

    private fun loadOrders() {
        readyList.clear()
        outForDeliveryCash.clear()
        deliveredCash.clear()

        viewModel.getOrdersByStatus(sellerId, "READY_FOR_PICKUP")
        viewModel.getOrdersByStatus(sellerId, "OUT_FOR_DELIVERY")
        viewModel.getOrdersByStatus(sellerId, "DELIVERED")
    }

    private fun observeOrders() {
        viewModel.observeOrdersLiveData().observe(viewLifecycleOwner) { orders ->
            if (orders.isNullOrEmpty()) return@observe

            when (orders.first().status) {

                "READY_FOR_PICKUP" -> {
                    readyList.clear()
                    readyList.addAll(orders)
                }

                "OUT_FOR_DELIVERY" -> {
                    outForDeliveryCash.clear()
                    outForDeliveryCash.addAll(
                        orders.filter { it.payment_method == "cash" }
                    )
                }

                "DELIVERED" -> {
                    deliveredCash.clear()
                    deliveredCash.addAll(
                        orders.filter { it.payment_method == "cash" }
                    )
                }
            }

            val finalList = mutableListOf<OrderResponse>()
            finalList.addAll(readyList)
            finalList.addAll(outForDeliveryCash)
            finalList.addAll(deliveredCash)

            setupRecycler(finalList)
        }
    }

    private fun setupRecycler(list: List<OrderResponse>) {
        val adapter = PreparingOrdersAdapter(
            list,
            object : OnClick {
                override fun onClick(position: Int) {
                    val item = list[position]

                    // 👇 نعطل الدخول إذا قيد التوصيل
                    if (item.status == "OUT_FOR_DELIVERY") {
                        android.widget.Toast.makeText(
                            requireContext(),
                            "بانتظار وصول الطلب حتى تتمكن من استلام المبلغ",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val intent =
                        Intent(requireContext(), OrderDetailsActivity::class.java)
                    intent.putExtra("order_id", item.id)
                    startActivity(intent)
                }
            }
        )

        binding.rvPreparingOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPreparingOrders.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }
}
