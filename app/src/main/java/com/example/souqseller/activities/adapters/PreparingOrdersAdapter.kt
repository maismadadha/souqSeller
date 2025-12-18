package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.R
import com.example.souqseller.databinding.RvPreparingOrdersItemBinding
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderResponse
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PreparingOrdersAdapter(
    var orders: List<OrderResponse>,
    val listener: OnClick
) : RecyclerView.Adapter<PreparingOrdersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvPreparingOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val dateFormatter =
        DateTimeFormatter.ofPattern("d MMMM", Locale("ar"))
    private val timeFormatter =
        DateTimeFormatter.ofPattern("h:mm a", Locale("ar"))

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvPreparingOrdersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = orders[position]


        holder.binding.orderDate.text = formatDate(item.created_at)
        holder.binding.orderTime.text = formatTime(item.created_at)


        holder.binding.customerName.text = item.customer_name ?: "زبون"


        holder.binding.orderNumber.text = "#${item.id}"


        holder.binding.productCount.text = "${item.items_count}"


        holder.binding.paymentMethod.text = paymentMethodToArabic(item.payment_method)


        holder.binding.totalPrice.text = item.total_price

        holder.binding.status.visibility = View.GONE

        // 👇 منطق الحالة
        when (item.status) {

            "READY_FOR_PICKUP" -> {
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "بانتظار السائق"
                holder.binding.status.setTextColor(
                    holder.itemView.context.getColor(R.color.gray_search_text)
                )
            }

            "OUT_FOR_DELIVERY" -> {
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "قيد التوصيل"
                holder.binding.status.setTextColor(
                    holder.itemView.context.getColor(R.color.mauve)
                )
            }
            "DELIVERED" -> {
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "بانتظار استلام المبلغ"
                holder.binding.status.setTextColor(
                    holder.itemView.context.getColor(R.color.green_for_price)
                )
            }

        }


        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener.onClick(pos)
            }
        }
    }

    override fun getItemCount(): Int = orders.size

    private fun formatDate(dateString: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(dateString)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))
            dateFormatter.format(zonedDateTime)
        } catch (e: Exception) {
            "خطأ في التاريخ"
        }
    }

    private fun formatTime(dateString: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(dateString)
                .withZoneSameInstant(ZoneId.of("Asia/Amman"))
            timeFormatter.format(zonedDateTime)
        } catch (e: Exception) {
            "خطأ في الوقت"
        }
    }

    private fun paymentMethodToArabic(method: String?): String {
        if (method.isNullOrBlank()) {
            return "غير محدد"
        }
        return when (method.lowercase()) {
            "cash" -> "كاش"
            "card" -> "بطاقة"
            else   -> method
        }
    }

}
