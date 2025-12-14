package com.example.souqseller.activities.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.souqseller.activities.adapters.ImagesSmallAdapter
import com.example.souqseller.activities.adapters.OptionGroupsAdapter
import com.example.souqseller.activities.pojo.CreateOptionValueRequest
import com.example.souqseller.activities.pojo.CreateProductOptionRequest
import com.example.souqseller.activities.pojo.ProductCreateRequest
import com.example.souqseller.activities.pojo.ProductOptionsItem
import com.example.souqseller.activities.viewModel.ProductViewModel
import com.example.souqseller.databinding.ActivityAddProductBinding

import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: ProductViewModel

    private var storeId = 0
    private var categoryId = 0

    private var createdProductId: Int = 0
    private var currentOptionIndex = 0
    private var currentValueIndex = 0

    private var currentOptionId: Int = 0



    private val optionGroups = ArrayList<ProductOptionsItem>()
    private lateinit var optionGroupsAdapter: OptionGroupsAdapter

    private val imageUris = mutableListOf<Uri>()//للعرض
    private val imageBytes = mutableListOf<ByteArray>()//للرفع

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.isNullOrEmpty()) return@registerForActivityResult

            uris.forEach { uri ->
                val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                if (bytes != null) {
                    imageUris.add(uri)
                    imageBytes.add(bytes)
                }
            }

            binding.ivMainImage.setImageURI(imageUris.last())
            binding.rvAllImages.visibility = View.VISIBLE
            setupImagesRecycler()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        storeId = intent.getIntExtra("storeId", 0)
        categoryId = intent.getIntExtra("categoryId", 0)

        binding.tvTitle.text = "إضافة منتج"
        binding.btnSave.text = "حفظ"

        observeViewModel()

        optionGroupsAdapter = OptionGroupsAdapter(optionGroups)
        binding.rvOptionsEditor.layoutManager = LinearLayoutManager(this)
        binding.rvOptionsEditor.adapter = optionGroupsAdapter

        binding.btnAddOptionGroup.setOnClickListener {
            optionGroups.add(
                ProductOptionsItem(
                    id = null,
                    product_id = 0,
                    name = "",
                    label = "",
                    selection = "single",
                    required = 1,
                    affects_variant = 1,
                    sort_order = optionGroups.size,
                    created_at = "",
                    updated_at = "",
                    values = ArrayList()
                )
            )
            optionGroupsAdapter.notifyItemInserted(optionGroups.size - 1)
        }

        binding.btnAddImage.setOnClickListener {
            pickImagesLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener { saveProduct() }
        binding.back.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.getLiveNewProduct().observe(this) { product ->
            product ?: return@observe

            createdProductId = product.id
            currentOptionIndex = 0
            createNextOption()
        }


        viewModel.getLiveNewOption().observe(this) { option ->
            option ?: return@observe

            currentOptionId = option.id!!
            currentValueIndex = 0
            createNextValue()
        }


        viewModel.getLiveNewOptionValue().observe(this) {
            createNextValue()
        }

        viewModel.getLiveError().observe(this){msg ->
            msg ?: return@observe
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            binding.btnSave.isEnabled = true

        }


    }
    private fun saveProduct() {
        val name = binding.etName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val price = binding.etPrice.text.toString().trim().toDoubleOrNull()

        if (name.isEmpty() || price == null) {
            Toast.makeText(this, "أدخل الاسم والسعر", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnSave.isEnabled = false

        viewModel.createProduct(
            ProductCreateRequest(
                store_id = storeId,
                store_category_id = categoryId,
                name = name,
                description = description.ifEmpty { null },
                price = price,
                preparation_time = "00:00:00"
            )
        )
    }
    private fun createNextOption() {

        if (currentOptionIndex >= optionGroups.size) {
            //  ارفع الصور
            uploadImages(createdProductId)
            return
        }

        val option = optionGroups[currentOptionIndex]

        viewModel.createProductOption(
            createdProductId,
            CreateProductOptionRequest(
                name = option.name ?: "option_$currentOptionIndex",
                label = option.label,
                selection = option.selection,
                required = option.required == 1,
                affects_variant = option.affects_variant == 1
            )
        )
    }
    private fun createNextValue() {

        val option = optionGroups[currentOptionIndex]
        val values = option.values

        if (currentValueIndex >= values.size) {
            currentOptionIndex++
            createNextOption()
            return
        }

        val value = values[currentValueIndex]
        currentValueIndex++

        if (value.value.isBlank()) {
            createNextValue()
            return
        }

        viewModel.createOptionValue(
            currentOptionId,
            CreateOptionValueRequest(
                value = value.value.trim(),
                label = value.label,
                price_delta = value.price_delta.toString().toDoubleOrNull() ?: 0.0
            )
        )
    }

    private fun uploadImages(productId: Int) {
        if (imageBytes.isEmpty()) {
            finishSuccess()
            return
        }

        var remaining = imageBytes.size

        imageBytes.forEach { bytes ->
            val body = RequestBody.create(MediaType.parse("image/*"), bytes)
            val part = MultipartBody.Part.createFormData("image", "image.png", body)

            lifecycleScope.launch {
                viewModel.uploadImage(productId, part)
                remaining--
                if (remaining == 0) finishSuccess()
            }
        }
    }
    private fun finishSuccess() {
        Toast.makeText(this, "تم إنشاء المنتج بنجاح", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun setupImagesRecycler() {
        binding.rvAllImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvAllImages.adapter = ImagesSmallAdapter(imageUris) { uri ->
            Glide.with(this).load(uri).into(binding.ivMainImage)
        }
    }
}
