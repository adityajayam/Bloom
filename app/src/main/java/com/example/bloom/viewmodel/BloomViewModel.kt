package com.example.bloom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom.data.CollectionMedia
import com.example.bloom.data.Media
import com.example.bloom.data.MyCollectionList
import com.example.bloom.repository.BloomRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class BloomViewModel(private val repository: BloomRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private lateinit var collectionsList: MyCollectionList
    private lateinit var collectionMedia: CollectionMedia
    private var _mediaList = MutableLiveData<List<Media>>()
    var mediaList: LiveData<List<Media>> = _mediaList
    fun getCollectionsList() {
        viewModelScope.launch {
            try {
                collectionsList = repository.fetchCollectionsList()!!
                collectionMedia =
                    repository.fetchCollectionMedia(collectionsList.collectionsList[0].id)!!
                _mediaList.value = collectionMedia.mediaList
                _text.value = collectionsList.toString()
            } catch (e: Exception) {
                _text.value = e.toString()
            }
        }
    }
}