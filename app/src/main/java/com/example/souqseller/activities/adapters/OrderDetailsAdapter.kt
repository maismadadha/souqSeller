package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.OrderItem
import com.example.souqseller.activities.pojo.Product
import com.example.souqseller.databinding.RvCartItemsBinding

class OrderDetailsAdapter( private var items: List<OrderItem>): RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvCartItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvCartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    //

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]

        holder.binding.itemDescription.text = item.product?.description ?: ""
        val customizationText = item.customizations
            ?.entries
            ?.joinToString(" • ") { (key, value) -> "$key: $value" }
            ?: ""

        if (customizationText.isEmpty()) {
            holder.binding.itemCustomizations.visibility = View.GONE
        } else {
            holder.binding.itemCustomizations.visibility = View.VISIBLE
            holder.binding.itemCustomizations.text = customizationText
        }
        holder.binding.itemName.text = item.product?.name ?: "منتج"
        holder.binding.itemCount.text = "${item.quantity}"
        holder.binding.itemPrice.text = "${item.price}"

        Glide.with(holder.itemView.context)
            .load(item.product?.main_image_url)
            .into(holder.binding.itemImage)
    }

    override fun getItemCount(): Int =items.size

    fun setItems(newItems: List<OrderItem>?) {
        items = newItems ?: emptyList()   // ← أهم سطر
        notifyDataSetChanged()
    }

}