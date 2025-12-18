package com.example.souqseller.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.WalletOrdersAdapter
import com.example.souqseller.activities.pojo.OrderResponse
import com.example.souqseller.activities.viewModel.OrdersViewModel
import com.example.souqseller.databinding.FragmentWalletBinding
import java.util.Locale


class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: OrdersViewModel
    private val walletOrders = mutableListOf<OrderResponse>()
    private lateinit var adapter: WalletOrdersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext()
            .getSharedPreferences("souq_prefs", AppCompatActivity.MODE_PRIVATE)

        val sellerId = prefs.getInt("SELLER_ID", 0)

        adapter = WalletOrdersAdapter(walletOrders)
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter


        observeOrders()
        viewModel.getOrdersForStore(sellerId)
    }

    private fun observeOrders() {
        viewModel.observeOrdersLiveData().observe(viewLifecycleOwner) { orders ->
            if (orders.isNullOrEmpty()) {
                binding.tvTotalIncome.text = "0 JD"
                return@observe
            }

            walletOrders.clear()


            val visibleOrders = orders.filter { shouldShowInWallet(it) }

            walletOrders.addAll(visibleOrders)

            val totalIncome = walletOrders.sumOf { orderIncome(it) }
            binding.tvTotalIncome.text =
                String.format(Locale.US, "%.2f JD", totalIncome)


            android.util.Log.d("WALLET", "Visible orders = ${walletOrders.size}")
            android.util.Log.d("WALLET", "Total income = $totalIncome")



            adapter.notifyDataSetChanged()


            val finishedCount = walletOrders.count { order ->
                orderIncome(order) > 0
            }


            val processingCount = walletOrders.count { order ->
                orderIncome(order) == 0.0
            }



            binding.finishedOrders.text = finishedCount.toString()
            binding.processingOrders.text = processingCount.toString()


        }
    }






    fun shouldShowInWallet(order: OrderResponse): Boolean {
        return order.status !in listOf(
            "ON_CART",
            "CONFIRMED",
            "CANCELLED"
        )
    }

    fun orderIncome(order: OrderResponse): Double {
        return when {
            order.payment_method == "card" &&
                    order.status !in listOf("ON_CART", "CONFIRMED", "CANCELLED") ->
                order.total_price.toDouble()

            order.payment_method == "cash" &&
                    order.status == "CASH_COLLECTED" ->
                order.total_price.toDouble()

            else -> 0.0
        }
    }


}