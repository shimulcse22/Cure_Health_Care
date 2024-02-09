package com.devshawon.curehealthcare.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "password")
    val password: String,
    @Json(name = "phone")
    val phone: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "customer")
    val customer: Customer,
    @Json(name = "email")
    val email: String?="",
    @Json(name = "email_verified_at")
    val emailVerifiedAt: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "phone")
    val phone: String?="",
    @Json(name = "token")
    val token: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class Customer(
    @Json(name = "area_id")
    val areaId: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "first_name")
    val firstName: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "last_name")
    val lastName: String?="",
    @Json(name = "license")
    val license: String?="",
    @Json(name = "media")
    val media: List<String>?= emptyList(),
    @Json(name = "nid")
    val nid: String?="",
    @Json(name = "photo")
    val photo: String?="",
    @Json(name = "previous_order_date")
    val previousOrderDate: String?="",
    @Json(name = "register_by_id")
    val registerById: String?="",
    @Json(name = "shop_address")
    val shopAddress: String?="",
    @Json(name = "shop_name")
    val shopName: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?="",
    @Json(name = "user_id")
    val userId: String?="",
    @Json(name = "zone_id")
    val zoneId: String?=""
)



data class RegistrationRequest(
    @Json(name = "address")
    val address: String,
    @Json(name = "fname")
    val fname: String,
    @Json(name = "license")
    val license: String,
    @Json(name = "lname")
    val lname: String,
    @Json(name = "mobile")
    val mobile: String,
    @Json(name = "nid")
    val nid: String,
    @Json(name = "shop")
    val shop: String
)

@JsonClass(generateAdapter = true)
data class RegistrationResponse(
    @Json(name = "message")
    val message: String?=""
)

@JsonClass(generateAdapter = true)
data class ForgotPasswordRequest(
    @Json(name = "phone")
    val phone: String?=""
)


object EmptyRequest {
    val INSTANCE: EmptyRequest = EmptyRequest
}