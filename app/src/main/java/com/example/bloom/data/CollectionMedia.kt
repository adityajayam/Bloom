package com.example.bloom.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionMedia(
    @Json(name = "page") val page: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "id") val collectionId: String,
    @Json(name = "media") val mediaList: List<Media>,
)

data class Media(
    @Json(name = "type") val type: String,
    @Json(name = "id") val id: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "url") val url: String,
    @Json(name = "photographer") val photographer: String,
    @Json(name = "photographer_url") val photographerUrl: String,
    @Json(name = "photographer_id") val photographerId: String,
    @Json(name = "avg_color") val avgColor: String,
    @Json(name = "src") val src: ImageUrls,
    @Json(name = "liked") val liked: Boolean,
)

data class ImageUrls(
    @Json(name = "original") val original: String,
    @Json(name = "large2x") val large2x: String,
    @Json(name = "large") val large: String,
    @Json(name = "medium") val medium: String,
    @Json(name = "small") val small: String,
    @Json(name = "portrait") val portrait: String,
    @Json(name = "landscape") val landscape: String,
    @Json(name = "tiny") val tiny: String,
)