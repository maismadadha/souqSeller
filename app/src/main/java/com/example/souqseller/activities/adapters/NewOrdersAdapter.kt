package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.databinding.RvNewOrdersItemBinding
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderResponse
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NewOrdersAdapter(
    var orders: List<OrderResponse>,
    val listener: OnClick
) : RecyclerView.Adapter<NewOrdersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvNewOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val dateFormatter =
        DateTimeFormatter.ofPattern("d MMMM", Locale("ar"))  // مثال: 15 نوفمبر
    private val timeFormatter =
        DateTimeFormatter.ofPattern("h:mm a", Locale("ar"))   // مثال: 9:21 م

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvNewOrdersItemBinding.inflate(
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

        // التاريخ و الوقت
        holder.binding.orderDate.text = formatDate(item.created_at)
        holder.binding.orderTime.text = formatTime(item.created_at)

        // اسم الزبون
        holder.binding.customerName.text = item.customer_name ?: "زبون"

        // رقم الطلب
        holder.binding.orderNumber.text = "#${item.id}"

        // عدد المنتجات
        holder.binding.productCount.text = "${item.items_count}"

        // طريقة الدفع
        holder.binding.paymentMethod.text = paymentMethodToArabic(item.payment_method)

        // السعر الكلي
        holder.binding.totalPrice.text = item.total_price

        // كليك على الكارد
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
