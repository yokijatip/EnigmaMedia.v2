package com.enigma.enigmamediav2.viewModel.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigma.enigmamediav2.data.remote.response.AddResponse
import com.enigma.enigmamediav2.repo.Repository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddViewModel(private val repository: Repository) : ViewModel() {

    suspend fun addStory(imageFile: File, description: String): AddResponse {
        val reqDescription = description.toRequestBody("text/plain".toMediaType())
        val reqImage = imageFile.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, reqImage)

        return repository.addStory(imagePart, reqDescription)
    }

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}