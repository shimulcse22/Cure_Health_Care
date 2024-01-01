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
data class EditProfileRequest(
    @Json(name = "customer")
    val customer: Customer,
    @Json(name = "_method")
    val method: String,
    @Json(name = "phone")
    val phone: String
)



