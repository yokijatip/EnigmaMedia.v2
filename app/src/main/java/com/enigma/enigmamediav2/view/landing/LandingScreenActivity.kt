package com.enigma.enigmamediav2.view.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enigma.enigmamediav2.R
import com.enigma.enigmamediav2.databinding.ActivityLandingScreenBinding
import com.enigma.enigmamediav2.view.login.LoginScreenActivity
import com.enigma.enigmamediav2.view.register.RegisterScreenActivity

class LandingScreenActivity : AppCompatActivity() {

    private lateinit var landingScreenBinding: ActivityLandingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landingScreenBinding = ActivityLandingScreenBinding.inflate(layoutInflater)
        setContentView(landingScreenBinding.root)

        landingScreenBinding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LandingScreenActivity, LoginScreenActivity::class.java))
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LandingScreenActivity, RegisterScreenActivity::class.java))
            }

        }

    }


}