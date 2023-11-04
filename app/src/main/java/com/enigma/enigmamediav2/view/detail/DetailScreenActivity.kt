package com.enigma.enigmamediav2.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.enigma.enigmamediav2.databinding.ActivityDetailScreenBinding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.main.MainActivity
import com.enigma.enigmamediav2.viewModel.detail.DetailViewModel
import com.enigma.enigmamediav2.viewModel.detail.ViewModelFactory

@SuppressLint("SimpleDateFormat")
class DetailScreenActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailScreenBinding
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        //        ViewModel
        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]

        CommonUtils.showLoading(detailBinding.loadingDetail, true)
        getDetailId()

        detailBinding.apply {
            icBack.setOnClickListener {
                val intent = Intent(this@DetailScreenActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        detailViewModel.getStoryDetailLiveData().observe(this) { storyDetail ->
            if (storyDetail != null) {
                CommonUtils.showLoading(detailBinding.loadingDetail, false)
                detailBinding.apply {
                    Glide.with(this@DetailScreenActivity)
                        .load(storyDetail.photoUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivPhoto)

                    tvName.text = storyDetail.name
                    tvDate.text = storyDetail.createdAt
                    tvDescription.text = storyDetail.description

                    val date = storyDetail.createdAt.toString()
                    val dateFormat = CommonUtils.formatter(date)
                    detailBinding.tvDate.text = dateFormat

                    val randomAvatar = CommonUtils.getRandomAvatar()

                    Glide.with(this@DetailScreenActivity)
                        .load(randomAvatar)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAvatar)
                }
            }
        }

    }

    private fun getDetailId() {
        val id = intent.getStringExtra("extra_id")
        if (id != null) {
            detailViewModel.getStoryDetail(id)
        }
    }
}