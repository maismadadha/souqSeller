package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.databinding.RvDoneOrdersItemBinding

class DoneOrdersAdapter(): RecyclerView.Adapter<DoneOrdersAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvDoneOrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvDoneOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.orderNumber.text="#2137182"
    }

    override fun getItemCount(): Int =10
}