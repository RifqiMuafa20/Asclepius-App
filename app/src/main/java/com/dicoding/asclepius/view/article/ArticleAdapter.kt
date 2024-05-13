package com.dicoding.asclepius.view.article

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.Locale
import android.content.pm.PackageManager

class ArticleAdapter(private val context: Context) : ListAdapter<ArticlesItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Item clicked: ${article.title}", Toast.LENGTH_SHORT).show()

            if(article.url == null){
                Toast.makeText(context, "Link not found", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(article.publishedAt)
            val formattedDate = outputFormat.format(date)

            binding.tvItemTitle.text = article.title
            binding.tvItemDescription.text = article.description
            binding.tvItemDate.text = formattedDate
            binding.tvItemAuthor.text = article.author
            Glide.with(binding.root)
                .load(article.urlToImage)
                .into(binding.imgItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
