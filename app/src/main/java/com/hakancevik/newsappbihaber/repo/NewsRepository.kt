package com.hakancevik.newsappbihaber.repo

import androidx.lifecycle.LiveData
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.util.Resource
import retrofit2.Response

interface NewsRepository {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>

    suspend fun insertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getAllArticle(): LiveData<List<Article>>

    suspend fun searchNews(newsString: String): Resource<NewsResponse>


}