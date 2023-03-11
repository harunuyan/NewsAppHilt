package com.volie.newsapphilt.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.newsapphilt.databinding.ItemArticlePreviewBinding
import com.volie.newsapphilt.model.Article

class NewsAdapter(val onItemClick: (article: Article) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder>() {


    inner class NewsAdapterViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            val item = differ.currentList[position]
            with(binding) {
                Glide.with(root).load(item.urlToImage).into(ivArticleImage)
                tvTitle.text = item.title
                tvDescription.text = item.description
                tvPublishedAt.text = item.publishedAt
                tvSource.text = item.source.name
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterViewHolder {
        val binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsAdapterViewHolder(binding)
    }

    val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        holder.bindData(position)
    }
}