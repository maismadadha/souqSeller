package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.ImageUploadResponse
import com.example.souqseller.activities.pojo.SellerProfile
import com.example.souqseller.activities.pojo.UpdateSellerProfileRequest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfileViewModel : ViewModel() {

    private val sellerProfile = MutableLiveData<SellerProfile>()
    private val error = MutableLiveData<String>()
    private val saving = MutableLiveData<Boolean>()  // عشان تفعل/تعطل الزر بسهولة

    private var pending = 0
    private var done = 0
    private var currentSellerId: Int = 0

    fun getSellerProfile(sellerId: Int) {
        RetrofitInterface.api.getSellerProfile(sellerId)
            .enqueue(object : Callback<SellerProfile> {
                override fun onResponse(call: Call<SellerProfile?>, response: Response<SellerProfile?>) {
                    Log.d("SELLER_API", "code=${response.code()} body=${response.body()}")
                    if (response.isSuccessful && response.body() != null) {
                        sellerProfile.value = response.body()
                    } else {
                        error.value = "API error ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<SellerProfile?>, t: Throwable) {
                    Log.e("SELLER_API", t.message ?: "error")
                    error.value = t.message ?: "Network error"
                }
            })
    }

    /**
     * ✅ هاي الدالة الوحيدة اللي بدك تناديها من الاكتيفتي عند الحفظ
     */
    fun saveAll(
        sellerId: Int,
        request: UpdateSellerProfileRequest,
        logoPart: MultipartBody.Part?,
        coverPart: MultipartBody.Part?
    ) {
        currentSellerId = sellerId

        pending = 1 // update profile دايمًا
        done = 0
        if (logoPart != null) pending++
        if (coverPart != null) pending++

        saving.value = true

        // 1) update name/desc
        RetrofitInterface.api.updateSellerProfile(sellerId, request)
            .enqueue(object : Callback<SellerProfile> {
                override fun onResponse(call: Call<SellerProfile?>, response: Response<SellerProfile?>) {
                    if (response.isSuccessful) {
                        markDone()
                    } else {
                        saving.value = false
                        error.value = "فشل التحديث: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<SellerProfile?>, t: Throwable) {
                    saving.value = false
                    error.value = t.message ?: "Network error"
                }
            })

        // 2) upload logo (اختياري)
        logoPart?.let { part ->
            uploadLogoInternal(sellerId, part)
        }

        // 3) upload cover (اختياري)
        coverPart?.let { part ->
            uploadCoverInternal(sellerId, part)
        }
    }

    private fun uploadLogoInternal(sellerId: Int, part: MultipartBody.Part) {
        val idBody = RequestBody.create(MediaType.parse("text/plain"), sellerId.toString())

        RetrofitInterface.api.uploadStoreLogo(part, idBody)
            .enqueue(object : Callback<ImageUploadResponse> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful) {
                        Log.d("UPLOAD_LOGO", response.body()?.url ?: "")
                        markDone()
                    } else {
                        saving.value = false
                        error.value = "فشل رفع اللوجو: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    saving.value = false
                    error.value = t.message ?: "Network error"
                }
            })
    }

    private fun uploadCoverInternal(sellerId: Int, part: MultipartBody.Part) {
        val idBody = RequestBody.create(MediaType.parse("text/plain"), sellerId.toString())

        RetrofitInterface.api.uploadStoreCover(part, idBody)
            .enqueue(object : Callback<ImageUploadResponse> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful) {
                        Log.d("UPLOAD_COVER", response.body()?.url ?: "")
                        markDone()
                    } else {
                        saving.value = false
                        error.value = "فشل رفع الغلاف: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    saving.value = false
                    error.value = t.message ?: "Network error"
                }
            })
    }

    private fun markDone() {
        done++
        if (done >= pending) {
            // ✅ كل العمليات خلصت → جيب الداتا مرة وحدة
            getSellerProfile(currentSellerId)
            saving.value = false
        }
    }

    fun getLiveSellerProfile(): LiveData<SellerProfile> = sellerProfile
    fun getLiveError(): LiveData<String> = error
    fun getLiveSaving(): LiveData<Boolean> = saving
}
