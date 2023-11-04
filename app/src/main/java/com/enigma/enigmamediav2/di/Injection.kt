package com.enigma.enigmamediav2.di

import android.app.Application
import android.content.Context
import com.enigma.enigmamediav2.data.remote.config.ApiConfig
import com.enigma.enigmamediav2.helper.TokenPreferences
import com.enigma.enigmamediav2.helper.dataStore
import com.enigma.enigmamediav2.repo.Repository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val dataStore = TokenPreferences.getInstance(context.dataStore)
        val user = runBlocking { dataStore.getToken() }
        val apiService = ApiConfig.getApiService(user)
        return Repository(apiService)
    }
}