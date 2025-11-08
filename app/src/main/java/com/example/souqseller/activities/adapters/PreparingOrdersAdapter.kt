package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.souqseller.databinding.RvPreparingOrdersItemBinding

class PreparingOrdersAdapter(): RecyclerView.Adapter<PreparingOrdersAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: RvPreparingOrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvPreparingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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