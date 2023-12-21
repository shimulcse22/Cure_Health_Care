package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "forms")
    val forms: List<Form>
)

@JsonClass(generateAdapter = true)
data class Form(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)