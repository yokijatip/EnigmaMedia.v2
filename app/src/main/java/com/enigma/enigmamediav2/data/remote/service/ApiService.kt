package com.enigma.enigmamediav2.data.remote.service

import com.enigma.enigmamediav2.data.remote.response.AddResponse
import com.enigma.enigmamediav2.data.remote.response.DetailResponse
import com.enigma.enigmamediav2.data.remote.response.LoginResponse
import com.enigma.enigmamediav2.data.remote.response.RegisterResponse
import com.enigma.enigmamediav2.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories/{id}")
    suspend fun getDetail(
        @Path("id") id: String,
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Response<StoryResponse>

    @GET("stories")
    suspend fun getUserLocation(
        @Query("location") location: Int = 1,
    ): StoryResponse

}