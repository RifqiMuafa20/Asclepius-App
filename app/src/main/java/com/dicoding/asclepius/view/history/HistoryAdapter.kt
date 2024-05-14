package com.dicoding.asclepius.view.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.entity.PhotoHistory
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.view.home.ResultActivity

class HistoryAdapter : ListAdapter<PhotoHistory, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, photo.photo)
            it.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoHistory) {
            binding.tvItemTitle.text = photo.photoName
            binding.tvItemDate.text = photo.date
            binding.tvItemResult.text = photo.result
            binding.tvItemInference.text = photo.inference
            Glide.with(binding.root.context)
                .load(photo.photo.toUri())
                .into(binding.imgItemPhoto);
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoHistory>() {
            override fun areItemsTheSame(oldItem: PhotoHistory, newItem: PhotoHistory): Boolean {
                return oldItem.photoName == newItem.photoName
            }

            override fun areContentsTheSame(oldItem: PhotoHistory, newItem: PhotoHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}