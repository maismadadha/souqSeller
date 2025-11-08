package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.databinding.RvNewOrdersItemBinding

class NewOrdersAdapter(val OnClick: OnClick): RecyclerView.Adapter<NewOrdersAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvNewOrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvNewOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.binding.orderNumber.text="#2137182"
        holder.itemView.setOnClickListener {
            OnClick.onClick(position)
        }
    }

    override fun getItemCount(): Int =10
}