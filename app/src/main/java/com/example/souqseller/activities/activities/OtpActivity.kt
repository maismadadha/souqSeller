package com.example.souqseller.activities.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.souqseller.databinding.ActivityOtpBinding
import kotlin.toString

import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.souqseller.R

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sellerId = intent.getIntExtra("sellerId", 0)
        val prefs = getSharedPreferences("souq_prefs", MODE_PRIVATE)
        prefs.edit()
            .putInt("SELLER_ID", sellerId)
            .apply()

        binding.sendOtpButton.setOnClickListener {
            var otp1 = binding.otp1.text.toString()
            var otp2 = binding.otp2.text.toString()
            var otp3 = binding.otp3.text.toString()
            var otp4 = binding.otp4.text.toString()

            val otp = otp1 + otp2 + otp3 + otp4
            if (otp == "")
                showCustomToast("يرجى ادخال رمز التحقق أولا")
            else if (otp == "1234") {
                showCustomToast("تم تسجيل الدخول بنجاح")
                var intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("userId", sellerId)
                startActivity(intent)
                finish()
            } else
                showCustomToast("رمز التحقق غير صحيح ,جرب مرة اخرى")

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


