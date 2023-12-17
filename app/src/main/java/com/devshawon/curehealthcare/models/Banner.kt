package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


class BannerResponse : ArrayList<BannerResponseItem?>()

@JsonClass(generateAdapter = true)
data class BannerResponseItem(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "link")
    val link: String?="",
    @Json(name = "name")
    val name: String?="",
    @Json(name = "photo")
    val photo: Photo,
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "collection_name")
    val collectionName: String?="",
    @Json(name = "conversions_disk")
    val conversionsDisk: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "custom_properties")
    val customProperties: List<String>?= emptyList(),
    @Json(name = "disk")
    val disk: String?="",
    @Json(name = "file_name")
    val fileName: String?="",
    @Json(name = "generated_conversions")
    val generatedConversions: GeneratedConversions?,
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manipulations")
    val manipulations: List<String>?= emptyList(),
    @Json(name = "mime_type")
    val mimeType: String?="",
    @Json(name = "model_id")
    val modelId: String?="",
    @Json(name = "model_type")
    val modelType: String?="",
    @Json(name = "name")
    val name: String?="",
    @Json(name = "order_column")
    val orderColumn: String?="",
    @Json(name = "original_url")
    val originalUrl: String?="",
    @Json(name = "preview")
    val preview: String?="",
    @Json(name = "preview_url")
    val previewUrl: String?="",
    @Json(name = "responsive_images")
    val responsiveImages: List<String>?= emptyList(),
    @Json(name = "size")
    val size: String?="",
    @Json(name = "thumbnail")
    val thumbnail: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?="",
    @Json(name = "url")
    val url: String?="",
    @Json(name = "uuid")
    val uuid: String?=""
)

@JsonClass(generateAdapter = true)
data class GeneratedConversions(
    @Json(name = "preview")
    val preview: Boolean?=false,
    @Json(name = "thumb")
    val thumb: Boolean?=false
)@JsonClass(generateAdapter = true)


data class BannerResponseMobile(
    @Json(name = "sliders")
    val sliders: List<String>
)


