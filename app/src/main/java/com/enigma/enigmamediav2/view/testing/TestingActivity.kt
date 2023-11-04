package com.enigma.enigmamediav2.view.testing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enigma.enigmamediav2.databinding.ActivityTestingBinding

class TestingActivity : AppCompatActivity() {

    private lateinit var testingBinding: ActivityTestingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testingBinding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(testingBinding.root)

        val id = intent.getStringExtra("extra_id")

        testingBinding.apply {
            tvTest.text = id
        }
    }
}