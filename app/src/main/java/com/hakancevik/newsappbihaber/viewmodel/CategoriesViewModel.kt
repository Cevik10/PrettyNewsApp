package com.hakancevik.newsappbihaber.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.repo.NewsRepository
import com.hakancevik.newsappbihaber.util.Constants.BUSINESS
import com.hakancevik.newsappbihaber.util.Constants.ENTERTAINMENT
import com.hakancevik.newsappbihaber.util.Constants.GENERAL
import com.hakancevik.newsappbihaber.util.Constants.HEALTH
import com.hakancevik.newsappbihaber.util.Constants.SCIENCE
import com.hakancevik.newsappbihaber.util.Constants.SPORTS
import com.hakancevik.newsappbihaber.util.Constants.TECHNOLOGY
import com.hakancevik.newsappbihaber.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var categoryNewsPage = 1
    var categoryNewsResponse: NewsResponse? = null

    val businessNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var businessNewsPage = 1
    var businessNewsResponse: NewsResponse? = null

    val entertainmentNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var entertainmentNewsPage = 1
    var entertainmentNewsResponse: NewsResponse? = null

    val generalNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var generalNewsPage = 1
    var generalNewsResponse: NewsResponse? = null

    val healthNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var healthNewsPage = 1
    var healthNewsResponse: NewsResponse? = null

    val scienceNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var scienceNewsPage = 1
    var scienceNewsResponse: NewsResponse? = null

    val sportsNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sportsNewsPage = 1
    var sportsNewsResponse: NewsResponse? = null

    val technologyNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var technologyNewsPage = 1
    var technologyNewsResponse: NewsResponse? = null


    fun getCategoryNews(countryCode: String, category: String) = viewModelScope.launch {
        safeCategoryNewsCall(countryCode, category)
    }


    fun getBusinessNews(countryCode: String) = viewModelScope.launch {
        safeBusinessNewsCall(countryCode)
    }

    fun getEntertainmentNews(countryCode: String) = viewModelScope.launch {
        safeEntertainmentNewsCall(countryCode)
    }

    fun getGeneralNews(countryCode: String) = viewModelScope.launch {
        safeGeneralNewsCall(countryCode)
    }


    fun getHealthNews(countryCode: String) = viewModelScope.launch {
        safeHealthNewsCall(countryCode)
    }

    fun getScienceNews(countryCode: String) = viewModelScope.launch {
        safeScienceNewsCall(countryCode)
    }

    fun getSportsNews(countryCode: String) = viewModelScope.launch {
        safeSportsNewsCall(countryCode)
    }

    fun getTechnologyNews(countryCode: String) = viewModelScope.launch {
        safeTechnologyNewsCall(countryCode)
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


    private fun handleBusinessNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                businessNewsPage++
                if (businessNewsResponse == null) {
                    businessNewsResponse = resultResponse
                } else {
                    val oldArticles = businessNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(businessNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleEntertainmentNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                entertainmentNewsPage++
                if (entertainmentNewsResponse == null) {
                    entertainmentNewsResponse = resultResponse
                } else {
                    val oldArticles = entertainmentNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(entertainmentNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleGeneralNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                generalNewsPage++
                if (generalNewsResponse == null) {
                    generalNewsResponse = resultResponse
                } else {
                    val oldArticles = generalNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(generalNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleHealthNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                healthNewsPage++
                if (healthNewsResponse == null) {
                    healthNewsResponse = resultResponse
                } else {
                    val oldArticles = healthNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(healthNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleScienceNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                scienceNewsPage++
                if (scienceNewsResponse == null) {
                    scienceNewsResponse = resultResponse
                } else {
                    val oldArticles = scienceNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(scienceNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSportsNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                sportsNewsPage++
                if (sportsNewsResponse == null) {
                    sportsNewsResponse = resultResponse
                } else {
                    val oldArticles = sportsNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(sportsNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTechnologyNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                technologyNewsPage++
                if (technologyNewsResponse == null) {
                    technologyNewsResponse = resultResponse
                } else {
                    val oldArticles = technologyNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(technologyNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun safeCategoryNewsCall(countryCode: String, category: String) {
        categoryNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, category, categoryNewsPage)
                categoryNews.postValue(handleCategoryNewsResponse(response))
            } else {
                categoryNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoryNews.postValue(Resource.Error("Network Failure"))
                else -> categoryNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private suspend fun safeBusinessNewsCall(countryCode: String) {
        businessNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, BUSINESS, businessNewsPage)
                businessNews.postValue(handleBusinessNewsResponse(response))
            } else {
                businessNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> businessNews.postValue(Resource.Error("Network Failure"))
                else -> businessNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeEntertainmentNewsCall(countryCode: String) {
        entertainmentNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, ENTERTAINMENT, entertainmentNewsPage)
                entertainmentNews.postValue(handleEntertainmentNewsResponse(response))
            } else {
                entertainmentNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> entertainmentNews.postValue(Resource.Error("Network Failure"))
                else -> entertainmentNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeGeneralNewsCall(countryCode: String) {
        generalNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, GENERAL, generalNewsPage)
                generalNews.postValue(handleGeneralNewsResponse(response))
            } else {
                generalNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> generalNews.postValue(Resource.Error("Network Failure"))
                else -> generalNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeHealthNewsCall(countryCode: String) {
        healthNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, HEALTH, healthNewsPage)
                healthNews.postValue(handleHealthNewsResponse(response))
            } else {
                healthNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> healthNews.postValue(Resource.Error("Network Failure"))
                else -> healthNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeScienceNewsCall(countryCode: String) {
        scienceNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, SCIENCE, scienceNewsPage)
                scienceNews.postValue(handleScienceNewsResponse(response))
            } else {
                scienceNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> scienceNews.postValue(Resource.Error("Network Failure"))
                else -> scienceNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private suspend fun safeSportsNewsCall(countryCode: String) {
        sportsNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, SPORTS, sportsNewsPage)
                sportsNews.postValue(handleSportsNewsResponse(response))
            } else {
                sportsNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> sportsNews.postValue(Resource.Error("Network Failure"))
                else -> sportsNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private suspend fun safeTechnologyNewsCall(countryCode: String) {
        technologyNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, TECHNOLOGY, technologyNewsPage)
                technologyNews.postValue(handleTechnologyNewsResponse(response))
            } else {
                technologyNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> technologyNews.postValue(Resource.Error("Network Failure"))
                else -> technologyNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private fun hasInternetConnection(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}