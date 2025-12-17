package com.example.souqseller.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.interface0.OnClick
import com.example.souqseller.activities.pojo.AddressDto
import com.example.souqseller.databinding.RvMyAddressesItemBinding

class MyAddressesAdapter(
    private var addresses: List<AddressDto>,
    private val listener: (AddressDto) -> Unit
) : RecyclerView.Adapter<MyAddressesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvMyAddressesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: AddressDto) {
            binding.addressName.text = address.address_name
            binding.addressDescription.text =
                "${address.city_name}, ${address.street ?: ""}, ${address.building_number ?: ""}"

            // ✅ الصح يظهر حسب الديفولت من السيرفر
            binding.selectedAddressCheckMark.visibility =
                if (address.is_default == 1) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {
                listener(address)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvMyAddressesItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount(): Int = addresses.size

    fun updateList(newAddresses: List<AddressDto>) {
        addresses = newAddresses
        notifyDataSetChanged() // 🔥 مهم
    }
}
