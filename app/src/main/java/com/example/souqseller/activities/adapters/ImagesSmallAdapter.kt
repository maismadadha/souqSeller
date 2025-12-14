package com.example.souqseller.activities.adapters

import com.example.souqseller.R
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.souqseller.databinding.ItemSmallImageBinding

class ImagesSmallAdapter(
    private val images: List<Uri>,
    private val onClick: (Uri) -> Unit
) : RecyclerView.Adapter<ImagesSmallAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSmallImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) {
            Glide.with(binding.root.context)
                .load(uri)
                .into(binding.ivSmallImage)

            binding.ivSmallImage.setOnClickListener {
                onClick(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSmallImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }
}
