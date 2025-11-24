package com.example.myapplicationlab6

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    @GET("/users/user")
    fun getUser(): Call<UserResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("/contacts")
    suspend fun getContacts(): List<Contact>

    @GET("/contacts/in_requests")
    suspend fun getIncomingRequests(): List<ContactRequest>

    @POST("/contacts/accept")
    suspend fun acceptRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @POST("/contacts/decline")
    suspend fun declineRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @GET("/users/search")
    suspend fun searchUsers(
        @Query("search") query: String,
        @Query("limit") limit: Int = 50
    ): List<Contact>

    // Отправка запроса на добавление
    @POST("/contacts/add")
    suspend fun sendContactRequest(
        @Body request: AcceptDeclineRequest
    ): Response<Unit>

    @Multipart
    @PATCH("/users/update/avatar")
    suspend fun updateAvatar(
        @Part file: MultipartBody.Part
    ): Response<String>

    // ====== PRODUCTS ======
    @GET("product")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("name") name: String? = null
    ): Response<ProductsResponse>

    // product comments
    @GET("products_comment/{shopId}/{productId}")
    suspend fun getProductComments(
        @Path("shopId") shopId: Int,
        @Path("productId") productId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<CommentsResponse>

    // Post a comment on a product (backend expects multipart/form-data in controller,
    // but server side accepts comment fields; keep it simple by sending JSON if possible)
    @POST("products_comment/register/{productId}")
    suspend fun postProductComment(
        @Path("productId") productId: Int,
        @Body body: Map<String, String>
    ): Response<Comment>

    // ====== CART ======
    @GET("cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("cart/add")
    suspend fun addToCart(@Body body: AddToCartRequest): Response<CartItem>

    @PATCH("cart/item/{itemId}")
    suspend fun updateCartItem(@Path("itemId") itemId: Int, @Body body: UpdateCartItemRequest): Response<CartItem>

    @DELETE("cart/item/{itemId}")
    suspend fun removeFromCart(@Path("itemId") itemId: Int): Response<Unit>

    // ====== ORDERS ======
    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): Response<Order>

    @GET("orders/my")
    suspend fun getMyOrders(@Query("page") page: Int = 1, @Query("limit") limit: Int = 20): Response<OrdersResponse>

    // ====== USER ======
    @GET("user/me")
    suspend fun getMe(): Response<UserResponse>

}