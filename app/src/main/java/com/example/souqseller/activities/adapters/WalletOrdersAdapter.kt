package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.R
import com.example.souqseller.activities.pojo.OrderResponse
import com.example.souqseller.databinding.RvOrderItemWalletBinding

class WalletOrdersAdapter(
    private val orders: List<OrderResponse>
) : RecyclerView.Adapter<WalletOrdersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvOrderItemWalletBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvOrderItemWalletBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        holder.binding.tvOrderNumber.text = "#${order.id}"
        holder.binding.orderDate.text = order.created_at?.take(10) ?: ""
        holder.binding.tvOrderStatus.text = walletStatus(order)

        val income = orderIncome(order)

        if (income > 0) {
            holder.binding.tvOrderAmount.text =
                "+ ${String.format("%.2f JD", income)}"
            holder.binding.tvOrderAmount.setTextColor(
                holder.itemView.context.getColor(R.color.green_for_price)
            )
        } else {
            holder.binding.tvOrderAmount.text =
                "- ${String.format("%.2f JD", order.total_price.toDouble())}"
            holder.binding.tvOrderAmount.setTextColor(
                holder.itemView.context.getColor(R.color.red)
            )
        }

    }

    override fun getItemCount(): Int = orders.size

    private fun orderIncome(order: OrderResponse): Double {
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

    private fun walletStatus(order: OrderResponse): String {
        return when {
            order.payment_method == "card" ->
                "مدفوع"

            order.payment_method == "cash" &&
                    order.status == "CASH_COLLECTED" ->
                "مدفوع"

            else ->
                "قيد التنفيذ"
        }
    }

}
