package com.example.souqseller.activities.adapters

import android.R.attr.text
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.activities.pojo.Value
import com.example.souqseller.databinding.RvItemOptionValueEditorBinding

class OptionValuesAdapter(
    private val values: MutableList<Value>
) : RecyclerView.Adapter<OptionValuesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemOptionValueEditorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Value) {

            binding.etValueLabel.setText(item.label ?: "")

            binding.etValueLabel.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val text = s?.toString()?.trim().orEmpty()
                    item.label = text
                    item.value = text
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemOptionValueEditorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size
}
