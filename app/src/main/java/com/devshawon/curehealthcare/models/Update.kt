package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class UpdatePassword(
    @Json(name = "newPassword")
    val newPassword: String,
    @Json(name = "newPassword_confirmation")
    val newPasswordConfirmation: String,
    @Json(name = "oldPassword")
    val oldPassword: String
)

@JsonClass(generateAdapter = true)
data class UpdatePasswordResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: String
)

@JsonClass(generateAdapter = true)
data class EditProfileGetRequest(
    @Json(name = "data")
    val `data`: EditProfileData?
)

@JsonClass(generateAdapter = true)
data class EditProfileData(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "customer")
    val customer: CustomerEdit?,
    @Json(name = "email")
    val email: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "phone")
    val phone: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class CustomerEdit(
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
    @Json(name = "nid")
    val nid: String?="",
    @Json(name = "shop_address")
    val shopAddress: String?="",
    @Json(name = "shop_name")
    val shopName: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?="",
    @Json(name = "user_id")
    val userId: String?=""
)

@JsonClass(generateAdapter = true)
data class UpdateProfileResponse(
    @Json(name = "customer")
    val customer: CustomerEdit?,
    @Json(name = "message")
    val message: String?="",
    @Json(name = "user")
    val user: User?
)

//@JsonClass(generateAdapter = true)
//data class Customer(
//    @Json(name = "area_id")
//    val areaId: Any,
//    @Json(name = "created_at")
//    val createdAt: String,
//    @Json(name = "first_name")
//    val firstName: String,
//    @Json(name = "id")
//    val id: Int,
//    @Json(name = "last_name")
//    val lastName: String,
//    @Json(name = "license")
//    val license: Any,
//    @Json(name = "media")
//    val media: List<Any>,
//    @Json(name = "nid")
//    val nid: Any,
//    @Json(name = "photo")
//    val photo: Any,
//    @Json(name = "previous_order_date")
//    val previousOrderDate: Any,
//    @Json(name = "register_by_id")
//    val registerById: String,
//    @Json(name = "shop_address")
//    val shopAddress: String,
//    @Json(name = "shop_name")
//    val shopName: String,
//    @Json(name = "status")
//    val status: String,
//    @Json(name = "updated_at")
//    val updatedAt: String,
//    @Json(name = "user_id")
//    val userId: String,
//    @Json(name = "zone_id")
//    val zoneId: Any
//)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "created_at")
    val createdAt: String?="",
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
    @Json(name = "updated_at")
    val updatedAt: String?=""
)


@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "customer")
    val customer: UpdateProfileCustomer,
    @Json(name = "_method")
    val method: String?="PUT",
    @Json(name = "phone")
    val phone: String
)

@JsonClass(generateAdapter = true)
data class UpdateProfileCustomer(
    @Json(name = "first_name")
    var firstName: String?="",
    @Json(name = "last_name")
    var lastName: String?="",
    @Json(name = "license")
    var license: String?="",
    @Json(name = "nid")
    var nid: String?="",
    @Json(name = "shop_address")
    var shopAddress: String?="",
    @Json(name = "shop_name")
    var shopName: String?=""
)


