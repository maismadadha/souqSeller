package com.example.souqseller.activities.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.souqseller.activities.viewModel.SliderAdViewModel
import com.example.souqseller.databinding.ActivityAddNewAdsBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddNewAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewAdsBinding
    private lateinit var viewModel: SliderAdViewModel

    private var storeId = 0
    private var adImageUri: Uri? = null


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            adImageUri = uri
            binding.AdsCover.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddNewAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("souq_prefs", MODE_PRIVATE)
        storeId = prefs.getInt("SELLER_ID", 0)

        viewModel = ViewModelProvider(this)[SliderAdViewModel::class.java]


        binding.back.setOnClickListener { finish() }


        binding.AdsCover.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        observeViewModel()


        binding.saveButton.setOnClickListener {
            submitAd()
        }
    }


    private fun submitAd() {

        if (storeId == 0) {
            Toast.makeText(this, "خطأ في بيانات المتجر", Toast.LENGTH_SHORT).show()
            return
        }

        val title = binding.AdsTitle.text.toString().trim()
        val description = binding.AdsDescription.text.toString().trim()


        val enteredCode =
            binding.otp1.text.toString() +
                    binding.otp2.text.toString() +
                    binding.otp3.text.toString() +
                    binding.otp4.text.toString()

        if (enteredCode != "1234") {
            Toast.makeText(this, "كود التفعيل غير صحيح", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "عنوان الإعلان مطلوب", Toast.LENGTH_SHORT).show()
            return
        }

        if (adImageUri == null) {
            Toast.makeText(this, "يرجى اختيار صورة الإعلان", Toast.LENGTH_SHORT).show()
            return
        }


        val storeIdBody = RequestBody.create(
            MediaType.parse("text/plain"),
            storeId.toString()
        )

        val titleBody = RequestBody.create(
            MediaType.parse("text/plain"),
            title
        )

        val descriptionBody = RequestBody.create(
            MediaType.parse("text/plain"),
            description
        )

        val startDateBody = RequestBody.create(
            MediaType.parse("text/plain"),
            "2025-01-01"
        )

        val endDateBody = RequestBody.create(
            MediaType.parse("text/plain"),
            "2030-02-01"
        )

        val imagePart = uriToPart(adImageUri!!, "ad_image.jpg")

        viewModel.createSliderAd(
            image = imagePart,
            imageUrl = null,
            storeId = storeIdBody,
            title = titleBody,
            description = descriptionBody,
            startDate = startDateBody,
            endDate = endDateBody
        )
    }


    private fun observeViewModel() {

        viewModel.getLoading().observe(this) { loading ->
            binding.saveButton.isEnabled = !loading
            binding.saveButton.text = if (loading) "جارٍ الحفظ..." else "حفظ"
        }

        viewModel.getCreateAdResponse().observe(this) {
            Toast.makeText(this, "تم إنشاء الإعلان بنجاح ✅", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.getError().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }


    private fun uriToPart(uri: Uri, fileName: String): MultipartBody.Part? {
        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return null
        val body = RequestBody.create(MediaType.parse("image/*"), bytes)
        return MultipartBody.Part.createFormData("image", fileName, body)
    }
}
