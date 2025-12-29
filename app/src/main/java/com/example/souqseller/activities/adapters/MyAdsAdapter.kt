package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.souqseller.R
import com.example.souqseller.activities.pojo.SliderAd
import com.example.souqseller.databinding.RvSliderItemsBinding

class MyAdsAdapter(
    private var items: List<SliderAd>
) : RecyclerView.Adapter<MyAdsAdapter.ViewHolder>() {

    class ViewHolder(val binding: RvSliderItemsBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RvSliderItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val ad = items[position]

        holder.binding.titleSlider.text = ad.title ?: ""
        holder.binding.descriptionSlider.text = ad.description ?: ""

        Glide.with(holder.itemView.context)
            .load(ad.image_url)
            .placeholder(R.drawable.category) // أو placeholder رمادي
            .error(R.drawable.category)
            .centerCrop()
            .into(holder.binding.imgSlider)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<SliderAd>?) {
        items = newItems ?: emptyList()
        notifyDataSetChanged()
    }
}
