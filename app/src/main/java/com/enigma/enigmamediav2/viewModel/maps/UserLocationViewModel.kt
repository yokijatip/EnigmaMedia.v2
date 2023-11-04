package com.enigma.enigmamediav2.viewModel.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enigma.enigmamediav2.data.remote.response.ListStoryItem
import com.enigma.enigmamediav2.repo.Repository
import kotlinx.coroutines.launch

class UserLocationViewModel(private val repository: Repository) : ViewModel() {

    private val _storyListLiveData = MutableLiveData<List<ListStoryItem>>()
    val storyListLiveData: LiveData<List<ListStoryItem>>
        get() = _storyListLiveData

    fun getUserLocation() {
        viewModelScope.launch {
            try {
                val response = repository.getLocation()
                val storyList = response.listStory
                _storyListLiveData.value = storyList
            } catch (e: Exception) {
                throw e
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLocationViewModel::class.java)) {
            return UserLocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}