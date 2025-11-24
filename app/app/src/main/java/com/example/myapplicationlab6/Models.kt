package com.example.myapplicationlab6

data class LoginResponse(
    val access_token: String // JWT-токен, возвращаемый сервером
)

data class UserResponse(
    val id: String,
    val name: String,
    val avatar: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String
)

data class UserRegisterResponse(
    val id: Int,
    val permission: Int,
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val password: String,
    val face: String,
    val date_created: String,
    val date_updated: String
)

data class RegisterResponse(
    val message: String,
    val user: UserRegisterResponse
)

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class ContactRequest(
    val requestId: String,
    val userId: String,
    val name: String,
    val login: String,
    val avatar: String?,
    val status: String
)

data class AcceptDeclineRequest(
    val requestId: String
)

// ===== Product / comments / cart / orders models =====
data class Product(
    val id: Int,
    val name: String?,
    val image: String?,
    val amount: Double?,
    val categoryId: Int?
)

data class ProductsResponse(
    val total: Int,
    val page: Int,
    val limit: Int,
    val data: List<Product>
)

data class Comment(
    val id: Int,
    val name: String?,
    val description: String?,
    val about: String?,
    val image: String?,
    val productId: Int?,
    val userId: Int?,
    val date_created: String?
)

data class CommentsResponse(
    val total: Int,
    val page: Int,
    val limit: Int,
    val data: List<Comment>
)

data class CartItem(
    val id: Int?,
    val product: Product?,
    val quantity: Int?
)

data class CartResponse(
    val id: Int?,
    val items: List<CartItem>?,
    val totalItems: Int?,
    val totalPrice: Double?
)

data class AddToCartRequest(
    val productId: Int,
    val quantity: Int
)

data class UpdateCartItemRequest(
    val quantity: Int
)

data class CreateOrderRequest(
    val address: String,
    val date_delivery: String,
    val total_service: String,
    val items: List<CartItemRequest>
)

data class CartItemRequest(
    val productId: Int,
    val quantity: Int
)

data class Order(
    val id: Int?,
    val status: String?,
    val date: String?,
    val total_service: String?
)

data class OrdersResponse(
    val total: Int,
    val page: Int,
    val limit: Int,
    val data: List<Order>
)
