package com.enigma.enigmamediav2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.enigma.enigmamediav2.data.remote.response.ListStoryItem
import com.enigma.enigmamediav2.databinding.ItemMainBinding
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.detail.DetailScreenActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, storyItem: ListStoryItem) {

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailScreenActivity::class.java)
                intent.putExtra("extra_id", storyItem.id)
                context.startActivity(intent)
            }

            val randomAvatar = CommonUtils.getRandomAvatar()

            binding.apply {
                Glide.with(itemView)
                    .load(randomAvatar)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivAvatar)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(storyItem.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivPhoto)
                tvName.text = storyItem.name

                val dateBeforeFormatter = storyItem.createdAt.toString()
                val dateAfterFormatter = CommonUtils.formatter(dateBeforeFormatter)
                tvDate.text = dateAfterFormatter

                btnMap.setOnClickListener {
                    Toast.makeText(context, "Fitur Belum Tersedia, Sabar 😛", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val view = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(holder.itemView.context, story)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}