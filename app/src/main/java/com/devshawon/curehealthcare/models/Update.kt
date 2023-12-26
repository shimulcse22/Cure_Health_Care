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