package com.example.souqseller.activities.activities

import android.R
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.souqseller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val items = listOf(
        "إلكترونيات",
        "ملابس",
        "أدوات منزلية",
        "كتب",
        "وصف قصير",
        "إلكترونيات",
        "ملابس",
        "أدوات منزلية",
        "كتب",
        "وصف قصير",
        "إلكترونيات",
        "ملابس",
        "أدوات منزلية",
        "كتب",
        "وصف قصير"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val etCategory = binding.etCategory
        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.simple_list_item_1,
            items
        ) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent) as TextView
                val color = ContextCompat.getColor(this@MainActivity, com.example.souqseller.R.color.black)
                v.setTextColor(color)
                return v
            }
        }

        etCategory.setAdapter(adapter)
        etCategory.setOnClickListener { etCategory.showDropDown() } // افتح القائمة عند الضغط
        etCategory.setDropDownBackgroundResource(com.example.souqseller.R.drawable.dropdown_bg)


    }// onCreate
}// MainActivity