package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class OrderResponse(
    @Json(name = "current_page")
    val currentPage: Int?=0,
    @Json(name = "data")
    val `data`: List<OrderData>,
    @Json(name = "first_page_url")
    val firstPageUrl: String?="",
    @Json(name = "from")
    val from: Int?=0,
    @Json(name = "last_page")
    val lastPage: Int?=0,
    @Json(name = "last_page_url")
    val lastPageUrl: String?="",
    @Json(name = "links")
    val links: List<LinkData>,
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
data class OrderData(
    @Json(name = "amount")
    val amount: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "is_pre_order")
    val isPreOrder: Boolean?=false,
    @Json(name = "order_date")
    val orderDate: String?="",
    @Json(name = "order_placed_at")
    val orderPlacedAt: String?="",
    @Json(name = "profit")
    val profit: String?="",
    @Json(name = "shop_id")
    val shopId: String?="",
    @Json(name = "shop_order_number")
    val shopOrderNumber: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class LinkData(
    @Json(name = "active")
    val active: Boolean?=false,
    @Json(name = "label")
    val label: String?="",
    @Json(name = "url")
    val url: String?=""
)