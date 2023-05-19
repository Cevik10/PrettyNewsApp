package com.hakancevik.newsappbihaber.viewmodel.tabbed

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.newsappbihaber.model.NewsResponse
import com.hakancevik.newsappbihaber.repo.NewsRepository
import com.hakancevik.newsappbihaber.util.Constants
import com.hakancevik.newsappbihaber.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val healthConnectionInfo = MutableLiveData<Boolean>()

    val healthNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var healthNewsPage = 1
    var healthNewsResponse: NewsResponse? = null

    fun getHealthNews(countryCode: String) = viewModelScope.launch {
        safeHealthNewsCall(countryCode)
    }


    private fun handleHealthNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {

            healthConnectionInfo.value = false

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

    private suspend fun safeHealthNewsCall(countryCode: String) {
        healthNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCategoryNews(countryCode, Constants.HEALTH, healthNewsPage)
                healthNews.postValue(handleHealthNewsResponse(response))
            } else {
                healthNews.postValue(Resource.Error("No Internet Connection"))
                healthConnectionInfo.value = true
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> healthNews.postValue(Resource.Error("Network Failure"))
                else -> healthNews.postValue(Resource.Error("Conversion Error"))
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