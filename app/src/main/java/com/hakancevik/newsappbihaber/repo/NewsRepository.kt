package com.hakancevik.newsappbihaber.repo

import androidx.lifecycle.LiveData
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.util.Resource
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>

    suspend fun getCategoryNews(category: String, pageNumber: Int): Response<NewsResponse>

    suspend fun insertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getSavedNews(): LiveData<List<Article>>


}