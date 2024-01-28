package com.devshawon.curehealthcare.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleOrderResponse(
    @Json(name = "amount")
    val amount: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "is_pre_order")
    val isPreOrder: Boolean,
    @Json(name = "order_date")
    val orderDate: String?="",
    @Json(name = "order_placed_at")
    val orderPlacedAt: String?="",
    @Json(name = "products")
    val products: List<SingleOrderProduct>,
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
data class SingleOrderProduct(
    @Json(name = "box_size")
    val boxSize: String?="",
    @Json(name = "commercial_name")
    val commercialName: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "description")
    val description: String?="",
    @Json(name = "discount")
    val discount: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manufacturing_company")
    val manufacturingCompany: SingleOrderManufacturingCompany,
    @Json(name = "manufacturing_company_id")
    val manufacturingCompanyId: String?="",
    @Json(name = "media")
    val media: List<SingleOrderMedia>,
    @Json(name = "mrp")
    val mrp: String?="",
    @Json(name = "photo")
    val photo: SingleOrderPhoto,
    @Json(name = "product_forms")
    val productForms: SingleOrderProductForms,
    @Json(name = "product_forms_id")
    val productFormsId: String?="",
    @Json(name = "product_generics")
    val productGenerics: SingleOrderProductGenerics,
    @Json(name = "product_generics_id")
    val productGenericsId: String?="",
    @Json(name = "product_limits")
    val productLimits: String?="",
    @Json(name = "purchase_price")
    val purchasePrice: String?="",
    @Json(name = "quantity")
    val quantity: String?="",
    @Json(name = "sale_price")
    val salePrice: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "strength")
    val strength: String?="",
    @Json(name = "unit_type")
    val unitType: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class SingleOrderManufacturingCompany(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "description")
    val description: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class SingleOrderMedia(
    @Json(name = "collection_name")
    val collectionName: String?="",
    @Json(name = "conversions_disk")
    val conversionsDisk: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "custom_properties")
    val customProperties: List<String>?= arrayListOf(),
    @Json(name = "disk")
    val disk: String?="",
    @Json(name = "file_name")
    val fileName: String?="",
    @Json(name = "generated_conversions")
    val generatedConversions: GeneratedConversions,
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manipulations")
    val manipulations: List<String>?= arrayListOf(),
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
    val responsiveImages: List<String>?= arrayListOf(),
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
data class SingleOrderPhoto(
    @Json(name = "collection_name")
    val collectionName: String?="",
    @Json(name = "conversions_disk")
    val conversionsDisk: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "custom_properties")
    val customProperties: List<String>?= arrayListOf(),
    @Json(name = "disk")
    val disk: String?="",
    @Json(name = "file_name")
    val fileName: String?="",
    @Json(name = "generated_conversions")
    val generatedConversions: GeneratedConversions,
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manipulations")
    val manipulations: List<String>?= arrayListOf(),
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
    val responsiveImages: List<String>?= arrayListOf(),
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
data class SingleOrderProductForms(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "description")
    val description: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class SingleOrderProductGenerics(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "description")
    val description: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)

@JsonClass(generateAdapter = true)
data class SingleOrderGeneratedConversions(
    @Json(name = "preview")
    val preview: Boolean,
    @Json(name = "thumb")
    val thumb: Boolean
)