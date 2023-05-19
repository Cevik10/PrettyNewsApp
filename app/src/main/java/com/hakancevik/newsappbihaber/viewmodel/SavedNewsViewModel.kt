package com.hakancevik.newsappbihaber.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    val savedNewsInfo = MutableLiveData<Boolean>()

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


}