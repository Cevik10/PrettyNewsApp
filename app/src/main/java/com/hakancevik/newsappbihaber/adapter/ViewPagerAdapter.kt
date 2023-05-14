package com.hakancevik.newsappbihaber.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hakancevik.newsappbihaber.view.tabbed.BusinessFragment
import com.hakancevik.newsappbihaber.view.tabbed.EntertainmentFragment
import com.hakancevik.newsappbihaber.view.tabbed.GeneralFragment
import com.hakancevik.newsappbihaber.view.tabbed.HealthFragment
import com.hakancevik.newsappbihaber.view.tabbed.ScienceFragment
import com.hakancevik.newsappbihaber.view.tabbed.SportsFragment
import com.hakancevik.newsappbihaber.view.tabbed.TechnologyFragment
import javax.inject.Inject

class ViewPagerAdapter @Inject constructor (private val newsAdapter: NewsAdapter,fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 7
    }


    override fun createFragment(position: Int): Fragment {


        return when (position) {
            0 -> BusinessFragment.newInstance(newsAdapter)
            1 -> EntertainmentFragment.newInstance(newsAdapter)
            2 -> GeneralFragment.newInstance()
            3 -> HealthFragment.newInstance()
            4 -> ScienceFragment.newInstance()
            5 -> SportsFragment.newInstance()
            6 -> TechnologyFragment.newInstance()
            else -> Fragment()
        }
    }
}