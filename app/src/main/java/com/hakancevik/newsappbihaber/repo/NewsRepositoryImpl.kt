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


    override suspend fun insertArticle(article: Article) {
        newsDao.insertArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        newsDao.deleteArticle(article)
    }

    override fun getAllArticle(): LiveData<List<Article>> {
        return newsDao.getAllArticles()
    }

    override suspend fun searchNews(newsString: String): Resource<NewsResponse> {
        TODO("Not yet implemented")
    }

//    override suspend fun searchNews(newsString: String): Resource<NewsResponse> {
//        return try {
//            val response = newsAPI.searchForNews(newsString)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return@let Resource.success(it)
//                } ?: Resource.error("Error", null)
//            } else {
//                Resource.error("Error", null)
//            }
//
//        } catch (e: Exception) {
//            Resource.error("No Data!", null)
//        }
//    }
}