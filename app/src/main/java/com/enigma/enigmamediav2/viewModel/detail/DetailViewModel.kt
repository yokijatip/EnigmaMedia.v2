package com.enigma.enigmamediav2.viewModel.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enigma.enigmamediav2.data.remote.response.Story
import com.enigma.enigmamediav2.repo.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val storyDetailLiveData = MutableLiveData<Story?>()

    fun getStoryDetail(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetail(id)
                val storyDetail = response.story
                storyDetailLiveData.value = storyDetail
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getStoryDetailLiveData(): MutableLiveData<Story?> {
        return storyDetailLiveData
    }

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}