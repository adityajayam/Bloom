package com.example.bloom.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.bloom.Constants
import com.example.bloom.DownloadImageWork
import com.example.bloom.data.CollectionMedia
import com.example.bloom.data.Media
import com.example.bloom.data.MyCollectionList
import com.example.bloom.repository.BloomRepository
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                collectionsList = repository.fetchCollectionsList()!!
                collectionMedia =
                    repository.fetchCollectionMedia(collectionsList.collectionsList[0].id)!!
                _mediaList.postValue(collectionMedia.mediaList)
                _text.value = collectionsList.toString()
            } catch (e: Exception) {
                _text.postValue(e.toString())
            }
        }
    }

    fun downloadImage(applicationContext: Context, imageData: Media) {
        val workManager = WorkManager.getInstance(applicationContext)
        val work = OneTimeWorkRequestBuilder<DownloadImageWork>()
        val inputData = Data.Builder()
        inputData.put(Constants.DOWNLOAD_IMAGE_URL, imageData.src.original)
        inputData.put(Constants.DOWNLOAD_IMAGE_PHOTOGRAPHER, imageData.photographer)
        inputData.put(Constants.DOWNLOAD_IMAGE_PHOTOGRAPHER, imageData.photographerId)
        inputData.put(Constants.DOWNLOAD_IMAGE_PHOTOGRAPHER, imageData.photographerId)
        work.setInputData(inputData.build())
        work.addTag(Constants.WORK_DOWNLOAD_IMAGE_TAG)
        work.setConstraints(Constraints.NONE)
        workManager.enqueue(work.build())
    }
}