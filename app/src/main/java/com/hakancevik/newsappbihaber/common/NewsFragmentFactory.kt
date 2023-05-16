package com.hakancevik.newsappbihaber.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.hakancevik.newsappbihaber.adapter.NewsAdapter
import com.hakancevik.newsappbihaber.adapter.SearchNewsAdapter
import com.hakancevik.newsappbihaber.view.BreakingNewsFragment
import com.hakancevik.newsappbihaber.view.CategoriesFragment
import com.hakancevik.newsappbihaber.view.SavedNewsFragment
import com.hakancevik.newsappbihaber.view.SearchNewsFragment
import com.hakancevik.newsappbihaber.view.tabbed.BusinessFragment
import com.hakancevik.newsappbihaber.view.tabbed.EntertainmentFragment
import com.hakancevik.newsappbihaber.view.tabbed.GeneralFragment
import com.hakancevik.newsappbihaber.view.tabbed.HealthFragment
import com.hakancevik.newsappbihaber.view.tabbed.ScienceFragment
import com.hakancevik.newsappbihaber.view.tabbed.SportsFragment
import com.hakancevik.newsappbihaber.view.tabbed.TechnologyFragment
import javax.inject.Inject


class NewsFragmentFactory @Inject constructor(
    private val newsAdapter: NewsAdapter,
    private val searchNewsAdapter: SearchNewsAdapter,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            BreakingNewsFragment::class.java.name -> BreakingNewsFragment(newsAdapter)
            SavedNewsFragment::class.java.name -> SavedNewsFragment(newsAdapter)
            SearchNewsFragment::class.java.name -> SearchNewsFragment(searchNewsAdapter)

            CategoriesFragment::class.java.name -> CategoriesFragment(searchNewsAdapter)
            BusinessFragment::class.java.name -> BusinessFragment(searchNewsAdapter)
            EntertainmentFragment::class.java.name -> EntertainmentFragment(searchNewsAdapter)
            GeneralFragment::class.java.name -> GeneralFragment(searchNewsAdapter)
            HealthFragment::class.java.name -> HealthFragment(searchNewsAdapter)
            ScienceFragment::class.java.name -> GeneralFragment(searchNewsAdapter)
            SportsFragment::class.java.name -> GeneralFragment(searchNewsAdapter)
            TechnologyFragment::class.java.name -> GeneralFragment(searchNewsAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }


}