package com.enigma.enigmamediav2.view.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.enigma.enigmamediav2.databinding.ActivityAddScreenBinding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.helper.getImageUri
import com.enigma.enigmamediav2.helper.uriToFile
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.main.MainActivity
import com.enigma.enigmamediav2.viewModel.add.AddViewModel
import com.enigma.enigmamediav2.viewModel.add.ViewModelFactory
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class AddScreenActivity : AppCompatActivity() {

    private lateinit var addBinding: ActivityAddScreenBinding
    private lateinit var addViewModel: AddViewModel
    private var currentImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        addBinding = ActivityAddScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(addBinding.root)

        //        ViewModel
        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        addViewModel = ViewModelProvider(this, viewModelFactory)[AddViewModel::class.java]

        addBinding.apply {
            icBack.setOnClickListener {
                startActivity(Intent(this@AddScreenActivity, MainActivity::class.java))
                finish()
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnUpload.setOnClickListener {
                CommonUtils.showLoading(addBinding.loadingAdd, true)
                Handler().postDelayed({

                                      val description = edtDescription.text.toString()
                    if (description.isNotEmpty()) {
                        lifecycleScope.launch {
                            upload(description)
                            onUploadSuccess()
                        }
                    } else {
                        CommonUtils.showToast(this@AddScreenActivity, "Deskripsi Gaboleh Kosong dong woy")
                        onUploadFailed()
                    }
                }, 6000)
            }
        }


    }

    private suspend fun upload(description: String) {
        try {
            val imageFile = uriToFile(currentImageUri!!, this)
            val compress = imageFile?.let { Compressor.compress(this, it) }
            if (compress != null) {
                addViewModel.addStory(compress, description)
            }
        } catch (e: Exception) {
            throw Exception("Pilih Poto")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            onUploadFailed()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            addBinding.ivPhoto.setImageURI(it)
        }
    }

    private fun onUploadSuccess() {
        CommonUtils.showLoading(addBinding.loadingAdd, false)
        CommonUtils.showToast(this, "Upload Berhasil")
        val intent = Intent(this@AddScreenActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun onUploadFailed() {
        CommonUtils.showLoading(addBinding.loadingAdd, false)
        CommonUtils.showToast(this, "Gagal Upload Coy")
    }
}