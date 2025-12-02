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
import com.example.souqseller.activities.viewModel.OrdersViewModel
import com.example.souqseller.databinding.FragmentPreparingOrdersBinding

class PreparingOrdersFragment : Fragment() {

    private lateinit var binding: FragmentPreparingOrdersBinding
    private lateinit var viewModel: OrdersViewModel
    private var sellerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreparingOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs =
            requireContext().getSharedPreferences("souq_prefs", AppCompatActivity.MODE_PRIVATE)
        sellerId = prefs.getInt("SELLER_ID", 0)

        // الطلبات قيد التحضير
        viewModel.getOrdersByStatus(sellerId, "PREPARING")

        viewModel.observeOrdersLiveData().observe(viewLifecycleOwner) { orders ->
            val adapter = PreparingOrdersAdapter(
                orders,
                object : OnClick {
                    override fun onClick(position: Int) {
                        val orderId = orders[position].id
                        val intent =
                            Intent(requireContext(), OrderDetailsActivity::class.java)
                        intent.putExtra("order_id", orderId)
                        startActivity(intent)
                    }
                }
            )
            binding.rvPreparingOrders.adapter = adapter
            binding.rvPreparingOrders.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getOrdersByStatus(sellerId,"PREPARING")
    }
}
