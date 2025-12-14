package com.example.souqseller.activities.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.pojo.ProductOptionsItem
import com.example.souqseller.activities.pojo.Value
import com.example.souqseller.databinding.RvItemOptionGroupEditorBinding

class OptionGroupsAdapter(
    private val optionGroups: MutableList<ProductOptionsItem>
) : RecyclerView.Adapter<OptionGroupsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemOptionGroupEditorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductOptionsItem) {


            binding.etOptionName.setText(item.name ?: "")
            binding.etOptionName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.name = s?.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })



            val valuesList = item.values as ArrayList<Value>

            val valuesAdapter = OptionValuesAdapter(valuesList)
            binding.rvOptionValues.adapter = valuesAdapter
            binding.rvOptionValues.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)



            binding.btnAddValue.setOnClickListener {

                val newValue = Value(
                    created_at = "",
                    id = null,
                    label = "",
                    option_id = item.id ?: 0,  // لو null لاحقًا الباك يجددها
                    price_delta = "0",
                    sort_order = valuesList.size,
                    updated_at = "",
                    value = ""
                )

                valuesList.add(newValue)
                valuesAdapter.notifyItemInserted(valuesList.size - 1)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemOptionGroupEditorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(optionGroups[position])
    }

    override fun getItemCount(): Int = optionGroups.size
}
