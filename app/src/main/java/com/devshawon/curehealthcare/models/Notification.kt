package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class NotificationResponse(
    @Json(name = "current_page")
    val currentPage: Int?=0,
    @Json(name = "data")
    val `data`: List<NotificationResponseData>,
    @Json(name = "first_page_url")
    val firstPageUrl: String?="",
    @Json(name = "from")
    val from: Int?=0,
    @Json(name = "last_page")
    val lastPage: Int?=0,
    @Json(name = "last_page_url")
    val lastPageUrl: String?="",
    @Json(name = "links")
    val links: List<Link>,
    @Json(name = "next_page_url")
    val nextPageUrl: String?="",
    @Json(name = "path")
    val path: String?="",
    @Json(name = "per_page")
    val perPage: Int?=0,
    @Json(name = "prev_page_url")
    val prevPageUrl: String?="",
    @Json(name = "to")
    val to: Int?=0,
    @Json(name = "total")
    val total: Int?=0
)

@JsonClass(generateAdapter = true)
data class NotificationResponseData(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "description")
    val description: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "push_status")
    val pushStatus: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?="",
    @Json(name = "user_id")
    val userId: String?=""
)
