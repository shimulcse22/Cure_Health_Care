package com.devshawon.curehealthcare.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenResponse(
    @Json(name = "code")
    val code: String?,
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "message")
    val message: String?
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "refresh_token")
    val refreshToken: String?
)