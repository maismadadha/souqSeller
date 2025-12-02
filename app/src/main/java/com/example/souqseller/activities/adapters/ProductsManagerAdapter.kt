package com.example.souqseller.activities.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.souqseller.activities.activities.ProductDetailsActivity
import com.example.souqseller.activities.pojo.Product
import com.example.souqseller.databinding.RvProductsItemBinding

class ProductsManagerAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<ProductsManagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvProductsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvProductsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = products[position]

        holder.binding.productName.text = item.name
        holder.binding.productPrice.text = "${item.price}"
        holder.binding.productDescription.text = item.description ?: ""

        Glide.with(holder.itemView.context)
            .load(item.main_image_url)
            .into(holder.binding.productImg)

        // زر تعديل المنتج
        holder.binding.btnEditProduct.setOnClickListener {

        }


        holder.itemView.setOnClickListener {
           val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", item.id)
            holder.itemView.context.startActivity(intent)
        }
    }
}
