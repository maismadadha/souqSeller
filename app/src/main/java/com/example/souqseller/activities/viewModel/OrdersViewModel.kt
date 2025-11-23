package com.example.souqseller.activities.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.souqcustomer.retrofit.RetrofitInterface
import com.example.souqseller.activities.pojo.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {

    private val orders = MutableLiveData<List<OrderResponse>>()
    private val errorLiveData = MutableLiveData<String>()
    private val orderDetailsLiveData = MutableLiveData<OrderResponse>()


    fun getOrdersByStatus(seseionId: Int, status: String) {
        RetrofitInterface.api.getStoreOrders(seseionId, status).enqueue(object :
            Callback<List<OrderResponse>> {
            override fun onResponse(
                call: Call<List<OrderResponse>?>,
                response: Response<List<OrderResponse>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    orders.value = response.body()
                } else {
                    val msg = when (response.code()) {
                        404 -> "لا يوجد طلبات"
                        else -> "فشل في جلب الطلبات"
                    }
                    errorLiveData.value = msg
                    Log.e("OrdersVM", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<List<OrderResponse>?>,
                t: Throwable
            ) {
                val msg = t.message ?: "مشكلة في الاتصال"
                errorLiveData.value = msg
                Log.e("OrdersVM", "Failure: $msg")

            }
        })
    }
    fun getOrderDetails(orderId: Int) {
        RetrofitInterface.api.getOrderDetails(orderId).enqueue(object :
            Callback<OrderResponse> {
            override fun onResponse(
                call: Call<OrderResponse?>,
                response: Response<OrderResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    orderDetailsLiveData.value = response.body()
                }else {
                    errorLiveData.value = "فشل في جلب تفاصيل الطلب"
                    Log.e("OrdersVM", "Details error code: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<OrderResponse?>,
                t: Throwable
            ) {
                errorLiveData.value = t.message ?: "مشكلة في الاتصال"
                Log.e("OrdersVM", "Details failure: ${t.message}")
            }
        })
    }

    fun observeOrdersLiveData(): LiveData<List<OrderResponse>> {
        return orders
    }
    fun observeOrderDetailsLiveData(): LiveData<OrderResponse> {
        return orderDetailsLiveData
    }
    fun observeErrorLiveData(): LiveData<String> = errorLiveData


}