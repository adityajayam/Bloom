package com.example.bloom.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
{
"collections": [
{
"id": "9mp14cx",
"title": "Cool Cats",
"description": null,
"private": false,
"media_count": 6,
"photos_count": 5,
"videos_count": 1
}
],

"page": 2,
"per_page": 1,
"total_results": 5,
"next_page": "https://api.pexels.com/v1/collections/?page=3&per_page=1",
"prev_page": "https://api.pexels.com/v1/collections/?page=1&per_page=1"
}
 */
@JsonClass(generateAdapter = true)
data class MyCollectionList(
    @Json(name = "collections") val collectionsList: List<Collection>,
    @Json(name = "page") val page: Int,
    @Json(name = "per_page") val per_page: Int,
    @Json(name = "total_results") val total_results: Int,
    @Json(name = "next_page") val nextPage: String? = null,
    @Json(name = "prev_page") val prevPage: String? = null,
)


data class Collection(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "private") val private: Boolean,
    @Json(name = "media_count") val mediaCount: Int,
    @Json(name = "photos_count") val photosCount: Int,
    @Json(name = "videos_count") val videosCount: Int,
)
