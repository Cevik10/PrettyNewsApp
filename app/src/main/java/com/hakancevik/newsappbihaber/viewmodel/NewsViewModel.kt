package com.hakancevik.newsappbihaber.viewmodel

import androidx.lifecycle.ViewModel
import com.hakancevik.newsappbihaber.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {





}