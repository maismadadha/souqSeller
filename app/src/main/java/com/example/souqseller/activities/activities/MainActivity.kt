package com.example.souqseller.activities.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.souqseller.R
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.souqseller.activities.viewModel.SignUpViewModel
import com.example.souqseller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SignUpViewModel
    private var selectedCategoryId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        viewModel.getMainCategories()
        observeMainCategoriesLiveData()
        observeSignUpLiveData()
        observeErrorLiveData()


        // زر تأكيد التسجيل
        binding.confirmButton.setOnClickListener {
            val storeName = binding.storeName.text.toString().trim()
            val storeDescription = binding.storeDescription.text.toString().trim()
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.storePassword.text.toString().trim()
            val email: String? = null

            if (storeName.isEmpty() || storeDescription.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()
            ) {
                showCustomToast("يرجى تعبئة جميع الحقول")
                return@setOnClickListener
            }

            if (selectedCategoryId == null) {
                showCustomToast("يرجى اختيار فئة المتجر")
                return@setOnClickListener
            }

            viewModel.signUpSeller(
                phone = phoneNumber,
                email = email,
                name = storeName,
                storeDescription = storeDescription,
                mainCategoryId = selectedCategoryId!!,
                password = password,
                logoUrl = null,
                coverUrl = null
            )

        }

        binding.doYouHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }// onCreate

    private fun observeMainCategoriesLiveData() {
        viewModel.observeMainCategoriesLiveData().observe(this) { mainCategories ->
            if (mainCategories != null) {
                val namesList = mainCategories.map { it.name }
                val etCategory = binding.etCategory
                val adapter = object : ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    namesList
                ) {
                    override fun getDropDownView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        val v = super.getDropDownView(position, convertView, parent) as TextView
                        val color = ContextCompat.getColor(
                            this@MainActivity,
                            com.example.souqseller.R.color.black
                        )
                        v.setTextColor(color)
                        return v
                    }
                }

                etCategory.setAdapter(adapter)
                etCategory.setDropDownBackgroundResource(com.example.souqseller.R.drawable.dropdown_bg)
                etCategory.setOnClickListener { etCategory.showDropDown() }


                etCategory.setOnItemClickListener { _, _, position, _ ->
                    val selectedCategory = mainCategories[position]
                     selectedCategoryId = selectedCategory.id
                    val selectedCategoryName = selectedCategory.name
                }

            }

        }
    }//obsarveMainCategories

    private fun observeSignUpLiveData() {
        viewModel.observeSignUpLiveData().observe(this) { response ->
            val sellerId=response.user.id

            if(sellerId==0) {
                showCustomToast("صار خطأ: ما وصل sellerId من السيرفر")
                return@observe
            }
            showCustomToast("يرجى ادخال رمز التحقق لإنشاء الحساب")
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("sellerId", sellerId)
            startActivity(intent)
            finish()
        }

    }

    private fun observeErrorLiveData() {
        viewModel.observeErrorLiveData().observe(this) { errorMsg ->
            showCustomToast(errorMsg)
        }
    }


    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val tvMessage = layout.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 160)
        toast.show()
    }
}// MainActivity


