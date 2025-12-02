package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.souqseller.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // نستقبل storeId و categoryId من الفراجمنت
        val storeId = intent.getIntExtra("storeId", 0)
        val categoryId = intent.getIntExtra("categoryId", 0)

        // هون رح نعمل إضافة المنتج بعدين
    }
}
