package com.enigma.enigmamediav2.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.enigma.enigmamediav2.databinding.ActivitySplashScreenBinding
import com.enigma.enigmamediav2.helper.TokenPreferences
import com.enigma.enigmamediav2.helper.dataStore
import com.enigma.enigmamediav2.view.landing.LandingScreenActivity
import com.enigma.enigmamediav2.view.main.MainActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        splashScreenBinding.apply {
            tvSplash.animate().alpha(1f).setDuration(2000).start()
            ivSplash.animate().alpha(1f).setDuration(2000).start()
            Handler().postDelayed({
                lifecycleScope.launch {
                    val token = loadTokenDataStore()
                    if (token.isNotEmpty()) {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    } else {
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                LandingScreenActivity::class.java
                            )
                        )
                    }
                    finish()
                }
            }, 3000)
        }

    }

    private suspend fun loadTokenDataStore() : String {
        val dataStore = TokenPreferences.getInstance(this.dataStore)
        return dataStore.getToken()
    }

}