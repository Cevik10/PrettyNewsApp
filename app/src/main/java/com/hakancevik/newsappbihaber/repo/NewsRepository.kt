package com.hakancevik.newsappbihaber.repo

import androidx.lifecycle.LiveData
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.util.Resource

interface NewsRepository {

    suspend fun insertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getAllArticle(): LiveData<List<Article>>

    suspend fun searchNews(newsString: String): Resource<NewsResponse>


}