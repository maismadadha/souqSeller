package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.databinding.RvDoneOrdersItemBinding
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderResponse
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.view.View
import com.example.souqseller.R


class DoneOrdersAdapter(
    var orders: List<OrderResponse>,
    val listener: OnClick
) : RecyclerView.Adapter<DoneOrdersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvDoneOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val dateFormatter =
        DateTimeFormatter.ofPattern("d MMMM", Locale("ar"))
    private val timeFormatter =
        DateTimeFormatter.ofPattern("h:mm a", Locale("ar"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvDoneOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orders[position]

        holder.binding.orderDate.text = formatDate(item.created_at)
        holder.binding.orderTime.text = formatTime(item.created_at)

        holder.binding.customerName.text = item.customer_name ?: "زبون"

        holder.binding.orderNumber.text = "#${item.id}"

        holder.binding.productCount.text = "${item.items_count}"

        holder.binding.paymentMethod.text = paymentMethodToArabic(item.payment_method)

        holder.binding.totalPrice.text = item.total_price

        when {
            item.status == "OUT_FOR_DELIVERY" && item.payment_method == "card" -> {
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "قيد التوصيل • مدفوع"
                holder.binding.status.setTextColor(
                    holder.itemView.context.getColor(R.color.mauve)
                )
            }

            else -> {
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "مدفوع"
                holder.binding.status.setTextColor(
                    holder.itemView.context.getColor(R.color.green_for_price)
                )
            }
        }

        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
    }

    override fun getItemCount(): Int = orders.size

    private fun formatDate(dateString: String): String {
        return try {
            val zoned = ZonedDateTime.parse(dateString)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))
            dateFormatter.format(zoned)
        } catch (e: Exception) {
            "خطأ في التاريخ"
        }
    }

    private fun formatTime(dateString: String): String {
        return try {
            val zoned = ZonedDateTime.parse(dateString)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))
            timeFormatter.format(zoned)
        } catch (e: Exception) {
            "خطأ في الوقت"
        }
    }

    private fun paymentMethodToArabic(method: String?): String {
        if (method.isNullOrBlank()) return "غير محدد"
        return when (method.lowercase()) {
            "cash" -> "كاش"
            "card" -> "بطاقة"
            else -> method
        }
    }
}
