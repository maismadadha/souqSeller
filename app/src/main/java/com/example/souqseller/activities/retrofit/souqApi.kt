package com.example.souqcustomer.retrofit

import com.example.souqseller.activities.pojo.CreateOptionValueRequest
import com.example.souqseller.activities.pojo.CreateProductOptionRequest
import com.example.souqseller.activities.pojo.CreateProductOptionsRespons
import com.example.souqseller.activities.pojo.CreateProductRespons
import com.example.souqseller.activities.pojo.CreateSellerRequest
import com.example.souqseller.activities.pojo.CreateSellerResponse
import com.example.souqseller.activities.pojo.ImageUploadResponse
import com.example.souqseller.activities.pojo.LoginRequest
import com.example.souqseller.activities.pojo.LoginResponse
import com.example.souqseller.activities.pojo.MainCategories
import com.example.souqseller.activities.pojo.OptionValueresponse
import com.example.souqseller.activities.pojo.OrderResponse
import com.example.souqseller.activities.pojo.Product
import com.example.souqseller.activities.pojo.ProductCreateRequest
import com.example.souqseller.activities.pojo.ProductImages
import com.example.souqseller.activities.pojo.ProductImagesItem
import com.example.souqseller.activities.pojo.ProductOptions
import com.example.souqseller.activities.pojo.ProductOptionsItem
import com.example.souqseller.activities.pojo.Products
import com.example.souqseller.activities.pojo.SellerProfile
import com.example.souqseller.activities.pojo.StoreCategories
import com.example.souqseller.activities.pojo.StoreCategoriesItem
import com.example.souqseller.activities.pojo.UpdateOrderStatusRequest
import com.example.souqseller.activities.pojo.UpdateSellerProfileRequest
import com.example.souqseller.activities.pojo.Value
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface souqApi {

    @GET("categories")
    fun getMainCategories(): Call<MainCategories>

    @POST("users")
    fun createUser(
        @Body request: CreateSellerRequest
    ): Call<CreateSellerResponse>

    @POST("seller/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("orders/store")
    fun getStoreOrders(
        @Query("store_id") storeId: Int,
        @Query("status") status: String?
    ): Call<List<OrderResponse>>


    @GET("order/details")
    fun getOrderDetails(
        @Query("order_id") orderId: Int
    ): Call<OrderResponse>

    @POST("orders/{orderId}/seller-status")
    fun updateOrderStatus(
        @Path("orderId") orderId: Int,
        @Body body: UpdateOrderStatusRequest
    ): Call<OrderResponse>

    @GET("stores/{id}/categories")
    fun getStoreCategories(@Path("id") id: Int
    ): Call<StoreCategories>

    @GET("store-categories/{id}/products")
    fun getStoreCategoryProducts(
        @Path("id") categoryId: Int
    ): Call<Products>

    @FormUrlEncoded
    @POST("store-categories")
    fun addStoreCategory(
        @Field("store_id") storeId: Int,
        @Field("name") name: String
    ): Call<StoreCategoriesItem>


    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>

    @GET("products/{id}/images")
    fun getProductImages(@Path("id") id: Int): Call<ProductImages>

    @GET("products/{id}/options")
    fun getProductOptions(@Path("id") id: Int): Call<ProductOptions>

    @POST("products/{productId}/images")
    fun addProductImage(
        @Path("productId") productId: Int,
        @Body body: Map<String, String>
    ): Call<ProductImagesItem>

    @POST("seller/products")
    fun createProduct(
        @Body body: ProductCreateRequest
    ): Call<CreateProductRespons>

    @POST("products/{productId}/options")
    fun addProductOption(
        @Path("productId") productId: Int,
        @Body body: CreateProductOptionRequest
    ): Call<CreateProductOptionsRespons>

    @POST("options/{optionId}/values")
    fun addOptionValue(
        @Path("optionId") optionId: Int,
        @Body body: CreateOptionValueRequest
    ): Call<OptionValueresponse>

    @Multipart
    @POST("upload-image")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("product_id") productId: RequestBody
    ): Call<ImageUploadResponse>

    @DELETE("seller/products/{id}")
    fun deleteProduct(
        @Path("id") productId: Int
    ): Call<Void>

    @GET("seller-profiles/{id}")
    fun getSellerProfile(
        @Path("id") sellerId: Int
    ): Call<SellerProfile>

    @PATCH("seller-profiles/{id}")
    fun updateSellerProfile(
        @Path("id") sellerId: Int,
        @Body body: UpdateSellerProfileRequest
    ): Call<SellerProfile>


    @Multipart
    @POST("seller/upload-logo")
    fun uploadStoreLogo(
        @Part image: MultipartBody.Part,
        @Part("seller_id") sellerId: RequestBody
    ): Call<ImageUploadResponse>

    @Multipart
    @POST("seller/upload-cover")
    fun uploadStoreCover(
        @Part image: MultipartBody.Part,
        @Part("seller_id") sellerId: RequestBody
    ): Call<ImageUploadResponse>





}