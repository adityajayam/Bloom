package com.example.bloom.repository

import android.util.Log
import com.example.bloom.data.CollectionMedia
import com.example.bloom.data.MyCollectionList
import com.example.bloom.network.BloomApi

class BloomRepository {

    suspend fun fetchCollectionsList(): MyCollectionList? {
        val response = BloomApi.retrofitService.getCollectionsList()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e(TAG, response.code().toString())
            throw Throwable(response.code().toString())
        }
    }

    suspend fun fetchCollectionMedia(collectionId: String): CollectionMedia? {
        val response = BloomApi.retrofitService.getCollectionMedia(collectionId)
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e(TAG, response.code().toString())
            throw Throwable(response.code().toString())
        }
    }

    companion object {
        private const val TAG = "BloomRepository"
    }
}