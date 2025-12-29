package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.CreateSliderAdResponse
import com.example.souqseller.activities.pojo.SliderAd
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SliderAdViewModel : ViewModel() {

    private val createAdResponse = MutableLiveData<CreateSliderAdResponse>()
    private val error = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()
    private val sliderAds = MutableLiveData<List<SliderAd>>()


    fun createSliderAd(
        image: MultipartBody.Part?,
        imageUrl: RequestBody?,
        storeId: RequestBody,
        title: RequestBody?,
        description: RequestBody?,
        startDate: RequestBody,
        endDate: RequestBody
    ) {

        loading.value = true

        RetrofitInterface.api.createSliderAd(
            image,
            imageUrl,
            storeId,
            title,
            description,
            startDate,
            endDate
        ).enqueue(object : Callback<CreateSliderAdResponse> {

            override fun onResponse(
                call: Call<CreateSliderAdResponse>,
                response: Response<CreateSliderAdResponse>
            ) {
                loading.value = false

                if (response.isSuccessful && response.body() != null) {
                    createAdResponse.value = response.body()
                    Log.d("CREATE_AD", "Ad created successfully")
                } else {
                    error.value =
                        "فشل إنشاء الإعلان: ${response.code()}"
                    Log.e("CREATE_AD", response.errorBody()?.string() ?: "")
                }
            }

            override fun onFailure(
                call: Call<CreateSliderAdResponse>,
                t: Throwable
            ) {
                loading.value = false
                error.value = t.message ?: "خطأ في الاتصال"
                Log.e("CREATE_AD", t.message ?: "")
            }
        })
    }

    fun getSliderAds(storeId: Int) {

        loading.value = true

        RetrofitInterface.api.getSliderAds(storeId)
            .enqueue(object : Callback<List<SliderAd>> {

                override fun onResponse(
                    call: Call<List<SliderAd>>,
                    response: Response<List<SliderAd>>
                ) {
                    loading.value = false

                    if (response.isSuccessful && response.body() != null) {
                        sliderAds.value = response.body()
                    } else {
                        error.value = "فشل تحميل الإعلانات"
                    }
                }

                override fun onFailure(
                    call: Call<List<SliderAd>>,
                    t: Throwable
                ) {
                    loading.value = false
                    error.value = t.message ?: "خطأ في الاتصال"
                }
            })
    }


    fun getCreateAdResponse(): LiveData<CreateSliderAdResponse> = createAdResponse
    fun getError(): LiveData<String> = error
    fun getLoading(): LiveData<Boolean> = loading
    fun getSliderAdsLive(): LiveData<List<SliderAd>> = sliderAds
}
