package com.hakancevik.newsappbihaber.view.tabbed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.hakancevik.newsappbihaber.adapter.SearchNewsAdapter
import com.hakancevik.newsappbihaber.databinding.FragmentTechnologyBinding
import com.hakancevik.newsappbihaber.util.Constants
import com.hakancevik.newsappbihaber.util.Resource

import com.hakancevik.newsappbihaber.util.gone
import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.view.CategoriesFragmentDirections
import com.hakancevik.newsappbihaber.viewmodel.tabbed.TechnologyViewModel

import javax.inject.Inject


class TechnologyFragment @Inject constructor(
    private val searchNewsAdapter: SearchNewsAdapter
) : Fragment() {


    private var _binding: FragmentTechnologyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TechnologyViewModel

    private val TAG = "TechnologyFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTechnologyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TechnologyViewModel::class.java]
        setupRecyclerView()

        viewModel.getTechnologyNews("us")

        searchNewsAdapter.setOnItemClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }


        subscribeToObservers()




        binding.connectionTryAgainButton.setOnClickListener {
            viewModel.getTechnologyNews("us")
        }


    }

    private fun subscribeToObservers() {
        viewModel.technologyNews.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {

                    hideProgressBar()
                    viewModel.technologyConnectionInfo.value = false

                    response.data?.let { newsResponse ->

                        searchNewsAdapter.newsList = newsResponse.articles?.toList() ?: arrayListOf()

                        if (newsResponse.totalResults != null) {
                            val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.technologyNewsPage == totalPages
                            if (isLastPage) {
                                binding.recyclerViewTechnology.setPadding(0, 0, 0, 0)
                            }
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    viewModel.technologyConnectionInfo.value = true
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                        binding.internetConnectionInfoLayout.show()
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                    viewModel.technologyConnectionInfo.value = false
                }
            }


        }



        viewModel.technologyConnectionInfo.observe(viewLifecycleOwner) {
            if (it) {
                binding.internetConnectionInfoLayout.show()
                binding.recyclerViewTechnology.gone()
            } else {
                binding.recyclerViewTechnology.show()
                binding.internetConnectionInfoLayout.gone()
            }
        }

    }


    private fun hideProgressBar() {
        binding.paginationProgressBar.hide()
        binding.recyclerViewTechnology.show()
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
                viewModel.getTechnologyNews("us")
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
        binding.recyclerViewTechnology.apply {
            adapter = searchNewsAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@TechnologyFragment.scrollListener)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(searchNewsAdapter: SearchNewsAdapter): TechnologyFragment {
            return TechnologyFragment(searchNewsAdapter)
        }
    }

}
