package com.example.souqcustomer.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInterface {
    private const val URL = "http://127.0.0.1:8000/api/"
    //private const val URL = "http://192.168.1.20:8000/api/";
    //    private const val URL = "http://10.0.2.2:8000/api/";
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(souqApi::class.java)

}