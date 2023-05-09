package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.adapter.ViewPagerAdapter
import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding


class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)







    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}