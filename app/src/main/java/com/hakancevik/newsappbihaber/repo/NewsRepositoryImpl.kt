package com.hakancevik.newsappbihaber.repo

import androidx.lifecycle.LiveData
import com.hakancevik.newsappbihaber.api.NewsAPI
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.roomdb.NewsDao
import com.hakancevik.newsappbihaber.util.Resource
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsAPI: NewsAPI

) : NewsRepository {
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return newsAPI.getBreakingNews(countryCode, pageNumber)
    }

    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return newsAPI.searchForNews(searchQuery, pageNumber)
    }


    override suspend fun insertArticle(article: Article) {
        newsDao.insertArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        newsDao.deleteArticle(article)
    }

    override fun getSavedNews(): LiveData<List<Article>> {
        return newsDao.getAllArticles()
    }



}