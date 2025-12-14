package com.example.souqseller.activities.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.souqseller.activities.pojo.ProductImages
import com.example.souqseller.activities.pojo.ProductImagesItem
import com.example.souqseller.databinding.RvProductImagesItemBinding
import kotlin.collections.get

class ProductImagesAdapter(
    val images: ProductImages

): RecyclerView.Adapter<ProductImagesAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RvProductImagesItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvProductImagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item=images[position]
        Glide.with(holder.itemView.context)
            .load(item.image_url)
            .into(holder.binding.productImg)

    }

    override fun getItemCount(): Int =images.size
}