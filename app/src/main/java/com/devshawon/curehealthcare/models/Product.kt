package com.devshawon.curehealthcare.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class FormResponse(
    @Json(name = "forms")
    val forms: List<Form>
)

@JsonClass(generateAdapter = true)
data class CompanyResponse(
    @Json(name = "companies")
    val forms: List<Form>
)

@JsonClass(generateAdapter = true)
data class Form(
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "name")
    val name: String?="",
    var checkBox : Boolean?= false
)

@JsonClass(generateAdapter = true)
data class ProductRequest(
    @Json(name = "company")
    val company: String?="",
    @Json(name = "first_latter")
    val firstLatter: String?="",
    @Json(name = "form")
    val form: String?="",
    @Json(name = "page")
    val page: Int?=0,
    @Json(name = "search")
    val search: String?=""
)

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "current_page")
    val currentPage: Int?=0,
    @Json(name = "data")
    val `data`: List<ProductData>,
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
data class ProductData(
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
    @Json(name = "estimated_delivery")
    val estimatedDelivery: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manufacturing_company")
    val manufacturingCompany: ManufacturingCompany,
    @Json(name = "manufacturing_company_id")
    val manufacturingCompanyId: String?="",
    @Json(name = "media")
    val media: List<Media>,
    @Json(name = "mrp")
    val mrp: String?="",
    @Json(name = "photo")
    val photo: MedicinePhoto,
    @Json(name = "product_forms")
    val productForms: ProductForms,
    @Json(name = "product_forms_id")
    val productFormsId: String?="",
    @Json(name = "product_generics")
    val productGenerics: ProductGenerics,
    @Json(name = "product_generics_id")
    val productGenericsId: String?="",
    @Json(name = "product_limits")
    val productLimits: String?="",
    @Json(name = "purchase_price")
    val purchasePrice: String?="",
    @Json(name = "sale_price")
    val salePrice: String?="",
    @Json(name = "status")
    val status: String?="",
    @Json(name = "stock")
    val stock: Stock,
    @Json(name = "strength")
    val strength: String?="",
    @Json(name = "unit_type")
    val unitType: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?="",
    var productCount :Int?= 0,
)

@JsonClass(generateAdapter = true)
data class Link(
    @Json(name = "active")
    val active: Boolean,
    @Json(name = "label")
    val label: String?="",
    @Json(name = "url")
    val url: String?=""
)

@JsonClass(generateAdapter = true)
data class ManufacturingCompany(
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
data class Media(
    @Json(name = "collection_name")
    val collectionName: String?="",
    @Json(name = "conversions_disk")
    val conversionsDisk: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "custom_properties")
    val customProperties: List<String>?= listOf(),
    @Json(name = "disk")
    val disk: String?="",
    @Json(name = "file_name")
    val fileName: String?="",
    @Json(name = "generated_conversions")
    val generatedConversions: GeneratedConversions,
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manipulations")
    val manipulations: List<String>?= listOf(),
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
    val responsiveImages: List<String>?= listOf(),
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
data class MedicinePhoto(
    @Json(name = "collection_name")
    val collectionName: String?="",
    @Json(name = "conversions_disk")
    val conversionsDisk: String?="",
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "custom_properties")
    val customProperties: List<String>?= listOf(),
    @Json(name = "disk")
    val disk: String?="",
    @Json(name = "file_name")
    val fileName: String?="",
    @Json(name = "generated_conversions")
    val generatedConversions: GeneratedConversions,
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "manipulations")
    val manipulations: List<String>?= listOf(),
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
    val responsiveImages: List<String>?= listOf(),
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
data class ProductForms(
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
data class ProductGenerics(
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
data class Stock(
    @Json(name = "created_at")
    val createdAt: String?="",
    @Json(name = "id")
    val id: Int?=0,
    @Json(name = "product_id")
    val productId: String?="",
    @Json(name = "purchase_date")
    val purchaseDate: String?="",
    @Json(name = "quantity")
    val quantity: String?="",
    @Json(name = "unite_price")
    val unitePrice: String?="",
    @Json(name = "updated_at")
    val updatedAt: String?=""
)
