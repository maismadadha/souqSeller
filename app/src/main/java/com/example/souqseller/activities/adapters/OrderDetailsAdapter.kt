package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.databinding.RvCartItemsBinding

class OrderDetailsAdapter(): RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvCartItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvCartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.itemName.text="معطف"

    }

    override fun getItemCount(): Int =10
}