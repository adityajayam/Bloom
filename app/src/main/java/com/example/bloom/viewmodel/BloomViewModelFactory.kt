package com.example.bloom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bloom.repository.BloomRepository
import java.lang.IllegalArgumentException

class BloomViewModelFactory(val repository: BloomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BloomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}