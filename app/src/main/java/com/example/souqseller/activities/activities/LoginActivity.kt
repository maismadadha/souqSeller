package com.example.souqseller.activities.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.souqseller.R
import com.example.souqseller.activities.viewModel.SignUpViewModel
import com.example.souqseller.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: SignUpViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        observeLoginLiveData()
        observeErrorLoginLiveData()

        binding.confirmButton.setOnClickListener {
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.storePassword.text.toString().trim()
            if (phoneNumber.isEmpty() || password.isEmpty()) {
                showCustomToast("يرجى تعبئة جميع الحقول")
                return@setOnClickListener
            }
            viewModel.loginSeller(phoneNumber, password)

        }

        binding.createNewAccount.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun observeErrorLoginLiveData() {
        viewModel.observeErrorLiveData().observe(this) { errorMsg ->
            showCustomToast(errorMsg)
        }
    }

    private fun observeLoginLiveData() {
        viewModel.observeLoginLiveData().observe(this){response ->
            val sellerId = response.user.id

            if (sellerId == 0) {
                showCustomToast("صار خطأ: ما وصل userId من السيرفر")
                return@observe
            }
            showCustomToast("يرجى ادخال رمز التحقق لتسحيل الدخول")
            var intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("sellerId", sellerId)
            startActivity(intent)
            finish()
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
}


