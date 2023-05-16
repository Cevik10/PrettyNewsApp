package com.hakancevik.newsappbihaber.viewmodel

import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI

import android.os.Build

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.repo.NewsRepository
import com.hakancevik.newsappbihaber.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import retrofit2.Response
import java.io.IOException

import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var categoryNewsPage = 1
    var categoryNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    fun getCategoryNews(category: String) = viewModelScope.launch {
        safeCategoryNewsCall(category)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }

                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleCategoryNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                categoryNewsPage++
                if (categoryNewsResponse == null) {
                    categoryNewsResponse = resultResponse
                } else {
                    val oldArticles = categoryNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(categoryNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
        savedNewsInfo.value = false
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getSavedNews(): LiveData<List<Article>> {
        return repository.getSavedNews()
    }


    val savedNewsInfo = MutableLiveData<Boolean>()

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeCategoryNewsCall(category: String) {
        categoryNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(category, categoryNewsPage)
                categoryNews.postValue(handleCategoryNewsResponse(response))
            } else {
                categoryNews.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoryNews.postValue(Resource.Error("Network Failure"))
                else -> categoryNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private fun hasInternetConnection(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}