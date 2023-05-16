package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.adapter.SearchNewsAdapter
import com.hakancevik.newsappbihaber.databinding.FragmentSearchNewsBinding
import com.hakancevik.newsappbihaber.util.Constants.QUERY_PAGE_SIZE
import com.hakancevik.newsappbihaber.util.Resource
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchNewsFragment @Inject constructor(
    private val searchNewsAdapter: SearchNewsAdapter
) : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SearchNewsFragment"
    lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
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

        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]
        setupRecyclerView()


        searchNewsAdapter.setOnItemClickListener {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it, R.id.searchNewsFragment)
            findNavController().navigate(action)
        }


        var job: Job? = null
        binding.searchViewEditText.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(600L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString().trim())
                    }
                }
            }
        }

        binding.clearSearchEditText.setOnClickListener {
            binding.searchViewEditText.text.clear()
            //searchNewsAdapter.differ.submitList(arrayListOf())
            searchNewsAdapter.newsList = arrayListOf()
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        //searchNewsAdapter.differ.submitList(newsResponse.articles?.toList())
                        searchNewsAdapter.newsList = newsResponse.articles?.toList() ?: arrayListOf()
                        if (newsResponse.totalResults != null) {
                            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.searchNewsPage == totalPages
                            if (isLastPage) {
                                binding.recyclerViewSearchNews.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                        requireActivity().customToast("message: sıkıntı var")
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })


    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.hide()
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
                viewModel.searchNews(binding.searchViewEditText.text.toString().trim())
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
        binding.recyclerViewSearchNews.apply {
            adapter = searchNewsAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
            //searchNewsAdapter.differ.submitList(arrayListOf())
            searchNewsAdapter.newsList = arrayListOf()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}