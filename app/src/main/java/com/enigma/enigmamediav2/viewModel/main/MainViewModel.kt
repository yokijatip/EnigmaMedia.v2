package com.enigma.enigmamediav2.viewModel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enigma.enigmamediav2.data.remote.response.ListStoryItem
import com.enigma.enigmamediav2.repo.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {

    val getStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}