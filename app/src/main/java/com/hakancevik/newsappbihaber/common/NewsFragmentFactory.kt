package com.hakancevik.newsappbihaber.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.hakancevik.newsappbihaber.adapter.NewsAdapter
import com.hakancevik.newsappbihaber.adapter.SearchNewsAdapter
import com.hakancevik.newsappbihaber.view.BreakingNewsFragment
import com.hakancevik.newsappbihaber.view.SavedNewsFragment
import com.hakancevik.newsappbihaber.view.SearchNewsFragment
import javax.inject.Inject


class NewsFragmentFactory @Inject constructor(
    private val newsAdapter: NewsAdapter,
    private val searchNewsAdapter: SearchNewsAdapter,
    val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            BreakingNewsFragment::class.java.name -> BreakingNewsFragment(newsAdapter)
            SavedNewsFragment::class.java.name -> SavedNewsFragment(newsAdapter)
            SearchNewsFragment::class.java.name -> SearchNewsFragment(searchNewsAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }


}