package com.enigma.enigmamediav2.viewModel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigma.enigmamediav2.data.remote.response.RegisterResponse
import com.enigma.enigmamediav2.repo.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}