package com.enigma.enigmamediav2.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.enigma.enigmamediav2.data.remote.response.AddResponse
import com.enigma.enigmamediav2.data.remote.response.DetailResponse
import com.enigma.enigmamediav2.data.remote.response.ListStoryItem
import com.enigma.enigmamediav2.data.remote.response.LoginResponse
import com.enigma.enigmamediav2.data.remote.response.RegisterResponse
import com.enigma.enigmamediav2.data.remote.service.ApiService
import com.enigma.enigmamediav2.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse

class Repository(private val apiService: ApiService) {

    //    Register
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            val response = apiService.postRegister(name, email, password).awaitResponse()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Gagal Membuat Akun")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    //    Login
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = apiService.postLogin(email, password).awaitResponse()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw Exception("Gagal Login")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    //    Get Story
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    //    Add Story
    suspend fun addStory(image: MultipartBody.Part, description: RequestBody): AddResponse {
        try {
            val response = apiService.postStory(image, description)
            if (response.error == false) {
                return response
            } else {
                throw Exception("Upload Gagal")
            }
        } catch (e: Exception) {
            throw e
        }
    }

//    Detail Story
    suspend fun getDetail(id: String): DetailResponse {
        try {
            val response = apiService.getDetail(id)
            if (response.error == false) {
                return response
            } else {
                throw Exception("Gagal Mendapatkan Detail")
            }
        } catch (e: Exception) {
            throw e
        }
    }

}