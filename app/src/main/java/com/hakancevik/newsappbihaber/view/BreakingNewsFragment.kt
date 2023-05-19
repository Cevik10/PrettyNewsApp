package com.hakancevik.newsappbihaber.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity


import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hakancevik.newsappbihaber.R

import com.hakancevik.newsappbihaber.adapter.NewsAdapter

import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding

import com.hakancevik.newsappbihaber.util.Constants.QUERY_PAGE_SIZE
import com.hakancevik.newsappbihaber.util.Resource
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.util.gone

import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.viewmodel.BreakingNewsViewModel
import javax.inject.Inject


class BreakingNewsFragment @Inject constructor(
    private val newsAdapter: NewsAdapter
) : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: BreakingNewsViewModel

    private val TAG = "BreakingNewsFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (requireActivity() as AppCompatActivity).findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.mToolbar)
        toolbar?.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.title = "NewsApp"

        }

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.show()


        viewModel = ViewModelProvider(requireActivity())[BreakingNewsViewModel::class.java]
        setupRecyclerView()


        newsAdapter.setOnItemClickListener {
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }


        subscribeToObservers()


        binding.connectionTryAgainButton.setOnClickListener {
            viewModel.getBreakingNews("us")
        }


    }

    private fun subscribeToObservers() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    activity?.customToast("başarılı")
                    hideProgressBar()
                    viewModel.breakingNewsInfo.value = false

                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())

                        if (newsResponse.totalResults != null) {
                            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.breakingNewsPage == totalPages
                            if (isLastPage) {
                                binding.recyclerViewBreakingNews.setPadding(0, 0, 0, 0)
                            }
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    activity?.customToast("api limit")
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                    }
                    viewModel.breakingNewsInfo.value = true

                }

                is Resource.Loading -> {
                    showProgressBar()
                    viewModel.breakingNewsInfo.value = false
                }
            }

        }


        viewModel.breakingNewsInfo.observe(viewLifecycleOwner) {
            if (it) {
                binding.internetConnectionInfoLayout.show()
                binding.recyclerViewBreakingNews.gone()
            } else {
                binding.recyclerViewBreakingNews.show()
                binding.internetConnectionInfoLayout.gone()
            }
        }


    }


    private fun hideProgressBar() {
        binding.paginationProgressBar.hide()
        binding.recyclerViewBreakingNews.show()
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.show()
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun setupRecyclerView() {
        binding.recyclerViewBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}