package com.example.souqseller.activities.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.souqseller.R
import com.example.souqseller.activities.pojo.UpdateSellerProfileRequest
import com.example.souqseller.activities.viewModel.SellerProfileViewModel
import com.example.souqseller.databinding.ActivityAccountInformationBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccountInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountInformationBinding
    private lateinit var viewModel: SellerProfileViewModel

    private var storeId = 0
    private var isEditMode = false

    private var newLogoUri: Uri? = null
    private var newCoverUri: Uri? = null

    private val pickLogoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            newLogoUri = uri
            binding.storeLogo.setImageURI(uri)
        }

    private val pickCoverLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            newCoverUri = uri
            binding.storeCover.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SellerProfileViewModel::class.java]

        val prefs = getSharedPreferences("souq_prefs", MODE_PRIVATE)
        storeId = prefs.getInt("SELLER_ID", 0)
        Log.d("ACCOUNT_INFO", "SELLER_ID = $storeId")

        // Observers
        viewModel.getLiveError().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.getLiveSaving().observe(this) { saving ->
            binding.saveButton.isEnabled = !saving
        }

        viewModel.getLiveSellerProfile().observe(this) { sellerProfile ->
            binding.storeName.setText(sellerProfile.name)
            binding.storeDescription.setText(sellerProfile.store_description)

            Glide.with(this)
                .load(sellerProfile.store_logo_url)
                .placeholder(R.drawable.img_store)
                .into(binding.storeLogo)

            Glide.with(this)
                .load(sellerProfile.store_cover_url)
                .placeholder(R.drawable.store_cover_img)
                .into(binding.storeCover)

            // ✅ بعد ما يرجع البروفايل الجديد (بعد الحفظ) بنسكر التعديل ونصفر اليوريات
            newLogoUri = null
            newCoverUri = null
            setEditMode(false)
        }

        // load profile
        viewModel.getSellerProfile(storeId)

        binding.saveButton.setOnClickListener {
            if (!isEditMode) {
                setEditMode(true)
                return@setOnClickListener
            }

            val name = binding.storeName.text.toString().trim()
            val desc = binding.storeDescription.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "اسم المتجر مطلوب", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = UpdateSellerProfileRequest(
                name = name,
                store_description = desc
            )

            val logoPart = newLogoUri?.let { uri -> uriToPart(uri, "logo.png") }
            val coverPart = newCoverUri?.let { uri -> uriToPart(uri, "cover.png") }

            // ✅ هاي بس اللي بتناديها
            viewModel.saveAll(
                sellerId = storeId,
                request = request,
                logoPart = logoPart,
                coverPart = coverPart
            )
        }

        binding.back.setOnClickListener { finish() }
    }

    private fun uriToPart(uri: Uri, fileName: String): MultipartBody.Part? {
        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return null
        val body = RequestBody.create(MediaType.parse("image/*"), bytes)
        return MultipartBody.Part.createFormData("image", fileName, body)
    }

    private fun setEditMode(enabled: Boolean) {
        isEditMode = enabled

        binding.storeName.isFocusable = enabled
        binding.storeName.isFocusableInTouchMode = enabled

        binding.storeDescription.isFocusable = enabled
        binding.storeDescription.isFocusableInTouchMode = enabled

        if (enabled) {
            binding.storeLogo.setOnClickListener { pickLogoLauncher.launch("image/*") }
            binding.storeCover.setOnClickListener { pickCoverLauncher.launch("image/*") }
        } else {
            binding.storeLogo.setOnClickListener(null)
            binding.storeCover.setOnClickListener(null)
        }

        binding.saveButton.text = if (enabled) "حفظ" else "تعديل"
    }
}
