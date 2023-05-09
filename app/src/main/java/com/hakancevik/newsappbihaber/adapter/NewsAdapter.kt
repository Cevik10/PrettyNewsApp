package com.hakancevik.newsappbihaber.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hakancevik.newsappbihaber.databinding.RecyclerRowNewsBinding
import com.hakancevik.newsappbihaber.model.Article
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: RecyclerRowNewsBinding) : RecyclerView.ViewHolder(binding.root)


    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = RecyclerRowNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.itemView.apply {
            glide.load(article.urlToImage).into(holder.binding.articleImageView)
            holder.binding.sourceText.text = article.source?.name
            holder.binding.titleText.text = article.title
            holder.binding.descriptionText.text = article.description
            holder.binding.publishedAtText.text = article.publishedAt

            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }

        }
    }


    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}