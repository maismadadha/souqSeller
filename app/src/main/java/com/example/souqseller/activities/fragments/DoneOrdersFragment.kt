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
import com.example.souqseller.activities.adapters.DoneOrdersAdapter
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderResponse
import com.example.souqseller.activities.viewModel.OrdersViewModel
import com.example.souqseller.databinding.FragmentDoneOrdersBinding

class DoneOrdersFragment : Fragment() {

    private lateinit var binding: FragmentDoneOrdersBinding
    private lateinit var viewModel: OrdersViewModel
    private var sellerId: Int = 0

    // قوائم منفصلة لكل حالة
    private val outForDeliveryCard = mutableListOf<OrderResponse>()
    private val deliveredCard = mutableListOf<OrderResponse>()
    private val cashCollected = mutableListOf<OrderResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneOrdersBinding.inflate(inflater, container, false)
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
        outForDeliveryCard.clear()
        deliveredCard.clear()
        cashCollected.clear()

        viewModel.getOrdersByStatus(sellerId, "OUT_FOR_DELIVERY")
        viewModel.getOrdersByStatus(sellerId, "DELIVERED")
        viewModel.getOrdersByStatus(sellerId, "CASH_COLLECTED")
    }

    private fun observeOrders() {
        viewModel.observeOrdersLiveData().observe(viewLifecycleOwner) { orders ->
            if (orders.isNullOrEmpty()) return@observe

            when (orders.first().status) {

                "OUT_FOR_DELIVERY" -> {
                    outForDeliveryCard.clear()
                    outForDeliveryCard.addAll(
                        orders.filter { it.payment_method == "card" }
                    )
                }

                "DELIVERED" -> {
                    deliveredCard.clear()
                    deliveredCard.addAll(
                        orders.filter { it.payment_method == "card" }
                    )
                }

                "CASH_COLLECTED" -> {
                    cashCollected.clear()
                    cashCollected.addAll(orders)
                }
            }

            val finalList = mutableListOf<OrderResponse>()
            finalList.addAll(outForDeliveryCard)
            finalList.addAll(deliveredCard)
            finalList.addAll(cashCollected)

            setupRecycler(finalList)
        }
    }

    private fun setupRecycler(list: List<OrderResponse>) {
        val adapter = DoneOrdersAdapter(
            list,
            object : OnClick {
                override fun onClick(position: Int) {
                    val orderId = list[position].id
                    val intent =
                        Intent(requireContext(), OrderDetailsActivity::class.java)
                    intent.putExtra("order_id", orderId)
                    startActivity(intent)
                }
            }
        )

        binding.rvDoneOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDoneOrders.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }
}
