package com.enigma.enigmamediav2.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.enigmamediav2.adapter.StoryAdapter
import com.enigma.enigmamediav2.databinding.ActivityMainBinding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.helper.TokenPreferences
import com.enigma.enigmamediav2.helper.dataStore
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.maps.UniversityLocationActivity
import com.enigma.enigmamediav2.view.add.AddScreenActivity
import com.enigma.enigmamediav2.view.landing.LandingScreenActivity
import com.enigma.enigmamediav2.view.maps.UserLocationActivity
import com.enigma.enigmamediav2.viewModel.main.MainViewModel
import com.enigma.enigmamediav2.viewModel.main.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

//        ViewModel
        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

//        Adapter & Recycler View
        adapter = StoryAdapter()
        val layoutManager = LinearLayoutManager(context)
        mainBinding.rvMain.layoutManager = layoutManager
        mainBinding.rvMain.adapter = adapter
//        Observer ViewModel
        mainViewModel.getStory.observe(this) { story ->
            CommonUtils.showLoading(mainBinding.loadingMain, true)
            if (story != null) {
                adapter.submitData(lifecycle, story)
            }
            CommonUtils.showLoading(mainBinding.loadingMain, false)
        }

        getStories()

        mainBinding.apply {
            btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    clearTokenDataStore()
                    startActivity(Intent(this@MainActivity, LandingScreenActivity::class.java))
                    finish()
                }
            }

            buttonGoogleMap.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserLocationActivity::class.java))
            }

            bgMap.setOnClickListener {
                startActivity(Intent(this@MainActivity, UniversityLocationActivity::class.java))
            }
        }
        floatingActionButtonAdd()
    }

    private fun getStories() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mainBinding.rvMain.layoutManager = layoutManager
        mainBinding.rvMain.adapter = adapter
        lifecycleScope.launch {
            mainViewModel.getStory.observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
                CommonUtils.showLoading(mainBinding.loadingMain, false)
            }
        }
    }

    private fun floatingActionButtonAdd() {
        mainBinding.apply {
            fabAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddScreenActivity::class.java))
            }
        }
    }

    private suspend fun clearTokenDataStore() {
        val dataStore = TokenPreferences.getInstance(this.dataStore)
        return dataStore.clearToken()
    }
}