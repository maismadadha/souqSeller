package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.Product
import com.example.souqseller.activities.pojo.Products
import com.example.souqseller.activities.pojo.StoreCategories
import com.example.souqseller.activities.pojo.StoreCategoriesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreManagementViewModel : ViewModel() {
    private val storeCategories = MutableLiveData<StoreCategories>()
    private val products = MutableLiveData<List<Product>>()
    private val categoryAdded = MutableLiveData<Boolean>()
    private val categoryProducts = MutableLiveData<Products>()




    fun getStoreCategories(id: Int) {
        RetrofitInterface.api.getStoreCategories(id).enqueue(object : Callback<StoreCategories> {
            override fun onResponse(
                call: Call<StoreCategories?>,
                response: Response<StoreCategories?>
            ) {
                if (response.isSuccessful && response.body() != null)
                    storeCategories.value = response.body()

            }

            override fun onFailure(
                call: Call<StoreCategories?>,
                t: Throwable
            ) {
                Log.e("SellerCategories", t.message.toString())
            }
        })
    }

    fun getCategoryProducts(categoryId: Int) {
        RetrofitInterface.api.getStoreCategoryProducts(categoryId)
            .enqueue(object : Callback<Products> {
                override fun onResponse(call: Call<Products>, response: Response<Products>) {
                    if (response.isSuccessful)
                        categoryProducts.value = response.body()
                }

                override fun onFailure(call: Call<Products>, t: Throwable) {
                    categoryProducts.value = Products()
                }
            })
    }




    fun addStoreCategory(storeId: Int, name: String) {
        RetrofitInterface.api.addStoreCategory(storeId, name)
            .enqueue(object : Callback<StoreCategoriesItem> {
                override fun onResponse(
                    call: Call<StoreCategoriesItem>,
                    response: Response<StoreCategoriesItem>
                ) {
                    if (response.isSuccessful) {
                        categoryAdded.value = true
                        getStoreCategories(storeId)
                    }
                }

                override fun onFailure(call: Call<StoreCategoriesItem>, t: Throwable) {
                    categoryAdded.value = false
                }
            })
    }




    fun getStoreCategoriesLiveData(): LiveData<StoreCategories> {
        return storeCategories
    }


    fun getProductsLiveData(): LiveData<List<Product>> {
        return products
    }
    fun getCategoryAddedLiveData(): LiveData<Boolean> = categoryAdded

    fun getLiveCategoryProducts(): LiveData<Products> = categoryProducts


}