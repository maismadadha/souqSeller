package com.example.souqseller.activities.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    lateinit var appContext: Context


    private val product = MutableLiveData<Product>()
    private val images = MutableLiveData<ProductImages>()
    private val options = MutableLiveData<ProductOptions>()
    private val error = MutableLiveData<String>()
    private val newProduct = MutableLiveData<CreateProductRespons>()
    private val newOption = MutableLiveData<CreateProductOptionsRespons>()
    private val newOptionValue = MutableLiveData<OptionValueresponse>()
    private val deletedProductId = MutableLiveData<Int>()


    fun getProductById(id: Int) {
        RetrofitInterface.api.getProductById(id)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, res: Response<Product>) {
                    if (res.isSuccessful) product.value = res.body()
                    else error.value = "خطأ في جلب المنتج"
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    error.value = t.message
                }
            })
    }
    fun getProductImages(id: Int) {
        RetrofitInterface.api.getProductImages(id)
            .enqueue(object : Callback<ProductImages> {
                override fun onResponse(call: Call<ProductImages>, res: Response<ProductImages>) {
                    if (res.isSuccessful) images.value = res.body()
                }

                override fun onFailure(call: Call<ProductImages>, t: Throwable) {
                    Log.e("getImages", t.message ?: "")
                }
            })
    }
    fun getProductOptions(id: Int) {
        RetrofitInterface.api.getProductOptions(id)
            .enqueue(object : Callback<ProductOptions> {
                override fun onResponse(call: Call<ProductOptions>, res: Response<ProductOptions>) {
                    if (res.isSuccessful) options.value = res.body()
                }

                override fun onFailure(call: Call<ProductOptions>, t: Throwable) {
                    error.value = t.message
                }
            })
    }
    fun createProduct(request: ProductCreateRequest) {
        Log.d("CREATE_PRODUCT", request.toString())
        RetrofitInterface.api.createProduct(request)
            .enqueue(object : Callback<CreateProductRespons> {
                override fun onResponse(
                    call: Call<CreateProductRespons?>,
                    response: Response<CreateProductRespons?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        newProduct.value = response.body()
                        Log.d("CREATE_PRODUCT00", response.toString())
                    } else {
                        Log.d("CREATE_PRODUCT0", response.toString())
                        error.value =
                            "Create product failed: ${response.code()} ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(
                    call: Call<CreateProductRespons?>,
                    t: Throwable
                ) {
                    error.value = t.message
                }
            })
    }
    fun createProductOption(productId: Int, request: CreateProductOptionRequest) {
        RetrofitInterface.api.addProductOption(productId, request)
            .enqueue(object : Callback<CreateProductOptionsRespons> {
                override fun onResponse(
                    call: Call<CreateProductOptionsRespons?>,
                    response: Response<CreateProductOptionsRespons?>
                ) {
                    if (response.isSuccessful) {
                        newOption.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<CreateProductOptionsRespons?>,
                    t: Throwable
                ) {
                    error.value = t.message

                }

            })
    }
    fun createOptionValue(optionId: Int, request: CreateOptionValueRequest) {
        Log.d("CREATE_VALUE", request.toString())
        RetrofitInterface.api.addOptionValue(optionId, request)
            .enqueue(object : Callback<OptionValueresponse> {
                override fun onResponse(
                    call: Call<OptionValueresponse?>,
                    response: Response<OptionValueresponse?>
                ) {
                    if (response.isSuccessful) {
                        newOptionValue.value = response.body()
                        Log.d("CREATE_VALUE0", response.toString())
                    }
                    else
                        Log.d("CREATE_VALUE1", response.toString())
                }

                override fun onFailure(
                    call: Call<OptionValueresponse?>,
                    t: Throwable
                ) {
                    error.value = t.message
                }

            })
    }
    fun uploadImage(productId: Int, part: MultipartBody.Part) {
        val productIdBody = RequestBody.create(
            MediaType.parse("text/plain"),
            productId.toString()
        )

        RetrofitInterface.api.uploadImage(part, productIdBody)
            .enqueue(object : Callback<ImageUploadResponse> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.url
                        Log.d("UPLOAD", "Uploaded to: $imageUrl")
                    } else {
                        Log.e("UPLOAD", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(
                    call: Call<ImageUploadResponse?>,
                    t: Throwable
                ) {
                    Log.e("UPLOAD", "Error: ${t.message}")
                }
            })
    }
    fun deleteProduct(productId: Int) {
        RetrofitInterface.api.deleteProduct(productId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        deletedProductId.value = productId
                    } else {
                        error.value = "فشل حذف المنتج"
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    error.value = t.message
                }
            })
    }




    fun getLiveProductById(): LiveData<Product> = product
    fun getLiveImages(): LiveData<ProductImages> = images
    fun getLiveOptions(): LiveData<ProductOptions> = options
    fun getLiveNewProduct(): LiveData<CreateProductRespons> = newProduct
    fun getLiveNewOption(): LiveData<CreateProductOptionsRespons> = newOption
    fun getLiveNewOptionValue(): LiveData<OptionValueresponse> = newOptionValue
    fun getLiveError(): LiveData<String> = error
    fun getLiveDeletedProductId(): LiveData<Int> = deletedProductId


}
