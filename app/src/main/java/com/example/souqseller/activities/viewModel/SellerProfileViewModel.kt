package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.AddressDto
import com.example.souqseller.activities.pojo.ImageUploadResponse
import com.example.souqseller.activities.pojo.SellerProfile
import com.example.souqseller.activities.pojo.StoreCategories
import com.example.souqseller.activities.pojo.StoreCategoriesItem
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
    private val saving = MutableLiveData<Boolean>()
    private val addressesLive = MutableLiveData<List<AddressDto>>()
    private val storeCategoriesLive = MutableLiveData<List<StoreCategoriesItem>>()

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

    fun saveAll(
        sellerId: Int,
        request: UpdateSellerProfileRequest,
        logoPart: MultipartBody.Part?,
        coverPart: MultipartBody.Part?
    ) {
        currentSellerId = sellerId

        pending = 1
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

    fun getUserAddresses(userId: Int) {
        RetrofitInterface.api.getUserAddresses(userId)
            .enqueue(object : Callback<List<AddressDto>> {
                override fun onResponse(
                    call: Call<List<AddressDto>>,
                    response: Response<List<AddressDto>>
                ) {
                    if (response.isSuccessful) {
                        addressesLive.value = response.body() ?: emptyList()
                        Log.d("ADDRESSES", response.body().toString())
                    } else {
                        addressesLive.value = emptyList()
                        error.value = "فشل تحميل العناوين"
                        Log.d("ADDRESSES0", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<List<AddressDto>>, t: Throwable) {
                    addressesLive.value = emptyList()
                    error.value = t.message
                    Log.d("ADDRESSES1", t.message.toString())
                }
            })
    }
    fun setDefaultAddress(userId: Int, addressId: Int) {
        RetrofitInterface.api.setDefaultAddress(userId, addressId)
            .enqueue(object : Callback<AddressDto> {
                override fun onResponse(
                    call: Call<AddressDto>,
                    response: Response<AddressDto>
                ) {
                    if (response.isSuccessful) {
                        getUserAddresses(userId)
                    }
                }

                override fun onFailure(call: Call<AddressDto>, t: Throwable) {
                    error.value = t.message
                }
            })
    }

    fun deleteAddress(userId: Int, addressId: Int) {
        RetrofitInterface.api.deleteAddress(userId, addressId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        getUserAddresses(userId) // 🔥 تحديث القائمة
                    } else {
                        error.value = "فشل حذف العنوان"
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    error.value = t.message
                }
            })
    }

    fun getStoreCategories(storeId: Int) {
        RetrofitInterface.api.getStoreCategories(storeId)
            .enqueue(object : Callback<StoreCategories> {
                override fun onResponse(
                    call: Call<StoreCategories>,
                    response: Response<StoreCategories>
                ) {
                    if (response.isSuccessful) {
                        storeCategoriesLive.value = response.body() ?: emptyList()
                    } else {
                        storeCategoriesLive.value = emptyList()
                    }
                }

                override fun onFailure(call: Call<StoreCategories>, t: Throwable) {
                    storeCategoriesLive.value = emptyList()
                }
            })
    }

    fun deleteStoreCategory(categoryId: Int, storeId: Int) {
        RetrofitInterface.api.deleteStoreCategory(categoryId)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        // 🔄 نعيد تحميل الفئات
                        getStoreCategories(storeId)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {}
            })
    }


    fun getLiveSellerProfile(): LiveData<SellerProfile> = sellerProfile
    fun getLiveError(): LiveData<String> = error
    fun getLiveSaving(): LiveData<Boolean> = saving
    fun observeAddresses(): LiveData<List<AddressDto>> = addressesLive
    fun observeStoreCategories(): LiveData<List<StoreCategoriesItem>> =
        storeCategoriesLive
}
