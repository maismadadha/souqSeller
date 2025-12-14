package com.example.souqseller.activities.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.pojo.ProductOptionsItem
import com.example.souqseller.databinding.ItemOptionGroupBinding

class ProductOptionsAdapterForManager :
    RecyclerView.Adapter<ProductOptionsAdapterForManager.ViewHolder>() {

    private var list: List<ProductOptionsItem> = emptyList()

    fun submitList(newList: List<ProductOptionsItem>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemOptionGroupBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvGroupTitle.text = item.name

        // نظف المحتوى
        holder.binding.chipGroup.removeAllViews()

        item.values.forEach { value ->
            val tv = TextView(holder.itemView.context).apply {
                text = "• ${value.label}"
                textSize = 15f
                setPadding(10, 6, 10, 6)
                setTextColor(Color.BLACK)
            }
            holder.binding.chipGroup.addView(tv)
        }
    }
}
