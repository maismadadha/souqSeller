package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.pojo.StoreCategoriesItem
import com.example.souqseller.databinding.RvMyCategoriesBinding

class MyCategoriesAdapter(
    private var categories: List<StoreCategoriesItem>
) : RecyclerView.Adapter<MyCategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvMyCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: StoreCategoriesItem) {
            binding.categoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvMyCategoriesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateList(newList: List<StoreCategoriesItem>) {
        categories = newList
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): StoreCategoriesItem =
        categories[position]
}
