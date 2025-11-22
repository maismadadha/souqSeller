package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.CreateSellerRequest
import com.example.souqseller.activities.pojo.CreateSellerResponse
import com.example.souqseller.activities.pojo.LoginRequest
import com.example.souqseller.activities.pojo.LoginResponse
import com.example.souqseller.activities.pojo.MainCategories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val mainCategories = MutableLiveData<MainCategories>()
    private val signupLiveData = MutableLiveData<CreateSellerResponse>()
    private val loginLiveData = MutableLiveData<LoginResponse>()
    private val errorLiveData = MutableLiveData<String>()

    fun getMainCategories() {
        RetrofitInterface.api.getMainCategories().enqueue(object : Callback<MainCategories> {
            override fun onResponse(
                call: Call<MainCategories?>,
                response: Response<MainCategories?>
            ) {
                if (response.isSuccessful) {
                    mainCategories.value = response.body()
                }
            }

            override fun onFailure(
                call: Call<MainCategories?>,
                t: Throwable
            ) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }
    fun signUpSeller(
        phone: String,
        email: String?,
        name: String,
        storeDescription: String,
        mainCategoryId: Int,
        password: String,
        logoUrl: String? = null,
        coverUrl: String? = null
    ) {
        val body = CreateSellerRequest(
            phone = phone,
            email = email,
            name = name,
            store_description = storeDescription,
            main_category_id = mainCategoryId,
            password = password,
            store_logo_url = logoUrl,
            store_cover_url = coverUrl
        )

        RetrofitInterface.api.createUser(body).enqueue(object : Callback<CreateSellerResponse> {
            override fun onResponse(
                call: Call<CreateSellerResponse?>,
                response: Response<CreateSellerResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    signupLiveData.value = response.body()
                    Log.d("SellerSignUpVM", "Success: ${response.body()}")
                } else {
                    val msg = when (response.code()) {
                        409 -> "الرقم أو الإيميل مستخدم بالفعل"
                        422 -> "البيانات المدخلة غير صحيحة"
                        500 -> "مشكلة في الخادم، جرّب بعد شوي"
                        else -> "صار خطأ غير متوقع"
                    }
                    errorLiveData.value = msg
                }
            }

            override fun onFailure(
                call: Call<CreateSellerResponse?>,
                t: Throwable
            ) {
                val msg = t.message ?: "مشكلة في الاتصال"
                errorLiveData.value = msg
                Log.e("SellerSignUpVM", "Failure: $msg")
            }
        })
    }

    fun loginSeller(
        phone: String,
        password: String
    ){
        val body = LoginRequest(
            phone = phone,
            password = password
        )
        RetrofitInterface.api.loginUser(body).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    loginLiveData.value = response.body()
                } else {
                    val msg = when (response.code()) {
                        401 -> "رقم الهاتف أو كلمة السر غير صحيحة"
                        422 -> "البيانات المدخلة غير صحيحة"
                        else -> "فشل تسجيل الدخول"
                    }
                    errorLiveData.value = msg
            }
            }

            override fun onFailure(
                call: Call<LoginResponse?>,
                t: Throwable
            ) {
                val msg = t.message ?: "مشكلة في الاتصال"
                errorLiveData.value = msg
                 Log.e("SellerLoginVM", "Failure: $msg")
            }
        })



    }

    fun observeMainCategoriesLiveData(): LiveData<MainCategories> {
        return mainCategories
    }

    fun observeSignUpLiveData(): LiveData<CreateSellerResponse> {
        return signupLiveData
    }
    fun observeLoginLiveData(): LiveData<LoginResponse> {
        return loginLiveData
    }

    fun observeErrorLiveData(): LiveData<String> {
        return errorLiveData
    }




}