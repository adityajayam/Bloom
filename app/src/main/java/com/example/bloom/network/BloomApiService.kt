package com.example.bloom.network

import com.example.bloom.data.CollectionMedia
import com.example.bloom.data.MyCollectionList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val BASE_URL = "https://api.pexels.com/v1/"
private const val AUTH_KEY = "563492ad6f91700001000001d5f114a2026041bd903c6f390dc43fe4"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface BloomApiService {
    @Headers("Authorization:$AUTH_KEY")
    @GET("collections?per_page=1")
    suspend fun getCollectionsList(): Response<MyCollectionList>

    @Headers("Authorization:$AUTH_KEY")
    @GET("collections/{id}")
    suspend fun getCollectionMedia(@Path("id") collectionId: String): Response<CollectionMedia>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BloomApi {
    val retrofitService: BloomApiService by lazy {
        retrofit.create(BloomApiService::class.java)
    }
}