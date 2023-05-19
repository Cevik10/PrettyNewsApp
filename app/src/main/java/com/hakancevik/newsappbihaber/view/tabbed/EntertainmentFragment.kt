package com.hakancevik.newsappbihaber.view.tabbed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView


import com.hakancevik.newsappbihaber.adapter.SearchNewsAdapter

import com.hakancevik.newsappbihaber.databinding.FragmentEntertainmentBinding
import com.hakancevik.newsappbihaber.util.Constants

import com.hakancevik.newsappbihaber.util.Resource
import com.hakancevik.newsappbihaber.util.gone

import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.view.CategoriesFragmentDirections

import com.hakancevik.newsappbihaber.viewmodel.tabbed.EntertainmentViewModel

import javax.inject.Inject


class EntertainmentFragment @Inject constructor(
    private val searchNewsAdapter: SearchNewsAdapter
) : Fragment() {


    private var _binding: FragmentEntertainmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EntertainmentViewModel

    private val TAG = "EntertainmentFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntertainmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[EntertainmentViewModel::class.java]
        setupRecyclerView()

        viewModel.getEntertainmentNews("us")

        searchNewsAdapter.setOnItemClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }


        subscribeToObservers()


        binding.connectionTryAgainButton.setOnClickListener {
            viewModel.getEntertainmentNews("us")
        }


    }

    private fun subscribeToObservers() {

        viewModel.entertainmentNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    viewModel.entertainmentConnectionInfo.value = false

                    response.data?.let { newsResponse ->

                        searchNewsAdapter.newsList = newsResponse.articles?.toList() ?: emptyList()


                        if (newsResponse.totalResults != null) {
                            val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.entertainmentNewsPage == totalPages
                            if (isLastPage) {
                                binding.recyclerViewEntertainment.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    viewModel.entertainmentConnectionInfo.value = true
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                    viewModel.entertainmentConnectionInfo.value = false
                }
            }


        })


        viewModel.entertainmentConnectionInfo.observe(viewLifecycleOwner) {
            if (it) {
                binding.internetConnectionInfoLayout.show()
                binding.recyclerViewEntertainment.gone()
            } else {
                binding.recyclerViewEntertainment.show()
                binding.internetConnectionInfoLayout.gone()
            }
        }


    }


    private fun hideProgressBar() {
        binding.paginationProgressBar.hide()
        binding.recyclerViewEntertainment.show()
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

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getEntertainmentNews("us")
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
        binding.recyclerViewEntertainment.apply {
            adapter = searchNewsAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@EntertainmentFragment.scrollListener)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    companion object {
        fun newInstance(searchNewsAdapter: SearchNewsAdapter): EntertainmentFragment {
            return EntertainmentFragment(searchNewsAdapter)
        }
    }

}